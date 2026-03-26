package microservice.school.ai.controller

import microservice.school.ai.model.Grade
import microservice.school.ai.service.GradeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grades")
class GradeController(private val service: GradeService) {

    @GetMapping
    fun getAll(): List<Grade> {
        return service.findAll()
    }
}