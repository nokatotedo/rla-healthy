package com.upiyptk.rlahealthy.patientemergency

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.upiyptk.rlahealthy.FunctionPack
import com.upiyptk.rlahealthy.MainActivity
import com.upiyptk.rlahealthy.R
import com.upiyptk.rlahealthy.patient.PatientData
import java.time.LocalDate

class PatientEmergencyDetailsActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_NUMBER = "extra_number"
        const val EXTRA_HEART = "extra_heart"
        const val EXTRA_TEMPERATURE = "extra_temperature"
        const val EXTRA_GLUCOSE = "extra_glucose"
    }

    private lateinit var btnBack: ImageView
    private lateinit var cbPatientNew: CheckBox
    private lateinit var actvPatientName: AutoCompleteTextView
    private lateinit var tilPatientName: TextInputLayout
    private lateinit var rbPatientMale: RadioButton
    private lateinit var rbPatientFemale: RadioButton
    private lateinit var etPatientAge: EditText
    private lateinit var etPatientHandphone: EditText
    private lateinit var tvPatientHeart: TextView
    private lateinit var tvPatientTemperature: TextView
    private lateinit var tvPatientGlucose: TextView
    private lateinit var btnSave: AppCompatButton
    private lateinit var btnDelete: AppCompatButton
    private lateinit var ref: DatabaseReference
    private var arrayNumber: ArrayList<Long> = arrayListOf()
    private var arrayName: ArrayList<String> = arrayListOf()
    private var _patientNumber: String = "Error"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_emergency_details)

        btnBack = findViewById(R.id.button_back)
        cbPatientNew = findViewById(R.id.cb_patient_new)
        actvPatientName = findViewById(R.id.actv_patient_name)
        tilPatientName = findViewById(R.id.til_patient_name)
        rbPatientMale = findViewById(R.id.rb_patient_male)
        rbPatientFemale = findViewById(R.id.rb_patient_female)
        etPatientAge = findViewById(R.id.et_patient_age)
        etPatientHandphone = findViewById(R.id.et_patient_handphone)
        tvPatientHeart = findViewById(R.id.tv_patient_heart)
        tvPatientTemperature = findViewById(R.id.tv_patient_temperature)
        tvPatientGlucose = findViewById(R.id.tv_patient_glucose)
        btnSave = findViewById(R.id.button_save)
        btnDelete = findViewById(R.id.button_delete)
        ref = FirebaseDatabase.getInstance().reference

        val login = intent.getStringExtra(EXTRA_LOGIN).toString()
        val patientNumber = intent.getStringExtra(EXTRA_NUMBER)!!
        val patientHeart = intent.getStringExtra(EXTRA_HEART)
        val patientTemperature = intent.getStringExtra(EXTRA_TEMPERATURE)
        val patientGlucose = intent.getStringExtra(EXTRA_GLUCOSE)

        cbPatientNew.setOnClickListener {
            clearPatient()
            if(cbPatientNew.isChecked) {
                actvPatientName.inputType = InputType.TYPE_CLASS_TEXT
                actvPatientName.setAdapter(null)
                tilPatientName.endIconMode = TextInputLayout.END_ICON_NONE
                rbPatientMale.isClickable = true
                rbPatientFemale.isClickable = true
                etPatientAge.inputType = InputType.TYPE_CLASS_NUMBER
                etPatientHandphone.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                actvPatientName.inputType = InputType.TYPE_NULL
                actvPatientName.setText("Pilih Pasien")
                tilPatientName.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                rbPatientMale.isClickable = false
                rbPatientFemale.isClickable = false
                etPatientAge.inputType = InputType.TYPE_NULL
                etPatientHandphone.inputType = InputType.TYPE_NULL
                getPatient()
            }
        }

        rbPatientMale.setOnClickListener {
            rbPatientFemale.isChecked = false
        }

        rbPatientFemale.setOnClickListener {
            rbPatientMale.isChecked = false
        }

        tvPatientHeart.text = patientHeart
        tvPatientTemperature.text = patientTemperature
        tvPatientGlucose.text = patientGlucose

        ref.child("patientEmergency").child("p$patientNumber").child("new")
            .setValue(0)

        ref.child("patient")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientData::class.java)
                            if(patientValue != null) {
                                if(patientValue.notification == 1) {
                                    getNotification(patientValue.number!!.toInt(), patientValue.name.toString())
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PatientEmergencyDetailsActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })

        btnSave.setOnClickListener {
            if(!FunctionPack.isOnline(this)) {
                Toast.makeText(this@PatientEmergencyDetailsActivity, "Kesalahan Jaringan", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(actvPatientName.text.toString().trim().isEmpty()) {
                actvPatientName.error = "Nama Kosong"
                return@setOnClickListener
            }
            if(etPatientAge.text.toString().trim().isEmpty()) {
                etPatientAge.error = "Umur Kosong"
                return@setOnClickListener
            }
            if(etPatientHandphone.text.toString().trim().isEmpty()) {
                etPatientHandphone.error = "Handphone Kosong"
                return@setOnClickListener
            }
            if(etPatientHandphone.text.toString().first() != '0') {
                etPatientHandphone.error = "Harap Diawali 0"
                return@setOnClickListener
            }

            val numberVal = when(cbPatientNew.isChecked) {
                true -> Integer.parseInt(patientNumber)
                false -> Integer.parseInt(_patientNumber)
            }
            val nameVal = actvPatientName.text.toString()
            val genderVal = when(rbPatientMale.isChecked) {
                true -> 0
                false -> 1
            }
            val imageVal = if(genderVal == 0) {
                (1..4).random()
            } else {
                (5..9).random()
            }
            val ageVal = Integer.parseInt(etPatientAge.text.toString())
            val handphoneVal = etPatientHandphone.text.toString().replaceFirstChar { "+62" }
            val heartVal = Integer.parseInt(tvPatientHeart.text.toString())
            val temperatureVal = Integer.parseInt(tvPatientTemperature.text.toString())
            val glucoseVal = Integer.parseInt(tvPatientGlucose.text.toString())

            ref.child("patient").child("p$numberVal").child("number")
                .setValue(numberVal)
            ref.child("patient").child("p$numberVal").child("name")
                .setValue(nameVal)
            ref.child("patient").child("p$numberVal").child("gender")
                .setValue(genderVal)
            ref.child("patient").child("p$numberVal").child("image")
                .get().addOnSuccessListener {
                    if(it.value.toString().isEmpty()) {
                        ref.child("patient").child("p$numberVal").child("image")
                            .setValue(imageVal)
                    }
                }
            ref.child("patient").child("p$numberVal").child("age")
                .setValue(ageVal)
            ref.child("patient").child("p$numberVal").child("handphone")
                .setValue(handphoneVal)
            ref.child("patient").child("p$numberVal").child("heart")
                .setValue(heartVal)
            ref.child("patient").child("p$numberVal").child("temperature")
                .setValue(temperatureVal)
            ref.child("patient").child("p$numberVal").child("glucose")
                .setValue(glucoseVal)
            ref.child("patient").child("p$numberVal").child("notification")
                .setValue(0)

            ref.child("lastValue").child("time")
                .get().addOnSuccessListener {
                    val time = Integer.parseInt(it.value.toString())
                    val dayVal = LocalDate.now().dayOfMonth
                    val monthVal = LocalDate.now().monthValue
                    val yearVal = LocalDate.now().year

                    ref.child("lastValue").child("time")
                        .setValue(time + 1)
                    ref.child("patientTime").child("t$time").child("number")
                        .setValue(numberVal)
                    ref.child("patientTime").child("t$time").child("time")
                        .setValue(time)
                    ref.child("patientTime").child("t$time").child("day")
                        .setValue(dayVal)
                    ref.child("patientTime").child("t$time").child("month")
                        .setValue(monthVal)
                    ref.child("patientTime").child("t$time").child("year")
                        .setValue(yearVal)
                    ref.child("patientTime").child("t$time").child("heart")
                        .setValue(heartVal)
                    ref.child("patientTime").child("t$time").child("temperature")
                        .setValue(temperatureVal)
                    ref.child("patientTime").child("t$time").child("glucose")
                        .setValue(glucoseVal)
                }

            ref.child("patientEmergency").child("p$patientNumber").removeValue()
            backIntent(login)
        }

        btnDelete.setOnClickListener {
            if(!FunctionPack.isOnline(this)) {
                Toast.makeText(this@PatientEmergencyDetailsActivity, "Kesalahan Jaringan", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hapus Data Pasien")
            builder.setMessage("Yakin Hapus?")
            builder.setPositiveButton("Ya") { _, _ ->
                ref.child("patientEmergency").child("p$patientNumber").removeValue()
                backIntent(login)
            }
            builder.setNegativeButton("Tidak") { _, _ ->
                return@setNegativeButton
            }

            val alert: AlertDialog = builder.create()
            alert.show()
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#212121"))
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#cd615a"))
        }

        btnBack.setOnClickListener {
            backIntent(login)
        }
    }

    private fun getNotification(number: Int, patient: String) {
        val channel = NotificationChannel("RLA_Healthy", "RLA Healthy", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, "RLA_Healthy")
            .setContentText("$patient membutuhkan bantuan!")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val compat = NotificationManagerCompat.from(this)
        compat.notify(number, builder)
    }

    private fun getPatient() {
        ref.child("patient")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayNumber.clear()
                    arrayName.clear()
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientData::class.java)
                            if(patientValue != null) {
                                arrayNumber.add(patientValue.number!!)
                                arrayName.add(patientValue.name!!.replace("\"", ""))
                            }
                        }
                        val nameValue = arrayName.toTypedArray()
                        val nameAdapter = ArrayAdapter(this@PatientEmergencyDetailsActivity, android.R.layout.simple_list_item_1, nameValue)
                        actvPatientName.setAdapter(nameAdapter)
                        actvPatientName.setOnItemClickListener { _, _, p2, _ ->
                            _patientNumber = arrayNumber[p2].toString()

                            ref.child("patient").child("p$_patientNumber").child("gender")
                                .get().addOnSuccessListener {
                                    if(it.value.toString() == "0") {
                                        rbPatientMale.isChecked = true
                                        rbPatientFemale.isChecked = false
                                    } else {
                                        rbPatientMale.isChecked = false
                                        rbPatientFemale.isChecked = true
                                    }
                                }

                            ref.child("patient").child("p$_patientNumber").child("age")
                                .get().addOnSuccessListener {
                                    etPatientAge.setText(it.value.toString())
                                }

                            ref.child("patient").child("p$_patientNumber").child("handphone")
                                .get().addOnSuccessListener {
                                    etPatientHandphone.setText(it.value.toString().replace("+62","0"))
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PatientEmergencyDetailsActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun clearPatient() {
        actvPatientName.error = null
        actvPatientName.setText("")
        rbPatientMale.isChecked = true
        rbPatientFemale.isChecked = false
        etPatientAge.error = null
        etPatientAge.setText("")
        etPatientHandphone.error = null
        etPatientHandphone.setText("")
    }

    private fun backIntent(login: String) {
        Intent(this@PatientEmergencyDetailsActivity, MainActivity::class.java).also {
            it.putExtra(MainActivity.EXTRA_LOGIN, login)
            startActivity(it)
        }
    }
}