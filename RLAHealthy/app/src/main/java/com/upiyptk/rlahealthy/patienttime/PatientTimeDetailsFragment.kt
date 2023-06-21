package com.upiyptk.rlahealthy.patienttime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.upiyptk.rlahealthy.R

class PatientTimeDetailsFragment: Fragment() {
    private lateinit var tvPatientDay: TextView
    private lateinit var tvPatientMonth: TextView
    private lateinit var tvPatientYear: TextView
    private lateinit var tvPatientHeart: TextView
    private lateinit var tvPatientTemperature: TextView
    private lateinit var tvPatientGlucose: TextView
    private lateinit var btnBack: ImageView
    private val args by navArgs<PatientTimeDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_time_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPatientDay = view.findViewById(R.id.tv_patient_day)
        tvPatientMonth = view.findViewById(R.id.tv_patient_month)
        tvPatientYear = view.findViewById(R.id.tv_patient_year)
        tvPatientHeart = view.findViewById(R.id.tv_patient_heart)
        tvPatientTemperature = view.findViewById(R.id.tv_patient_temperature)
        tvPatientGlucose = view.findViewById(R.id.tv_patient_glucose)
        btnBack = view.findViewById(R.id.button_back)

        tvPatientDay.text = "${args.patientTime.day}"
        tvPatientMonth.text = when(args.patientTime.month) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> "Error"
        }
        tvPatientYear.text = "${args.patientTime.year}"
        tvPatientHeart.text = "${args.patientTime.heart}"
        tvPatientTemperature.text = "${args.patientTime.temperature}"
        tvPatientGlucose.text = "${args.patientTime.glucose}"

        btnBack.setOnClickListener {
            val action = PatientTimeDetailsFragmentDirections.actionPatientTimeDetailsFragmentToPatientTimeFragment()
            findNavController().navigate(action)
        }
    }
}