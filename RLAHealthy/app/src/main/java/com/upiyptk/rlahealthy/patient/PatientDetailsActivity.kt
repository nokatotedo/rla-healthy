package com.upiyptk.rlahealthy.patient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.upiyptk.rlahealthy.MainActivity
import com.upiyptk.rlahealthy.R

class PatientDetailsActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_NUMBER = "extra_number"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_AGE = "extra_age"
        const val EXTRA_HANDPHONE = "extra_handphone"
        const val EXTRA_HEART = "extra_heart"
        const val EXTRA_TEMPERATURE = "extra_temperature"
        const val EXTRA_GLUCOSE = "extra_glucose"
    }

    private lateinit var btnBack: ImageView
    private lateinit var ivPatientImage: ImageView
    private lateinit var tvPatientName: TextView
    private lateinit var tvPatientAge: TextView
    private lateinit var tvPatientHeart: TextView
    private lateinit var tvPatientTemperature: TextView
    private lateinit var tvPatientGlucose: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_details)

        btnBack = findViewById(R.id.button_back)
        ivPatientImage = findViewById(R.id.iv_patient_image)
        tvPatientName = findViewById(R.id.tv_patient_name)
        tvPatientAge = findViewById(R.id.tv_patient_age)
        tvPatientHeart = findViewById(R.id.tv_patient_heart)
        tvPatientTemperature = findViewById(R.id.tv_patient_temperature)
        tvPatientGlucose = findViewById(R.id.tv_patient_glucose)

        val patientImage = when(intent.getStringExtra(EXTRA_IMAGE)) {
            "1" -> R.drawable.patient_three
            "2" -> R.drawable.patient_four
            "3" -> R.drawable.patient_six
            "4" -> R.drawable.patient_eight
            "5" -> R.drawable.patient_one
            "6" -> R.drawable.patient_two
            "7" -> R.drawable.patient_five
            "8" -> R.drawable.patient_seven
            "9" -> R.drawable.patient_nine
            else -> when(intent.getStringExtra(EXTRA_GENDER) == "0") {
                true -> R.drawable.patient_three
                false -> R.drawable.patient_one
            }
        }
        val patientName = intent.getStringExtra(EXTRA_NAME)
        val patientAge = intent.getStringExtra(EXTRA_AGE)
        val patientHeart = intent.getStringExtra(EXTRA_HEART)
        val patientTemperature = intent.getStringExtra(EXTRA_TEMPERATURE)
        val patientGlucose = intent.getStringExtra(EXTRA_GLUCOSE)

        Glide.with(this)
            .load(patientImage)
            .into(ivPatientImage)
        tvPatientName.text = "$patientName,"
        tvPatientAge.text = patientAge
        tvPatientHeart.text = patientHeart
        tvPatientTemperature.text = patientTemperature
        tvPatientGlucose.text = patientGlucose

        btnBack.setOnClickListener {
            Intent(this@PatientDetailsActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}