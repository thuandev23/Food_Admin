package com.example.food_adminwave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.food_adminwave.databinding.PendingordersItemBinding

class PendingItemAdapter(private val customerNames: ArrayList<String>, private val quantityItem:ArrayList<String>, private val imagePending:ArrayList<Int>,private val context: Context):RecyclerView.Adapter<PendingItemAdapter.PendingItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingItemAdapter.PendingItemViewHolder {
        val binding = PendingordersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingItemAdapter.PendingItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size
    inner class PendingItemViewHolder(private val binding: PendingordersItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var isAccept = false
        fun bind(position: Int) {
            binding.apply {
                binding.nameFoodPending.text = customerNames[position]
                binding.priceFoodPending.text = quantityItem[position]
                binding.imageFoodPending.setImageResource(imagePending[position])

                btnAcceptOrNot.apply {
                    if(!isAccept){
                        text = "Accept"
                    }
                    else{
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccept){
                            text = "Dispatch"
                            isAccept = true
                            showToast("Orders is accepted !")
                        }
                        else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched !")
                        }
                    }
                }
            }
        }
        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}