package com.upiyptk.rlahealthy.patienttime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PatientTimeData(
    var number: Long? = null,
    var day: Int? = null,
    var month: Int? = null,
    var year: Long? = null,
    var heart: Int? = null,
    var temperature: Int? = null,
    var glucose: Int? = null
): Parcelable