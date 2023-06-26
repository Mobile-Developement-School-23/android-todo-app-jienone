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
    private val viewmodel: HomeViewModel,
    private val homeFragment: HomeFragment
) : ListAdapter<Model, TodoListAdapter.TodoViewHolder>(TODO_ITEM_COMPARATOR) {


    var onItemClicked: ((Model) -> Unit)? = null



    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)


        var cbCounter = 0
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
            binding.textDate.text = data.deadline
            Log.d("DATAAAAAAAAAAAA", data.deadline)


            checkBox.setOnCheckedChangeListener(null) // Remove previous listener to avoid recursion

            // Retrieve the checkbox state from the ViewModel
            val isChecked = viewmodel.getCheckboxState(data.id.toInt())

            // Update the checkbox state without triggering the listener
            checkBox.isChecked = isChecked

            // Change checkbox color based on priority and checkbox state
            val priority = data.priority
            val checkboxColor = when {
                isChecked && priority == "!!Высокий" -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                priority == "!!Высокий" -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
                else -> {
                    if (isChecked) {
                        ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    } else {
                        ContextCompat.getColor(itemView.context, R.color.LightGray)
                    }
                }
            }
            checkBox.buttonTintList = ColorStateList.valueOf(checkboxColor)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Update the checkbox state in the ViewModel
                viewmodel.setCheckboxState(data.id.toInt(), isChecked)
                binding.textDescription.setStrikeThrough(isChecked)
                data.flag = isChecked


                // Change checkbox color dynamically based on priority and checkbox state
                val newCheckboxColor = when {
                    isChecked && priority == "!!Высокий" -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    priority == "!!Высокий" -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
                    else -> {
                        if (isChecked) {
                            ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                        } else {
                            ContextCompat.getColor(itemView.context, R.color.LightGray)
                        }
                    }
                }
                checkBox.buttonTintList = ColorStateList.valueOf(newCheckboxColor)
            }

            // Set the strike-through based on the checkbox state
            binding.textDescription.setStrikeThrough(isChecked)

            binding.root.setOnClickListener {
                onItemClicked?.invoke(data)

                // Change checkbox color when item is clicked and priority is "!!Высокий"
                if (priority == "!!Высокий") {
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

