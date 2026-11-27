package com.ramos.avisoscampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramos.avisoscampus.model.Aviso
import com.ramos.avisoscampus.model.Origen

@Entity(tableName = "avisos")
data class AvisoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val texto: String,
    val origen: String,
    val hora: String
) {
    fun aAviso(): Aviso = Aviso(id, titulo, texto, Origen.valueOf(origen), hora)
}
