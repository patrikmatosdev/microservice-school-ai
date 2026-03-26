package microservice.school.ai.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb-school-grades")
data class Grade(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "student_id")
    val studentId: Long,

    @Column(name = "student_name")
    val studentName: String,

    @Column(name = "first_term_grade")
    val firstTermGrade: Double,

    @Column(name = "second_term_grade")
    val secondTermGrade: Double,

    @Column(name = "third_term_grade")
    val thirdTermGrade: Double,

    @Column(name = "fourth_term_grade")
    val fourthTermGrade: Double,

    @Column(name = "created_at")
    val createdAt: LocalDateTime
)