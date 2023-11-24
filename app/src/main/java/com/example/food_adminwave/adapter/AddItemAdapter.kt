package com.example.food_adminwave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food_adminwave.databinding.ItemAllItemBinding

class AddItemAdapter(private val menuItemName:ArrayList<String>, private val menuItemPrice:ArrayList<String>, private val menuItemImage:ArrayList<Int>):RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {
    private val itemQuantities = IntArray(menuItemName.size) { 1 }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddItemAdapter.AddItemViewHolder {
        val binding = ItemAllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemAdapter.AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItemName.size
    inner class AddItemViewHolder(private val binding: ItemAllItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                binding.cartFoodName.text = menuItemName[position]
                binding.cartPrice.text = menuItemPrice[position]
                binding.imageFoodCart.setImageResource(menuItemImage[position])

                countItemCart.text = quantity.toString()

                btnMinusCart.setOnClickListener {
                    decreaseQuantity(position)
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
            menuItemName.removeAt(itemPosition)
            menuItemPrice.removeAt(itemPosition)
            menuItemImage.removeAt(itemPosition)

            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, menuItemName.size)
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
