package com.upiyptk.rlahealthy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewPatientAdapter(private val list: ArrayList<NewPatientData>): RecyclerView.Adapter<NewPatientAdapter.NewPatientViewHolder>() {
    inner class NewPatientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val patientNumber: TextView = itemView.findViewById(R.id.tv_patient_number)
        val patientHeart: TextView = itemView.findViewById(R.id.val_patient_heart)
        val patientTemperature: TextView = itemView.findViewById(R.id.val_patient_temperature)
        val patientGlucose: TextView = itemView.findViewById(R.id.val_patient_glucose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewPatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient_new, parent, false)
        return NewPatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewPatientViewHolder, position: Int) {
        val positionList = list[position]

        val number = "#${position+1}"
        val heart = positionList.heart
        val temperature = positionList.temperature
        val glucose = positionList.glucose

        holder.patientNumber.text = number
        holder.patientHeart.text = heart.toString()
        holder.patientTemperature.text = temperature.toString()
        holder.patientGlucose.text = glucose.toString()
    }

    override fun getItemCount(): Int = list.size
}