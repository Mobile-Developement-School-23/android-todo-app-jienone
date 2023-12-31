package com.example.yandextodo.view.detail

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yandextodo.R
import com.example.yandextodo.core.di.Injection
import com.example.yandextodo.data.Model
import com.example.yandextodo.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding


    private val viewModel by viewModels<DetailViewModel> {
        Injection.provideViewModelFactory(requireContext())
    }
    private val args by navArgs<DetailFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding?.root



        val switchButton = view?.findViewById<SwitchCompat>(R.id.switchButton)
        switchButton?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showDatePickerDialog()
            }
        }


        val spinner = view?.findViewById<Spinner>(R.id.spinner)
        val options = resources.getStringArray(R.array.options)
        val adapter = CustomSpinnerAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, options.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter
        if (spinner != null) {
            spinner.adapter = adapter
        }
        return view
    }

    private fun showDatePickerDialog() {
        if (binding?.pickedDate?.text.isNullOrBlank()) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
                    val formattedDate = dateFormat.format(selectedCalendar.time)
                    Log.d("FORMDATE", formattedDate)
                    binding?.pickedDate?.text = formattedDate.toString()

                }, year, month, day)

            datePickerDialog.show()
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_trash)
        val btnDelete = binding?.btnDelete
        val btnBack = binding?.btnBack
        val btnAddOrUpdate = binding?.btnAddOrUpdate
        val etDescription = binding?.etDescription

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkTheme = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        btnBack?.setOnClickListener(this)
        btnAddOrUpdate?.setOnClickListener(this)
        btnDelete?.setOnClickListener(this)

        val tintDisable = if (isDarkTheme) R.color.DarkDisable else R.color.LightDisable

        drawable?.mutate()?.setTint(ContextCompat.getColor(requireContext(), tintDisable))
        btnDelete?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)



        populateData()
        initViews()

        etDescription?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Не требуется
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isInputValidated = isUserInputValidated()
                val tintResId = if (isDarkTheme) {
                    if (isInputValidated) R.color.DarkRed else R.color.DarkDisable
                } else {
                    if (isInputValidated) R.color.LightRed else R.color.LightDisable
                }
                btnDelete?.isEnabled = isInputValidated
                drawable?.mutate()?.setTint(ContextCompat.getColor(requireContext(), tintResId))
                btnDelete?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                btnDelete?.setTextColor(ContextCompat.getColor(requireContext(), tintResId))
            }

            override fun afterTextChanged(s: Editable?) {
                // Не требуется
            }
        })
    }

    private fun populateData() {
        if (args.todo != null) {
            binding?.etDescription?.setText(args.todo?.description)
            //15.06
            val options = resources.getStringArray(R.array.options)

            val selectedOption = args.todo?.priority
            val position = options.indexOf(selectedOption)

            if (position != -1) {
                binding?.spinner?.setSelection(position)
            }
            if (!args.todo?.deadline.isNullOrEmpty()) {
                if (!args.todo?.deadline.isNullOrEmpty()) {
                    if (binding?.pickedDate?.text.isNullOrBlank()) {
                        binding?.pickedDate?.text = args.todo?.deadline
                    }
                    binding?.switchButton?.isChecked = true
                }

            }
            viewModel.setIsOnUpdatingTodo(true)
        } else {
            viewModel.setIsOnUpdatingTodo(false)
        }
    }
    private fun initViews() {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_trash)
        val btnDelete = binding?.btnDelete
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkTheme = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        val tintEnable = if (isDarkTheme) R.color.DarkRed else R.color.LightRed

        if (viewModel.isOnUpdatingTodo) {
            drawable?.mutate()?.setTint(ContextCompat.getColor(requireContext(), tintEnable))
            btnDelete?.setTextColor(ContextCompat.getColor(requireContext(), tintEnable))
            btnDelete?.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            binding?.btnAddOrUpdate?.text = getString(R.string.update)
        }
    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.switchButton -> {
                if (binding?.switchButton?.isChecked == true && !binding?.pickedDate?.text.isNullOrBlank()) {
                    binding?.pickedDate?.text = null
                    showDatePickerDialog()
                }
            }

            R.id.btn_back -> {
//                activity?.onBackPressed()
                findNavController().popBackStack()
            }


            R.id.btn_delete -> {
                if (!isUserInputValidated()) {
                    binding?.btnDelete?.isEnabled = false
                } else {
                    binding?.btnDelete?.isEnabled = true
                    args.todo?.let { viewModel.deleteTodo(it) }
//                    activity?.onBackPressed()
                    findNavController().popBackStack()

                }
            }

            R.id.btn_add_or_update -> {
                if (isUserInputValidated()) {

                    if (viewModel.isOnUpdatingTodo) {
                        if (isUserInputValidated()) {
                            args.todo?.let {
                                val description = binding?.etDescription?.text.toString()
                                val selectedSpinnerItem = binding?.spinner?.selectedItem.toString()
                                val deadline = binding?.pickedDate?.text.toString()
                                val todo = Model(
                                    id = it.id,
                                    description = description,
                                    priority = selectedSpinnerItem,
                                    deadline = deadline,
                                    flag = false,
                                    createdAt = it.createdAt,
                                    updatedAt = System.currentTimeMillis(),
                                )

                                viewModel.updateTodo(todo)
                            }
                        }
                    } else {


                        val description = binding?.etDescription?.text.toString()

                        val selectedSpinnerItem = binding?.spinner?.selectedItem.toString()
                        val deadline = binding?.pickedDate?.text.toString()
                        val todo = Model(
                            description = description,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis(),
                            priority = selectedSpinnerItem,
                            deadline = deadline,
                            flag = false
                        )
                        viewModel.addTodo(todo)
                    }
//                    activity?.onBackPressed()
                    findNavController().popBackStack()

                } else {
                    Toast.makeText(requireContext(), R.string.empty_et_desc, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isUserInputValidated(): Boolean {
        val description = binding?.etDescription?.text
        return (!description.isNullOrBlank())
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

