package com.upiyptk.rlahealthy

data class PatientEmergencyData(
    var number: Long? = null,
    var heart: Int? = null,
    var temperature: Int? = null,
    var glucose: Int? = null,
    var new: Int? = 0
)