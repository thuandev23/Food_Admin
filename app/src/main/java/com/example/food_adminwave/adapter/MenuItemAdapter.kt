package com.example.food_adminwave.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_adminwave.activities.DetailsItemMenuActivity
import com.example.food_adminwave.databinding.ItemAllItemBinding
import com.example.food_adminwave.model.AllItemMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllItemMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position :Int) -> Unit
):RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuItemAdapter.AddItemViewHolder {
        val binding = ItemAllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }
    private val auth = FirebaseAuth.getInstance()
    init {
        val database = FirebaseDatabase.getInstance()
        val menuId = auth.currentUser?.uid ?: ""
        menuItemsReference = database.reference.child("user").child(menuId)
    }

    companion object {
        private lateinit var menuItemsReference: DatabaseReference
    }
    override fun onBindViewHolder(holder: MenuItemAdapter.AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemAllItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position!= RecyclerView.NO_POSITION){
                    openDetailsItemActivity(position)
                }
            }
        }

        private fun openDetailsItemActivity(position: Int) {
            val menuItem = menuList[position]
            val intent= Intent(context, DetailsItemMenuActivity::class.java).apply {
                putExtra("menuItemId", menuItem.key)
                putExtra("menuItemName", menuItem.foodName)
                putExtra("menuItemPrice", menuItem.foodPrice)
                putExtra("menuItemDescription", menuItem.foodDescription)
                putExtra("menuItemImage", menuItem.foodImage)
                putExtra("menuItemIngredient", menuItem.foodIngredient)
            }
            context.startActivity(intent)
        }

        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                cartFoodName.text = menuItem.foodName
                cartPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(imageFoodCart)
                val itemPosition = adapterPosition
                btnDeleteTrashCart.setOnClickListener {
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun deleteItem(position: Int) {
            val menuIdToDelete = menuList[position].key
            val menuRef = FirebaseDatabase.getInstance().getReference("menu")
            if (menuIdToDelete != null) {
                menuRef.child(menuIdToDelete).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Voucher deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed to delete voucher: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            // Remove the item from the list
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }
    }
}
