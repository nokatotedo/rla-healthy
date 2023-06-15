package com.upiyptk.rlahealthy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity: AppCompatActivity() {
    private lateinit var ref: DatabaseReference
    private lateinit var rvNew: RecyclerView
    private var arrayNew: ArrayList<NewPatientData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvNew = findViewById(R.id.rv_patient_new)
        rvNew.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvNew.setHasFixedSize(true)

        ref = FirebaseDatabase.getInstance().getReference("newPatient")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayNew.clear()
                if(snapshot.exists()) {
                    for(patient in snapshot.children) {
                        val patientValue = patient.getValue(NewPatientData::class.java)
                        if(patientValue != null) {
                            arrayNew.add(patientValue)
                        }
                    }
                    val adapter = NewPatientAdapter(arrayNew)
                    rvNew.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }
}