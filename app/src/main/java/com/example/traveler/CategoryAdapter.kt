package com.example.traveler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class CategoryAdapter(
    private val categories: List<String>,
    private val images: List<Int>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position],images[position], position == selectedPosition)
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
        private val imageView: ImageView = itemView.findViewById(R.id.category_image)
        fun bind(category: String, imageRes: Int, isSelected: Boolean) {
            textView.text = category
            imageView.setImageResource(imageRes)

            if (isSelected) {
                itemView.setBackgroundColor(Color.LTGRAY)
                textView.setTextColor(Color.BLUE)
                itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start() // Scale up
            } else {
                itemView.setBackgroundColor(Color.WHITE)
                textView.setTextColor(Color.BLACK)
                itemView.animate().scaleX(1f).scaleY(1f).setDuration(200).start() // Scale down
            }
        }
    }
}
