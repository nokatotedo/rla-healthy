package com.upiyptk.rlahealthy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity: AppCompatActivity() {
    private lateinit var rvPatientEmergency: RecyclerView
    private lateinit var rvPatient: RecyclerView
    private lateinit var ref: DatabaseReference
    private var arrayPatientEmergency: ArrayList<PatientEmergencyData> = arrayListOf()
    private var arrayPatient: ArrayList<PatientData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPatientEmergency = findViewById(R.id.rv_patient_emergency)
        rvPatientEmergency.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPatientEmergency.setHasFixedSize(true)
        rvPatient = findViewById(R.id.rv_patient)
        rvPatient.layoutManager = GridLayoutManager(this, 2)
        rvPatient.setHasFixedSize(true)

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
                                Intent(this@MainActivity, PatientDetailsActivity::class.java).also {
                                    getPatientDetails(list)
                                }
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getPatientEmergencyDetails(patient: PatientEmergencyData) {
        Intent(this@MainActivity, PatientEmergencyDetailsActivity::class.java).also {
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_HEART, patient.heart.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_TEMPERATURE, patient.temperature.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_GLUCOSE, patient.glucose.toString())
            it.putExtra(PatientEmergencyDetailsActivity.EXTRA_NUMBER, patient.number.toString())
            startActivity(it)
        }
    }

    private fun getPatientDetails(patient: PatientData) {
        Intent(this@MainActivity, PatientDetailsActivity::class.java).also {
            it.putExtra(PatientDetailsActivity.EXTRA_IMAGE, patient.image.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_NAME, patient.name.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_GENDER, patient.gender.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_AGE, patient.age.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_HEART, patient.heart.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_TEMPERATURE, patient.temperature.toString())
            it.putExtra(PatientDetailsActivity.EXTRA_GLUCOSE, patient.glucose.toString())
            startActivity(it)
        }
    }
}