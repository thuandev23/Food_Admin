package com.example.food_adminwave.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_adminwave.databinding.ItemAllItemBinding
import com.example.food_adminwave.model.AllItemMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllItemMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position :Int) -> Unit
):RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantities = IntArray(menuList.size) { 1 }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuItemAdapter.AddItemViewHolder {
        val binding = ItemAllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuItemAdapter.AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemAllItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)
                cartFoodName.text = menuItem.foodName
                cartPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(imageFoodCart)
                countItemCart.text = quantity.toString()

                btnMinusCart.setOnClickListener {
                    onDeleteClickListener(position)
                }

                btnPlusCart.setOnClickListener {
                    increaseQuantity(position)
                }

                btnDeleteTrashCart.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun deleteItem(itemPosition: Int) {
            menuList.removeAt(itemPosition)
            menuList.removeAt(itemPosition)
            menuList.removeAt(itemPosition)

            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, menuList.size)
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position]<10){
                itemQuantities[position]++
                binding.countItemCart.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if(itemQuantities[position] >1){
                itemQuantities[position]--
                binding.countItemCart.text = itemQuantities[position].toString()
            }
        }

    }
}
