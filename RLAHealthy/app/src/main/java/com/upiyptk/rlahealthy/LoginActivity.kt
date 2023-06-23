package com.upiyptk.rlahealthy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity: AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var etDoctorName: EditText
    private lateinit var etDoctorPassword: EditText
    private lateinit var btnLogin: AppCompatButton
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnBack = findViewById(R.id.button_back)
        etDoctorName = findViewById(R.id.et_doctor_name)
        etDoctorPassword = findViewById(R.id.et_doctor_password)
        btnLogin = findViewById(R.id.button_login)
        ref = FirebaseDatabase.getInstance().reference

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
}