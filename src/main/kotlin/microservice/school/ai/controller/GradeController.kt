package microservice.school.ai.controller

import microservice.school.ai.model.Grade
import microservice.school.ai.service.GradeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grades")
class GradeController(private val service: GradeService) {

    @GetMapping("/all")
    fun getAll(): List<Grade> {
        return service.findAll()
    }

    @GetMapping("/reproved")
    fun getReproved(): List<Grade> {
        return service.findReproved()
    }

    @GetMapping("/approved")
    fun getApproved(): List<Grade> {
        return service.findApproved()
    }

    @GetMapping("/{id}/feedback")
    fun feedback(@PathVariable id: Long): String {
        return service.generateStudentFeedback(id)
    }
}