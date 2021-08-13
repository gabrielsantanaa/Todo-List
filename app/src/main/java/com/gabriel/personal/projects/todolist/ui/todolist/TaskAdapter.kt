package com.gabriel.personal.projects.todolist.ui.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.databinding.AdapterTaskBinding

class TaskAdapter(private val viewModel: ToDoListViewModel) :
    PagingDataAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    class ViewHolder private constructor(private val binding: AdapterTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task?, viewModel: ToDoListViewModel) {
            task?.let {
                binding.task = task
                binding.viewModel = viewModel
                binding.executePendingBindings()

            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                return ViewHolder(
                    AdapterTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}