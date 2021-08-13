package com.gabriel.personal.projects.todolist.ui.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.data.source.repository.TaskRepository
import com.gabriel.personal.projects.todolist.enums.TaskFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    sealed class Event {

        object NavigateToAddTask : Event()

        class NavigateToEditTask(val taskId: String) : Event()

        class ShowSnackbar(val task: Task) : Event()

    }

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventChannel = _eventChannel.receiveAsFlow()

    //search task in database
    fun onQueryChanged(query: String) {
        _currentTaskFilterType.value = TaskFilterType.SEARCH(query)
    }

    //Current tasks filter
    private val _currentTaskFilterType = MutableStateFlow<TaskFilterType>(TaskFilterType.ALL)
    val currentTaskFilterType: StateFlow<TaskFilterType> = _currentTaskFilterType

    //filtered tasks exposed to fragment
    @ExperimentalCoroutinesApi
    val tasks: Flow<PagingData<Task>> = _currentTaskFilterType.flatMapLatest { filter ->
        when (filter) {
            is TaskFilterType.ALL -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getAll()
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.COMPLETED -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getByCompleteState(true)
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.UNCOMPLETED -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.getByCompleteState(false)
                }.flow.cachedIn(viewModelScope)
            }
            is TaskFilterType.SEARCH -> {
                Pager(
                    config = PagingConfig(pageSize = 32, maxSize = 256)
                ) {
                    repository.searchByName("%${filter.query}%")
                }.flow.cachedIn(viewModelScope)
            }
        }
    }


    fun updateTaskFilterType(taskFilterType: TaskFilterType) {
        _currentTaskFilterType.value = taskFilterType
    }

    /*
    operations in database
     */

    fun deleteTask(task: Task) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }


    fun completeTask(task: Task, isComplete: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateComplete(task.taskId, isComplete)
        }


    fun clearAll() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.clear()
        }


    fun clearCompletedTasks() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearCompletedTasks()
        }


    /*
    handling events and button click
     */

    fun navigateToAddTask() = viewModelScope.launch {
        _eventChannel.send(Event.NavigateToAddTask)
    }

    fun navigateToEditTask(task: Task) = viewModelScope.launch {
        _eventChannel.send(
            Event.NavigateToEditTask(task.taskId.toString())
        )
    }

    fun showSnackbar(task: Task) = viewModelScope.launch {
        _eventChannel.send(Event.ShowSnackbar(task))
    }

}

