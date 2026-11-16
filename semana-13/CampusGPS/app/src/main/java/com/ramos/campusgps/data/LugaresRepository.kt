package com.ramos.campusgps.data

import com.ramos.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
