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
    private val onItemClicked: ((Model) -> Unit)? = null
) : ListAdapter<Model, TodoListAdapter.TodoViewHolder>(TODO_ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(
        view: View,
        private val onItemClicked: ((Model) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTodoBinding.bind(view)
        private val checkBox = binding.checkboxCompleted

        init {
            setupCheckboxListener()
            binding.root.setOnClickListener { onItemClicked?.invoke(getItem(layoutPosition)) }
        }

        fun bind(data: Model) {
            val priority = data.priority
            val isChecked = data.flag

            binding.textDescription.text = if (priority == Priority.IMPORTANT) {
                "‼ ${data.description}"
            } else {
                data.description
            }

            binding.priority.text = when (priority) {
                Priority.IMPORTANT -> "‼"
                Priority.LOW -> "↓"
                else -> ""
            }

            binding.priority.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (priority == Priority.IMPORTANT) R.color.DarkRed else R.color.LightSeparator
                )
            )
            binding.textDescription.text = data.description
            binding.textDate.text = data.deadline?.let { convertTimestampToDate(it) }

            updateCheckboxState(isChecked, priority)
            binding.textDescription.setStrikeThrough(isChecked)
        }

        private fun setupCheckboxListener() {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val data = getItem(layoutPosition)
                val priority = data.priority
                updateCheckboxState(isChecked, priority)
                binding.textDescription.setStrikeThrough(isChecked)
                if (isChecked) {
                    viewModel.setCheckboxState(layoutPosition)
                } else {
                    viewModel.offCheckboxState(layoutPosition)
                }
            }
        }

        private fun updateCheckboxState(isChecked: Boolean, priority: Priority) {
            val checkboxColor = when {
                isChecked && priority == Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                priority == Priority.IMPORTANT -> ContextCompat.getColor(itemView.context, R.color.DarkRed)
                isChecked -> ContextCompat.getColor(itemView.context, R.color.DarkGreen)
                else -> ContextCompat.getColor(itemView.context, R.color.LightGray)
            }

            checkBox.isChecked = isChecked
            checkBox.buttonTintList = ColorStateList.valueOf(checkboxColor)
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
