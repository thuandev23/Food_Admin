package com.example.food_adminwave.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_adminwave.databinding.ItemAllItemBinding
import com.example.food_adminwave.model.AllItemMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                cartFoodName.text = menuItem.foodName
                cartPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(imageFoodCart)
                btnDeleteTrashCart.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun deleteItem(position: Int) {
            getUniqueKeyPosition(position) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }
        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null) {
                menuItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    if (position >= 0 && position < menuList.size) {
                        menuList.removeAt(position)
                        Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()

                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, menuList.size)

                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


        private fun getUniqueKeyPosition(position: Int, onComplete: (String?) -> Unit) {
            menuItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == position) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}
