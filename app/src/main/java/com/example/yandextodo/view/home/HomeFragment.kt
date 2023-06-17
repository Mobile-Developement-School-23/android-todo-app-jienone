package com.example.yandextodo.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yandextodo.R
import com.example.yandextodo.core.di.Injection
import com.example.yandextodo.core.vo.LoadResult
import com.example.yandextodo.data.Model
import com.example.yandextodo.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), View.OnClickListener {



    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding

    private val viewModel by viewModels<HomeViewModel> {
        Injection.provideViewModelFactory(requireContext())
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.fabAdd?.setOnClickListener(this)

        viewModel.getAllTodos()

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Do nothing, as we don't need to handle item move
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Call the deleteTodo method of your ViewModel with the position as an argument
                viewModel.deleteTodo(position)
            }
        })

        // Attach the ItemTouchHelper to the RecyclerView
        itemTouchHelper.attachToRecyclerView(binding?.rvTodos)



        val todoListAdapter = TodoListAdapter(viewModel)

        todoListAdapter.onItemClicked = { todo ->
            goToDetailFragment(todo)
        }

        binding?.rvTodos?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvTodos?.adapter = todoListAdapter


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.todos.collect { loadResult ->
                    when (loadResult) {
                        is LoadResult.Success -> {
                            showLoadingState(false)

                            todoListAdapter.submitList(loadResult.data)

                            if (loadResult.data.isNullOrEmpty()) {
                                showEmptyListState(true)
                            } else {
                                showEmptyListState(false)
                            }
                        }

                        is LoadResult.Loading -> {
                            showLoadingState(true)
                        }

                        is LoadResult.Error -> {
                            showEmptyListState(true)
                            showLoadingState(false)
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add -> goToDetailFragment()
        }
    }

    private fun goToDetailFragment(todo: Model? = null) {
        val action = HomeFragmentDirections.actionHomeFragment2ToDetailFragment2()
        action.todo = todo

        findNavController().navigate(action)
    }

    private fun showLoadingState(state: Boolean) {
        if (state) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showEmptyListState(state: Boolean) {
        if (state) {
            binding?.tvEmptyState?.visibility = View.VISIBLE
        } else {
            binding?.tvEmptyState?.visibility = View.GONE
        }
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}