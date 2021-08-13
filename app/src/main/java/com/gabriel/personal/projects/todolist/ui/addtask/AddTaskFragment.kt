package com.gabriel.personal.projects.todolist.ui.addtask

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gabriel.personal.projects.todolist.R
import com.gabriel.personal.projects.todolist.databinding.FragmentAddTaskBinding
import com.gabriel.personal.projects.todolist.util.observeOnLifecycle
import com.gabriel.personal.projects.todolist.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTask : Fragment() {

    private val args: AddTaskArgs by navArgs()

    private val addTaskViewModel by viewModels<AddTaskViewModel>()

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        args.taskId?.let {
            addTaskViewModel.editTask(it.toLong())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        binding.apply {
            addTaskViewModel = this@AddTask.addTaskViewModel
            lifecycleOwner = this@AddTask
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStates()
        setupEvents()
    }

    private fun setupEvents() {
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            setOnMenuItemClickListener { item -> onMenuItemClick(item) }
        }

        addTaskViewModel.eventChannel.observeOnLifecycle(viewLifecycleOwner) { event ->

            when (event) {
                is AddTaskViewModel.Event.NavigateToDoList -> {
                    showToast(event.eventMessageResource)
                    findNavController().popBackStack()
                }
            }

        }

    }

    private fun setupStates() {
        addTaskViewModel.editedTask.observe(viewLifecycleOwner, { task ->
            addTaskViewModel.apply {
                title.value = task.taskTitle
                description.value = task.taskDescription
            }
        })
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_save -> {
                addTaskViewModel.onSave()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
