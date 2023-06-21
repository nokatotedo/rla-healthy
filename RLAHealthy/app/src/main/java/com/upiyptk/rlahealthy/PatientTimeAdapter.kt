package com.upiyptk.rlahealthy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PatientTimeAdapter(private val list: ArrayList<PatientTimeData>): RecyclerView.Adapter<PatientTimeAdapter.PatientTimeViewHolder>() {
    inner class PatientTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvPatientTime: TextView = itemView.findViewById(R.id.tv_patient_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientTimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient_time, parent, false)
        return PatientTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientTimeViewHolder, position: Int) {
        list.sortWith(compareBy({ -it.year!! }, { -it.month!! }, { -it.day!! }))

        val positionList = list[position]

        val patientDay = positionList.day
        val patientMonth = when(positionList.month) {
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
        val patientYear = positionList.year

        holder.tvPatientTime.text = "$patientDay $patientMonth $patientYear"
    }

    override fun getItemCount(): Int = list.size
}