package com.upiyptk.rlahealthy.patient

data class PatientData(
    var number: Long? = null,
    var image: Int? = 0,
    var name: String? = "Error",
    var gender: Int? = 0,
    var age: Int? = null,
    var handphone: String? = "Error",
    var heart: Int? = null,
    var temperature: Int? = null,
    var glucose: Int? = null
)