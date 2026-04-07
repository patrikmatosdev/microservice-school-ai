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

    fun findByStatus(status: Status): List<Grade> {
        val grades = repository.findAll()

        val data = grades.joinToString("\n") {
            val total =
                it.firstTermGrade +
                        it.secondTermGrade +
                        it.thirdTermGrade +
                        it.fourthTermGrade

            "ID: ${it.studentId}, Total: $total"
        }

        val rawResponse = openAIService.analyzeStudents(data, status)

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

    fun findReproved(): List<Grade> {
        return findByStatus(Status.REPROVADO)
    }

    fun findApproved(): List<Grade> {
        return findByStatus(Status.APROVADO)
    }

    fun generateStudentFeedback(id: Long): String {
        val grade = repository.findById(id).get()

        val total =
            grade.firstTermGrade +
                    grade.secondTermGrade +
                    grade.thirdTermGrade +
                    grade.fourthTermGrade

        val data = """
        Aluno: ${grade.studentName}
        Notas:
        1º: ${grade.firstTermGrade}
        2º: ${grade.secondTermGrade}
        3º: ${grade.thirdTermGrade}
        4º: ${grade.fourthTermGrade}
        Total: $total
    """.trimIndent()

        return openAIService.generateFeedback(data)
    }
}