package com.example.traveler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class CategoryAdapter(
    private val categories: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position == selectedPosition)
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) {
                RecyclerView.NO_POSITION // Deselect if the same item is clicked again
            } else {
                position
            }
            notifyItemChanged(previousPosition)
            notifyItemChanged(position)
            onClick(categories[position])
        }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.category_name)

        fun bind(category: String, isSelected: Boolean) {
            textView.text = category
            // Update UI for selected state
            itemView.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.WHITE)
            textView.setTextColor(if (isSelected) Color.BLUE else Color.BLACK)
        }
    }
}
