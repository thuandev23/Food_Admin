package com.example.food_adminwave.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_adminwave.databinding.ActivityAddItemVoucherBinding
import com.example.food_adminwave.databinding.ActivityAllVoucherBinding
import com.example.food_adminwave.databinding.DiscountItemBinding
import com.example.food_adminwave.model.AllVoucher
import com.google.firebase.database.DatabaseReference

class VoucherAdapter(
    private val context: Context,
    private val voucherList: ArrayList<AllVoucher>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<VoucherAdapter.AddVoucherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddVoucherViewHolder {
        val binding =
            DiscountItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddVoucherViewHolder(binding)
    }

    override fun getItemCount(): Int = voucherList.size

    override fun onBindViewHolder(holder: AddVoucherViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddVoucherViewHolder(private val binding: DiscountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val voucherItem = voucherList[position]
                idVoucher.text = voucherItem.id
                code.text = voucherItem.code
                description.text = voucherItem.description
                expiryDate.text = voucherItem.expiryDate

                btnDeleteVoucher.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItemVoucher(itemPosition)
                    }
                }
            }
        }

        private fun deleteItemVoucher(itemPosition: Int) {
            voucherList.removeAt(itemPosition)
            voucherList.removeAt(itemPosition)
            voucherList.removeAt(itemPosition)

            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, voucherList.size)
        }

    }

}