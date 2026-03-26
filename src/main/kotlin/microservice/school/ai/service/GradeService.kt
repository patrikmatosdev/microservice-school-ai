package microservice.school.ai.service

import microservice.school.ai.model.Grade
import microservice.school.ai.repository.GradeRepository
import org.springframework.stereotype.Service

@Service
class GradeService(private val repository: GradeRepository) {

    fun findAll(): List<Grade> {
        return repository.findAll()
    }
}