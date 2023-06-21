package com.upiyptk.rlahealthy.patientemergency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upiyptk.rlahealthy.R

class PatientEmergencyAdapter(private val list: ArrayList<PatientEmergencyData>): RecyclerView.Adapter<PatientEmergencyAdapter.PatientEmergencyViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class PatientEmergencyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvPatientNumber: TextView = itemView.findViewById(R.id.tv_patient_number)
        val tvPatientHeart: TextView = itemView.findViewById(R.id.tv_patient_heart)
        val tvPatientTemperature: TextView = itemView.findViewById(R.id.tv_patient_temperature)
        val tvPatientGlucose: TextView = itemView.findViewById(R.id.tv_patient_glucose)
        val tvPatientNew: TextView = itemView.findViewById(R.id.tv_patient_new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientEmergencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient_emergency, parent, false)
        return PatientEmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientEmergencyViewHolder, position: Int) {
        list.sortWith(compareBy({ -it.new!! }, { it.number }))
        val positionList = list[position]

        val patientNumber = "#${position+1}"
        val patientHeart = positionList.heart
        val patientTemperature = positionList.temperature
        val patientGlucose = positionList.glucose

        holder.tvPatientNumber.text = patientNumber
        holder.tvPatientHeart.text = patientHeart.toString()
        holder.tvPatientTemperature.text = patientTemperature.toString()
        holder.tvPatientGlucose.text = patientGlucose.toString()
        if(positionList.new == 1) {
            holder.tvPatientNew.visibility = View.VISIBLE
        } else {
            holder.tvPatientNew.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(list[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(list: PatientEmergencyData)
    }
}