package com.upiyptk.rlahealthy

data class PatientTimeData(
    var number: Long? = null,
    var heart: Int? = null,
    var temperature: Int? = null,
    var glucose: Int? = null,
    var day: Int? = null,
    var month: Int? = null,
    var year: Long? = null
)