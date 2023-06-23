package com.upiyptk.rlahealthy.patient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.upiyptk.rlahealthy.R

class PatientAdapter(private val list: ArrayList<PatientData>): RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class PatientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivPatientImage: ImageView = itemView.findViewById(R.id.iv_patient_image)
        val tvPatientName: TextView = itemView.findViewById(R.id.tv_patient_name)
        val pbPatientHeart: ProgressBar = itemView.findViewById(R.id.pb_patient_heart)
        val pbPatientTemperature: ProgressBar = itemView.findViewById(R.id.pb_patient_temperature)
        val pbPatientGlucose: ProgressBar = itemView.findViewById(R.id.pb_patient_glucose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        list.sortBy { it.number }
        val positionList = list[position]

        val patientImage = when(positionList.image) {
            1 -> R.drawable.patient_three
            2 -> R.drawable.patient_four
            3 -> R.drawable.patient_six
            4 -> R.drawable.patient_eight
            5 -> R.drawable.patient_one
            6 -> R.drawable.patient_two
            7 -> R.drawable.patient_five
            8 -> R.drawable.patient_seven
            9 -> R.drawable.patient_nine
            else -> when(positionList.gender == 0) {
                true -> R.drawable.patient_three
                false -> R.drawable.patient_one
            }
        }
        val patientName = positionList.name
        val patientHeart = positionList.heart
        val patientTemperature = positionList.temperature
        val patientGlucose = positionList.glucose

        Glide.with(holder.itemView.context)
            .load(patientImage)
            .into(holder.ivPatientImage)
        holder.tvPatientName.text = "${position+1}. $patientName"
        holder.pbPatientHeart.progress = patientHeart!!
        holder.pbPatientTemperature.progress = patientTemperature!!
        holder.pbPatientGlucose.progress = patientGlucose!!

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(list[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(list: PatientData)
    }
}