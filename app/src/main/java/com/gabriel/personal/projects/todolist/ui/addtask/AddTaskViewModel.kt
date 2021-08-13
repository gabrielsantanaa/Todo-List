package com.gabriel.personal.projects.todolist.ui.addtask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabriel.personal.projects.todolist.R
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.data.source.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    sealed class Event {
        class NavigateToDoList(val eventMessageResource: Int) : Event()
    }

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()

    //variable used when the user is editing a task
    private val _editedTask = MutableLiveData<Task>()
    val editedTask: LiveData<Task> = _editedTask

    //two-way dataBinding
    val title = MutableLiveData<String>()

    //two-way dataBinding
    val description = MutableLiveData<String>()

    fun onSave() {
        if (!title.value.isNullOrEmpty() && !description.value.isNullOrEmpty()) {
            //if is task edition
            editedTask.value?.let { task ->
                task.apply {
                    taskTitle = title.value!!
                    taskDescription = description.value!!
                }
                updateTask(task)
                //if is task creation
            } ?: run {
                val newTask = Task(
                    taskTitle = title.value!!,
                    taskDescription = description.value!!
                )
                createTask(newTask)
            }

        }
    }

    private fun createTask(task: Task) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(task)
            _eventChannel.send(Event.NavigateToDoList(R.string.message_task_added))
        }

    fun editTask(taskId: Long) =
        viewModelScope.launch {
            _editedTask.value = withContext(Dispatchers.IO) { repository.getById(taskId) }!!
        }

    private fun updateTask(task: Task) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
            _eventChannel.send(Event.NavigateToDoList(R.string.message_task_updated))
        }


}