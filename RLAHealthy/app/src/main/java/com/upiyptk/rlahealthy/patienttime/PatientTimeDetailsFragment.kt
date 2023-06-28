package com.upiyptk.rlahealthy.patienttime

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.*
import com.upiyptk.rlahealthy.FunctionPack
import com.upiyptk.rlahealthy.MainActivity
import com.upiyptk.rlahealthy.R
import java.net.URLEncoder

class PatientTimeDetailsFragment: Fragment() {
    private lateinit var btnWhatsapp: ImageView
    private lateinit var tvPatientDay: TextView
    private lateinit var tvPatientMonth: TextView
    private lateinit var tvPatientYear: TextView
    private lateinit var tvPatientHeart: TextView
    private lateinit var tvPatientTemperature: TextView
    private lateinit var tvPatientGlucose: TextView
    private lateinit var btnDeletePatient: AppCompatButton
    private lateinit var btnDeletePatientTime: AppCompatButton
    private lateinit var btnBack: ImageView
    private lateinit var ref: DatabaseReference
    private val args by navArgs<PatientTimeDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_time_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnWhatsapp = view.findViewById(R.id.button_whatsapp)
        tvPatientDay = view.findViewById(R.id.tv_patient_day)
        tvPatientMonth = view.findViewById(R.id.tv_patient_month)
        tvPatientYear = view.findViewById(R.id.tv_patient_year)
        tvPatientHeart = view.findViewById(R.id.tv_patient_heart)
        tvPatientTemperature = view.findViewById(R.id.tv_patient_temperature)
        tvPatientGlucose = view.findViewById(R.id.tv_patient_glucose)
        btnDeletePatient = view.findViewById(R.id.button_delete_patient)
        btnDeletePatientTime = view.findViewById(R.id.button_delete_patient_time)
        btnBack = view.findViewById(R.id.button_back)
        ref = FirebaseDatabase.getInstance().reference

        val login = requireActivity().intent.getStringExtra("extra_login").toString()
        val patientNumber = requireActivity().intent.getStringExtra("extra_number").toString()
        val patientName = requireActivity().intent.getStringExtra("extra_name").toString()
        val patientGender = when(requireActivity().intent.getStringExtra("extra_gender")) {
            "0" -> "Bapak"
            "1" -> "Ibu"
            else -> "Bapak"
        }
        val patientHandphone = requireActivity().intent.getStringExtra("extra_handphone").toString()

        btnWhatsapp.setOnClickListener {
            startWhatsapp(patientName, patientGender, patientHandphone)
        }

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

        if(login == "1") {
            btnDeletePatient.visibility = View.VISIBLE
            btnDeletePatientTime.visibility = View.VISIBLE
        } else {
            btnDeletePatient.visibility = View.GONE
            btnDeletePatientTime.visibility = View.GONE
        }

        btnDeletePatient.setOnClickListener {
            if(!FunctionPack.isOnline(requireContext())) {
                Toast.makeText(requireActivity(), "Kesalahan Jaringan", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Hapus Data Pasien")
            builder.setMessage("Yakin Hapus?")
            builder.setPositiveButton("Ya") { _, _ ->
                ref.child("patient").child("p$patientNumber").removeValue()
                ref.child("patientTime").orderByChild("number").equalTo(patientNumber.toDouble())
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()) {
                                for(patient in snapshot.children) {
                                    patient.ref.removeValue()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                        }
                    })
                backIntent()
            }
            builder.setNegativeButton("Tidak") { _, _ ->
                return@setNegativeButton
            }

            val alert: AlertDialog = builder.create()
            alert.show()
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#212121"))
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF8F80"))
        }

        btnDeletePatientTime.setOnClickListener {
            if(!FunctionPack.isOnline(requireContext())) {
                Toast.makeText(requireActivity(), "Kesalahan Jaringan", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Hapus Data Riwayat Pasien")
            builder.setMessage("Yakin Hapus?")
            builder.setPositiveButton("Ya") { _, _ ->
                ref.child("patientTime").child("t${args.patientTime.time}").removeValue()
                backFragment()
                ref.child("patientTime").orderByChild("number").equalTo(patientNumber.toDouble())
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.exists()) {
                                ref.child("patient").child("p$patientNumber").removeValue()
                                backIntent()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                        }
                    })
            }
            builder.setNegativeButton("Tidak") { _, _ ->
                return@setNegativeButton
            }

            val alert: AlertDialog = builder.create()
            alert.show()
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#212121"))
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF8F80"))
        }

        btnBack.setOnClickListener {
            backFragment()
        }
    }

    private fun startWhatsapp(name: String, gender: String, handphone: String) {
        try {
            val packageManager = requireActivity().packageManager
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=" + handphone + "&text=" +
                    URLEncoder.encode("Apakah ada yang bisa saya bantu, $gender $name?", "UTF-8")
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(url)
            if(intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(requireActivity(), "Mohon install Whatsapp terlebih dahulu", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {

        }
    }

    private fun backFragment() {
        val action = PatientTimeDetailsFragmentDirections.actionPatientTimeDetailsFragmentToPatientTimeFragment()
        findNavController().navigate(action)
    }

    private fun backIntent() {
        Intent(requireContext(), MainActivity::class.java).also {
            startActivity(it)
        }
    }
}