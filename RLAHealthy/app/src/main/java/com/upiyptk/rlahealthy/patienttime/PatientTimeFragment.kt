package com.upiyptk.rlahealthy.patienttime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.upiyptk.rlahealthy.R

class PatientTimeFragment: Fragment() {
    private lateinit var rvPatientTime: RecyclerView
    private lateinit var ref: DatabaseReference
    private var arrayPatientTime: ArrayList<PatientTimeData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_patient_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPatientTime = view.findViewById(R.id.rv_patient_time)
        rvPatientTime.layoutManager = LinearLayoutManager(requireActivity())
        rvPatientTime.setHasFixedSize(true)
        ref = FirebaseDatabase.getInstance().reference

        val patientNumber = requireActivity().intent.getStringExtra("extra_number")

        ref.child("patientTime").orderByChild("number").equalTo(patientNumber!!.toDouble())
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayPatientTime.clear()
                    if(snapshot.exists()) {
                        for(patient in snapshot.children) {
                            val patientValue = patient.getValue(PatientTimeData::class.java)
                            if(patientValue != null) {
                                arrayPatientTime.add(patientValue)
                            }
                        }
                        val patientAdapter = PatientTimeAdapter(arrayPatientTime)
                        rvPatientTime.adapter = patientAdapter
                        patientAdapter.setOnItemClickCallback(object: PatientTimeAdapter.OnItemClickCallback {
                            override fun onItemClicked(list: PatientTimeData) {
                                val action = PatientTimeFragmentDirections.actionPatientTimeFragmentToPatientTimeDetailsFragment(list)
                                findNavController().navigate(action)
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show()
                }
            })
    }
}