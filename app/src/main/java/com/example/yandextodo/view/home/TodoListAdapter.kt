package com.example.yandextodo.view.home

import android.content.res.ColorStateList
import android.graphics.Paint
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
import com.example.yandextodo.data.Priority
import com.example.yandextodo.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoListAdapter(
    private val viewModel: HomeViewModel,
) : ListAdapter<Model, TodoListAdapter.TodoViewHolder>(TODO_ITEM_COMPARATOR) {


    var onItemClicked: ((Model) -> Unit)? = null
    inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)
        private val checkBox = binding.checkboxCompleted
        fun bind(data: Model) {
            binding.textDescription.text = if (data.priority == Priority.IMPORTANT) {
                "‼ ${data.description}"
            } else {
                data.description
            }
            binding.priority.text = when (data.priority) {
                Priority.IMPORTANT -> {
                    "‼"
                }
                Priority.LOW -> {
                    "↓"
                }
                else -> ""
            }
            binding.priority.setTextColor(ContextCompat.getColor(itemView.context, if (data.priority == Priority.IMPORTANT) R.color.DarkRed else R.color.LightSeparator))

            binding.textDescription.text = data.description
            binding.textDate.text = data.deadline?.let { convertTimestampToDate(it) }
            checkBox.setOnCheckedChangeListener(null)
            val isChecked = data.flag

            checkBox.isChecked = isChecked

            // Change checkbox color based on priority and checkbox state
            val priority = data.priority
            val checkboxColor = when {
                isChecked && priority == Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                priority == Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
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
                    viewModel.offCheckboxState(layoutPosition)
                    binding.textDescription.setStrikeThrough(flag)
                } else {
                    viewModel.setCheckboxState(layoutPosition)
                    binding.textDescription.setStrikeThrough(flag)
                }


                val newCheckboxColor = when {
                    flag && priority ==  Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                    priority ==  Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
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

                if (priority == Priority.IMPORTANT) {
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
        paintFlags = if (strikethrough) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
fun convertTimestampToDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}


