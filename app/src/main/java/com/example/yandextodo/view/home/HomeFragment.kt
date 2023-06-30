package com.example.yandextodo.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.example.yandextodo.core.utils.SwipeGesture
import com.example.yandextodo.core.vo.LoadResult
import com.example.yandextodo.data.Model
import com.example.yandextodo.data.TodoDatabase
import com.example.yandextodo.data.TodoLocalDataSource
import com.example.yandextodo.data.TodoRepository
import com.example.yandextodo.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var todoRepository: TodoRepository

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        Injection.provideViewModelFactory(requireContext())
    }
    private lateinit var todoListAdapter: TodoListAdapter

    private var tvCompleted: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoRepository = TodoRepository.getInstance(
            TodoLocalDataSource.getInstance(
                TodoDatabase.getInstance(requireContext()).getTodoDao(),
                Dispatchers.IO
            ),
            Dispatchers.IO
        )


        tvCompleted = binding.tvCompleted

        var isVisible = true
        val eye = view.findViewById<ImageView>(R.id.iv_eye)
        eye?.setOnClickListener {
            isVisible = !isVisible
            if(isVisible) eye.setImageResource(R.drawable.ic_eye_visible)
            else eye.setImageResource(R.drawable.ic_eye_invisible)
        }

        binding.fabAdd.setOnClickListener(this)

        viewModel.getAllTodos()

        todoListAdapter = TodoListAdapter(viewModel)
        todoListAdapter.onItemClicked = { todo ->
            goToDetailFragment(todo)
        }

        binding.rvTodos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTodos.adapter = todoListAdapter

        val swipeGesture = object : SwipeGesture(requireContext(), viewModel, adapter = todoListAdapter) {}
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.rvTodos)

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
                            updateCounter()
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
    private suspend fun updateCounter() {
        val count = viewModel.countElementsWithProperty()
        tvCompleted!!.text = getString(R.string.counter_text, count)
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
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showEmptyListState(state: Boolean) {
        binding.tvEmptyState.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

