package com.example.yandextodo.view.home

import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yandextodo.R
import com.example.yandextodo.data.Model
import com.example.yandextodo.databinding.ItemTodoBinding

class TodoListAdapter(
    private val viewModel: HomeViewModel,
) : ListAdapter<Model, TodoListAdapter.TodoViewHolder>(TODO_ITEM_COMPARATOR) {

    var onItemClicked: ((Model) -> Unit)? = null
    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)
        private val checkBox = binding.checkboxCompleted
        fun bind(data: Model) {
            binding.textDescription.text = if (data.priority == "!!Высокий") {
                "‼ ${data.description}"
            } else {
                data.description
            }
            binding.priority.text = if (data.priority == "!!Высокий") {
                "‼"
            } else if(data.priority == "Низкий"){
                "↓"
            } else ""
            binding.priority.setTextColor(ContextCompat.getColor(itemView.context, if (data.priority == "!!Высокий") R.color.DarkRed else R.color.LightSeparator))

            binding.textDescription.text = data.description
            binding.textDate.text = data.deadline.toString()
            checkBox.setOnCheckedChangeListener(null) // Remove previous listener to avoid recursion

            // Retrieve the checkbox state from the ViewModel
            val isChecked = data.flag

            // Update the checkbox state without triggering the listener
            checkBox.isChecked = isChecked

            // Change checkbox color based on priority and checkbox state
            val priority = data.priority
            val checkboxColor = when {
                isChecked && priority == R.string.important.toString() -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                priority == R.string.important.toString() -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
                else -> {
                    if (isChecked) {
                        ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    } else {
                        ContextCompat.getColor(itemView.context, R.color.LightGray)
                    }
                }
            }
            checkBox.buttonTintList = ColorStateList.valueOf(checkboxColor)

            checkBox.setOnCheckedChangeListener { _, flag ->
                if (data.flag) {
                    viewModel.offCheckboxState(position)
                    binding.textDescription.setStrikeThrough(flag)
                }
                else {
                    viewModel.setCheckboxState(position)
                    binding.textDescription.setStrikeThrough(flag)
                }
                notifyItemChanged(data.id.toInt())


                val newCheckboxColor = when {
                    flag && priority ==  R.string.important.toString() -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    priority ==  R.string.important.toString() -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
                    else -> {
                        if (flag) {
                            ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                        } else {
                            ContextCompat.getColor(itemView.context, R.color.LightGray)
                        }
                    }
                }
                checkBox.buttonTintList = ColorStateList.valueOf(newCheckboxColor)
            }
            binding.textDescription.setStrikeThrough(isChecked)

            binding.root.setOnClickListener {
                onItemClicked?.invoke(data)

                if (priority == R.string.important.toString()) {
                    val newCheckboxColor = ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    checkBox.buttonTintList = ColorStateList.valueOf(newCheckboxColor)
                }
            }
        }

    }
    companion object {
        private val TODO_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                return oldItem.id == newItem.id && newItem.description == oldItem.description
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
    fun TextView.setStrikeThrough(strikethrough: Boolean) {
        if (strikethrough) {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

