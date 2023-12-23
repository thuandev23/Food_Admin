package com.example.food_adminwave.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.food_adminwave.DetailsItemMenuActivity
import com.example.food_adminwave.DetailsItemVoucherActivity
import com.example.food_adminwave.databinding.ActivityAddItemVoucherBinding
import com.example.food_adminwave.databinding.ActivityAllVoucherBinding
import com.example.food_adminwave.databinding.DiscountItemBinding
import com.example.food_adminwave.model.AllVoucher
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VoucherAdapter(
    private val context: Context,
    private val voucherList: ArrayList<AllVoucher>,
    private val databaseReference: DatabaseReference,
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
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position!= RecyclerView.NO_POSITION){
                    openDetailsItemActivity(position)
                }
            }
        }

        private fun openDetailsItemActivity(position: Int) {
            val voucherItem = voucherList[position]
            val intent= Intent(context, DetailsItemVoucherActivity::class.java).apply {
                putExtra("voucherItemID", voucherItem.id)
                putExtra("voucherItemCode", voucherItem.code)
                putExtra("voucherItemDescription", voucherItem.description)
                putExtra("voucherItemDiscountAmount", voucherItem.discountAmount)
                putExtra("voucherItemDiscountPercent", voucherItem.discountPercent)
                putExtra("voucherItemExpiryDate", voucherItem.expiryDate)
                putExtra("voucherItemMinPurchaseAmount", voucherItem.minPurchaseAmount)
                putExtra("voucherItemMaxDiscount", voucherItem.maxDiscount)
            }
            context.startActivity(intent)
        }
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
            val voucherIdToDelete = voucherList[itemPosition].id
            val voucherRef = FirebaseDatabase.getInstance().getReference("voucher")
            if (voucherIdToDelete != null) {
                voucherRef.child(voucherIdToDelete).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Voucher deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to delete voucher: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            // Remove the item from the list
            voucherList.removeAt(itemPosition)
            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, voucherList.size)
        }
    }

}