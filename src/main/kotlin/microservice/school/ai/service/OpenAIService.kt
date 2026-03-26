package microservice.school.ai.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import io.github.cdimascio.dotenv.Dotenv

@Service
class OpenAIService {
    private val dotenv = Dotenv.load()
    private val apiKey = dotenv["OPENAI_API_KEY"]
    private val restTemplate = RestTemplate()

    fun analyzeStudents(data: String): String {
        val url = "https://api.openai.com/v1/chat/completions"

        val headers = HttpHeaders()
        headers.setBearerAuth(apiKey)
        headers.contentType = MediaType.APPLICATION_JSON

        val body = mapOf(
            "model" to "gpt-4o-mini",
            "temperature" to 0,
            "messages" to listOf(
                mapOf(
                    "role" to "system",
                    "content" to """
                    Você é um professor.

                    Regra:
                    - Nota máxima: 40
                    - Mínimo: 32

                    Retorne APENAS um JSON com os IDs dos alunos reprovados.

                    Exemplo:
                    [1, 2, 3]

                    Se ninguém estiver reprovado, retorne [].

                    NÃO retorne objetos, apenas números.
                    """.trimIndent()
                ),
                mapOf(
                    "role" to "user",
                    "content" to data
                )
            )
        )

        val request = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(url, request, String::class.java)

        return response.body ?: "{}"
    }
}