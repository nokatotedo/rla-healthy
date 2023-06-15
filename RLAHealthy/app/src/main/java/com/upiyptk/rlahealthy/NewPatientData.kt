package com.upiyptk.rlahealthy

data class NewPatientData(
    var id: Double? = null,
    var heart: Int? = null,
    var temperature: Int? = null,
    var glucose: Int? = null,
    var isNew: Boolean? = false
)