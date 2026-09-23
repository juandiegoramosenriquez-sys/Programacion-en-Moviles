package com.tecsup.semanaapp.data

data class Student(
    val id: Int,
    val studentCode: String,
    val name: String,
    val career: String,
    val email: String,
    val faculty: String,
    val bio: String
)

val sampleStudents = listOf(
    Student(
        id = 1,
        studentCode = "2024-0001",
        name = "Diego Ramos",
        career = "Ingeniería de Sistemas",
        email = "diego.ramos@example.com",
        faculty = "Ingeniería y Tecnología",
        bio = "Estudiante destacado con interés en desarrollo Android."
    ),
    Student(
        id = 2,
        studentCode = "2024-0002",
        name = "Maria Garcia",
        career = "Arquitectura",
        email = "maria.garcia@example.com",
        faculty = "Arquitectura y Diseño",
        bio = "Apasionada por el diseño sostenible y la planificación urbana."
    ),
    Student(
        id = 3,
        studentCode = "2024-0003",
        name = "Carlos Perez",
        career = "Medicina",
        email = "carlos.perez@example.com",
        faculty = "Ciencias de la Salud",
        bio = "Interesado en la investigación médica y salud pública."
    ),
    Student(
        id = 4,
        studentCode = "2024-0004",
        name = "Ana Lopez",
        career = "Derecho",
        email = "ana.lopez@example.com",
        faculty = "Derecho y Ciencias Políticas",
        bio = "Enfocada en el derecho corporativo y derechos humanos."
    ),
    Student(
        id = 5,
        studentCode = "2024-0005",
        name = "Luis Ramirez",
        career = "Administración",
        email = "luis.ramirez@example.com",
        faculty = "Gestión y Negocios",
        bio = "Especializándose en gestión de proyectos y emprendimiento."
    )
)
