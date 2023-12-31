package com.upiyptk.rlahealthy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.upiyptk.rlahealthy.patient.PatientAdapter
import com.upiyptk.rlahealthy.patient.PatientData
import com.upiyptk.rlahealthy.patient.PatientDetailsActivity
import com.upiyptk.rlahealthy.patientemergency.PatientEmergencyAdapter
import com.upiyptk.rlahealthy.patientemergency.PatientEmergencyData
import com.upiyptk.rlahealthy.patientemergency.PatientEmergencyDetailsActivity

class MainActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var svPatient: SearchView
    private lateinit var tvPatientEmergency: TextView
    private lateinit var rvPatientEmergency: RecyclerView
    private lateinit var rvPatient: RecyclerView
    private lateinit var ref: DatabaseReference
    private var arrayPatientSearch: ArrayList<PatientData> = arrayListOf()
    private var arrayPatientEmergency: ArrayList<PatientEmergencyData> = arrayListOf()
    private var arrayPatient: ArrayList<PatientData> = arrayListOf()
    private var login: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svPatient = findViewById(R.id.sv_patient)
        tvPatientEmergency = findViewById(R.id.tv_patient_emergency)
        rvPatientEmergency = findViewById(R.id.rv_patient_emergency)
        rvPatientEmergency.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPatientEmergency.setHasFixedSize(true)
        rvPatient = findViewById(R.id.rv_patient)
        rvPatient.layoutManager = GridLayoutManager(this, 2)
        rvPatient.setHasFixedSize(true)

        svPatient.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0!!.lowercase() == "login") {
                    Intent(this@MainActivity, LoginActivity::class.java).also {
                        startActivity(it)
                    }
                }

                getPatient(p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                getPatient(p0)
                return false
            }
        })

        login = intent.getStringExtra(EXTRA_LOGIN).toString()
        if(login == "1") {
            tvPatientEmergency.visibility = View.VISIBLE
            rvPatientEmergency.visibility = View.VISIBLE
        } else {
            tvPatientEmergency.visibility = View.GONE
            rvPatientEmergency.visibility = View.GONE
        }

        ref = FirebaseDatabase.getInstance().reference
        ref.child("patientEmergency")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayPatientEmergency.clear()
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientEmergencyData::class.java)
                            if(patientValue != null) {
                                arrayPatientEmergency.add(patientValue)
                            }
                        }
                        val patientAdapter = PatientEmergencyAdapter(arrayPatientEmergency)
                        rvPatientEmergency.adapter = patientAdapter
                        patientAdapter.setOnItemClickCallback(object: PatientEmergencyAdapter.OnItemClickCallback {
                            override fun onItemClicked(list: PatientEmergencyData) {
                                getPatientEmergencyDetails(list)
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })

        ref.child("patient")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayPatient.clear()
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientData::class.java)
                            if(patientValue != null) {
                                arrayPatient.add(patientValue)
                            }
                        }
                        val patientAdapter = PatientAdapter(arrayPatient)
                        rvPatient.adapter = patientAdapter
                        patientAdapter.setOnItemClickCallback(object: PatientAdapter.OnItemClickCallback {
                            override fun onItemClicked(list: PatientData) {
                                getPatientDetails(list)
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })

        ref.child("patientRoom")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientRoomData::class.java)
                            if(patientValue != null) {
                                if(patientValue.notification == 1) {
                                    if(login == "1") getNotification(patientValue.room!!.toInt())
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getNotification(room: Int) {
        if(room > 0) {
            val channel = NotificationChannel("RLA_Healthy", "RLA Healthy", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            val builder = NotificationCompat.Builder(this, "RLA_Healthy")
                .setContentText("Kamar $room membutuhkan bantuan!")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val compat = NotificationManagerCompat.from(this)
            compat.notify(room, builder)
        }
    }

    private fun getPatient(searchText: String?) {
        arrayPatientSearch.clear()
        val search = searchText!!.lowercase()
        if(search.isNotEmpty()) {
            arrayPatient.forEach {
                if(it.name!!.lowercase().contains(search)) {
                    arrayPatientSearch.add(it)
                }
            }
            val patientAdapter = PatientAdapter(arrayPatientSearch)
            rvPatient.adapter = patientAdapter
            patientAdapter.setOnItemClickCallback(object: PatientAdapter.OnItemClickCallback {
                override fun onItemClicked(list: PatientData) {
                    Intent(this@MainActivity, PatientDetailsActivity::class.java).also {
                        getPatientDetails(list)
                    }
                }
            })
        } else {
            val patientAdapter = PatientAdapter(arrayPatient)
            rvPatient.adapter = patientAdapter
            patientAdapter.setOnItemClickCallback(object: PatientAdapter.OnItemClickCallback {
                override fun onItemClicked(list: PatientData) {
                    Intent(this@MainActivity, PatientDetailsActivity::class.java).also {
                        getPatientDetails(list)
                    }
                }
            })
        }
    }

    private fun getPatientEmergencyDetails(patient: PatientEmergencyData) {
        Intent(this@MainActivity, PatientEmergencyDetailsActivity::class.java).also {
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_LOGIN, login)
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_NUMBER, patient.number.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_ROOM, patient.room.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_HEART, patient.heart.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_TEMPERATURE, patient.temperature)
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_GLUCOSE, patient.glucose)
            startActivity(it)
        }
    }

    private fun getPatientDetails(patient: PatientData) {
        Intent(this@MainActivity, PatientDetailsActivity::class.java).also {
            it.putExtra(PatientDetailsActivity.EXTRA_LOGIN, login)
            it.putExtra(PatientDetailsActivity.EXTRA_NUMBER, patient.number.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_IMAGE, patient.image.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_NAME, patient.name.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_GENDER, patient.gender.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_AGE, patient.age.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_HANDPHONE, patient.handphone.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_HEART, patient.heart.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_TEMPERATURE, patient.temperature.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_GLUCOSE, patient.glucose.toString())
            startActivity(it)
        }
    }
}