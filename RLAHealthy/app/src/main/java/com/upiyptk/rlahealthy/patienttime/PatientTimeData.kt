package com.upiyptk.rlahealthy.patienttime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientTimeData(
    var number: Long? = null,
    var time: Long? = null,
    var day: Int? = null,
    var month: Int? = null,
    var year: Long? = null,
    var heart: Int? = null,
    var temperature: Double? = null,
    var glucose: Double? = null
): Parcelable