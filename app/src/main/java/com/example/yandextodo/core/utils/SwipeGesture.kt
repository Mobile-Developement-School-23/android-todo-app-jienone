package com.example.yandextodo.core.utils

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.yandextodo.R
import com.example.yandextodo.view.home.HomeViewModel
import com.example.yandextodo.view.home.adapter.TodoListAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeGesture(
    context: Context,
    private val viewModel: HomeViewModel,
    private val adapter: TodoListAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteColor = ContextCompat.getColor(context, R.color.DarkRed)
    private val doneColor = ContextCompat.getColor(context, R.color.DarkGreen)

    private val deleteIcon = R.drawable.ic_trash
    private val doneIcon = R.drawable.ic_done

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        val item = adapter.currentList.getOrNull(position)

        when (direction) {
            ItemTouchHelper.LEFT -> {
                item?.let {
                    viewModel.deleteTodo(it)
                    adapter.notifyItemRemoved(position)
                }
            }

            ItemTouchHelper.RIGHT -> {
                item?.let { todo ->
                    if (todo.flag) {
                        viewModel.offCheckboxState(position)
                    } else {
                        viewModel.setCheckboxState(position)
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightBackgroundColor(doneColor)
            .addSwipeRightActionIcon(doneIcon)
            .create()
            .decorate()

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}
