package com.example.food_adminwave.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_adminwave.databinding.PendingordersItemBinding

class PendingItemAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: OnItemClicked,
) : RecyclerView.Adapter<PendingItemAdapter.PendingItemViewHolder>() {

    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingItemAdapter.PendingItemViewHolder {
        val binding =
            PendingordersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingItemAdapter.PendingItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size
    inner class PendingItemViewHolder(private val binding: PendingordersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccept = false
        fun bind(position: Int) {
            binding.apply {
                nameFoodPending.text = customerNames[position]
                priceFoodPending.text = quantity[position]
                var uri = Uri.parse(foodImage[position])
                Glide.with(context).load(uri).into(imageFoodPending)
                btnAcceptOrNot.apply {
                    if (!isAccept) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccept) {
                            text = "Dispatch"
                            isAccept = true
                            showToast("Orders is accepted !")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched !")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener{
                    itemClicked.onItemClickListener(position)
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}