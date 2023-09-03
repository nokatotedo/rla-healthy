package com.upiyptk.rlahealthy.patientemergency

data class PatientEmergencyData(
    var number: Long? = null,
    var room: Long? = 0,
    var heart: Int? = null,
    var temperature: Double? = null,
    var glucose: Double? = null,
    var new: Int? = 0
)