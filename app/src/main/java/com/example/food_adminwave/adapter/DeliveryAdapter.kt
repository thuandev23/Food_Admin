package com.example.food_adminwave.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_adminwave.databinding.DeliveyItemBinding

class DeliveryAdapter(private val customerNames: ArrayList<String>, private val paymentStatus:ArrayList<String>):RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int = customerNames.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class DeliveryViewHolder(private val binding: DeliveyItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                payment.text = paymentStatus[position]
                val colorMap = mapOf(
                    "received" to Color.GREEN, "notReceived" to Color.RED,  "Pending" to Color.GRAY,
                )
                payment.setTextColor(colorMap[paymentStatus[position]]?:Color.BLACK)
                cardViewDelivery.backgroundTintList = ColorStateList.valueOf(colorMap[paymentStatus[position]]?:Color.BLACK)
            }
        }

    }
}