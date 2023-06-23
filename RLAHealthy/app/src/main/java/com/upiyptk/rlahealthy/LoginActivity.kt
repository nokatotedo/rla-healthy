package com.upiyptk.rlahealthy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.upiyptk.rlahealthy.patient.PatientData

class LoginActivity: AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var ivDoctorImage: ImageView
    private lateinit var etDoctorName: EditText
    private lateinit var etDoctorPassword: EditText
    private lateinit var btnLogin: AppCompatButton
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnBack = findViewById(R.id.button_back)
        ivDoctorImage = findViewById(R.id.iv_doctor_image)
        etDoctorName = findViewById(R.id.et_doctor_name)
        etDoctorPassword = findViewById(R.id.et_doctor_password)
        btnLogin = findViewById(R.id.button_login)
        ref = FirebaseDatabase.getInstance().reference

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
                    Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })

        val image = when((0..1).random()) {
            0 -> R.drawable.doctor_one
            1 -> R.drawable.doctor_two
            else -> R.drawable.doctor_three
        }
        
        Glide.with(this)
            .load(image)
            .into(ivDoctorImage)

        ref.child("doctor").child("name").get()
            .addOnSuccessListener { name ->
                val doctorName = name.value.toString()
                ref.child("doctor").child("password").get()
                    .addOnSuccessListener { password ->
                        val doctorPassword = password.value.toString()

                        btnLogin.setOnClickListener {
                            if(!FunctionPack.isOnline(this)) {
                                Toast.makeText(this@LoginActivity, "Kesalahan Jaringan", Toast.LENGTH_LONG).show()
                                return@setOnClickListener
                            }

                            if(etDoctorName.text.toString().trim().isEmpty()) {
                                etDoctorName.error = "Nama Kosong"
                                return@setOnClickListener
                            }
                            if(etDoctorPassword.text.toString().trim().isEmpty()) {
                                etDoctorPassword.error = "Kata Sandi Kosong"
                                return@setOnClickListener
                            }

                            val nameValue = etDoctorName.text.toString()
                            val passwordValue = etDoctorPassword.text.toString()
                            if((nameValue == doctorName) && (passwordValue == doctorPassword)) {
                                Intent(this@LoginActivity, MainActivity::class.java).also {
                                    it.putExtra(MainActivity.EXTRA_LOGIN, "1")
                                    startActivity(it)
                                }
                            } else {
                                Toast.makeText(this, "Nama/Kata Sandi Salah", Toast.LENGTH_LONG).show()
                                return@setOnClickListener
                            }
                        }
                    }
            }

        btnBack.setOnClickListener {
            Intent(this@LoginActivity, MainActivity::class.java).also {
                startActivity(it)
            }
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
}