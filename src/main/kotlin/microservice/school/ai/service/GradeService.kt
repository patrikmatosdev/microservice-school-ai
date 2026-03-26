package microservice.school.ai.service

import microservice.school.ai.model.Grade
import microservice.school.ai.repository.GradeRepository
import org.springframework.stereotype.Service
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Service
class GradeService(
    private val repository: GradeRepository,
    private val openAIService: OpenAIService
) {

    fun findAll(): List<Grade> {
        return repository.findAll()
    }

    fun findReproved(): List<Grade> {
        val grades = repository.findAll()

        val data = grades.joinToString("\n") {
            val total =
                it.firstTermGrade +
                        it.secondTermGrade +
                        it.thirdTermGrade +
                        it.fourthTermGrade

            "ID: ${it.studentId}, Total: $total"
        }

        val rawResponse = openAIService.analyzeStudents(data)

        val mapper = jacksonObjectMapper()

        val ids: List<Long> = try {
            val jsonNode = mapper.readTree(rawResponse)

            val content = jsonNode["choices"][0]["message"]["content"].asText()

            val clean = content
                .replace("```json", "")
                .replace("```", "")
                .trim()

            println("IA RESPONSE: $clean")
            mapper.readValue(clean)
        } catch (e: Exception) {
            println("ERRO: ${e.message}")
            emptyList()
        }

        return grades.filter { it.studentId in ids }
    }
}