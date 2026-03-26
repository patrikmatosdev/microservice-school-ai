package microservice.school.ai.repository

import microservice.school.ai.model.Grade
import org.springframework.data.jpa.repository.JpaRepository

interface GradeRepository : JpaRepository<Grade, Long>