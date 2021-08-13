package com.gabriel.personal.projects.todolist.ui.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.personal.projects.todolist.R
import com.gabriel.personal.projects.todolist.databinding.FragmentToDoListBinding
import com.gabriel.personal.projects.todolist.enums.TaskFilterType
import com.gabriel.personal.projects.todolist.util.observeOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ToDoListFragment : Fragment() {

    private val toDoListViewModel by viewModels<ToDoListViewModel>()

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)

        binding.apply {
            toDoListViewModel = this@ToDoListFragment.toDoListViewModel
            lifecycleOwner = this@ToDoListFragment
        }

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerAndAdapter()
        setupEvents()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setOnMenuItemClickListener { item -> onMenuItemClick(item) }
            setupOptionsToolbar(this.menu)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerAndAdapter() {

        val adapter = TaskAdapter(toDoListViewModel)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.fab.visibility == VISIBLE) {
                    binding.fab.hide()
                } else if (dy < 0 && binding.fab.visibility != VISIBLE) {
                    binding.fab.show()
                }
            }
        })

        lifecycleScope.launch {
            toDoListViewModel.tasks.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun setupEvents() {

        toDoListViewModel.eventChannel.observeOnLifecycle(viewLifecycleOwner) { event ->

            when (event) {

                ToDoListViewModel.Event.NavigateToAddTask -> {
                    findNavController().navigate(ToDoListFragmentDirections.actionToDoListToAddTask(null))
                }
                is ToDoListViewModel.Event.NavigateToEditTask -> {
                    findNavController().navigate(ToDoListFragmentDirections.actionToDoListToAddTask(event.taskId))
                }
                is ToDoListViewModel.Event.ShowSnackbar -> {
                    Snackbar.make(binding.fab, R.string.delete_task_message, Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                        .setAction(R.string.delete) {
                            toDoListViewModel.deleteTask(event.task)
                        }.show()
                }

            }

        }

    }

    private fun setupOptionsToolbar(menu: Menu) {

        val searchView = menu.findItem(R.id.ic_action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    toDoListViewModel.onQueryChanged(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        lifecycleScope.launch {
            toDoListViewModel.currentTaskFilterType.collectLatest { filter ->
                binding.toolbar.title = filter.getToolbarTitle(requireContext())
                menu.findItem(filter.menuId).isChecked = true
            }
        }

    }

    private fun MenuItem.setupFilter(taskFilterType: TaskFilterType) {
        //fix for permanently hidden FAB
        binding.fab.show()
        this.isChecked = true
        toDoListViewModel.updateTaskFilterType(taskFilterType)
    }


    private fun onMenuItemClick(item: MenuItem): Boolean {

        if (item.itemId == toDoListViewModel.currentTaskFilterType.value.menuId) return true

        return when (item.itemId) {
            R.id.ic_filter_all_tasks -> {
                item.setupFilter(TaskFilterType.ALL)
                true
            }
            R.id.ic_filter_completed_tasks -> {
                item.setupFilter(TaskFilterType.COMPLETED)
                true
            }
            R.id.ic_filter_uncompleted_tasks -> {
                item.setupFilter(TaskFilterType.UNCOMPLETED)
                true
            }
            R.id.ic_clear_all_tasks -> {
                Snackbar.make(binding.fab, R.string.warning_delete_all_tasks, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete) {
                        toDoListViewModel.clearAll()
                    }.show()
                true
            }
            R.id.ic_clear_completed_tasks -> {
                Snackbar.make(binding.fab, R.string.warning_delete_completed_tasks, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete) {
                        toDoListViewModel.clearCompletedTasks()
                    }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}