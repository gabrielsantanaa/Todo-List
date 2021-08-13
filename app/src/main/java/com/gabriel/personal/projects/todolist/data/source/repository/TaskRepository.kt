package com.gabriel.personal.projects.todolist.data.source.repository

import androidx.paging.PagingSource
import com.gabriel.personal.projects.todolist.data.db.entity.Task
import com.gabriel.personal.projects.todolist.data.source.local.TaskDataSource

class TaskRepository(private val dataSource: TaskDataSource) {

    suspend fun insert(task: Task) = dataSource.insert(task)

    fun getAll(): PagingSource<Int, Task> = dataSource.getAll()

    fun getByCompleteState(isComplete: Boolean): PagingSource<Int, Task> =
        dataSource.getByCompleteState(isComplete)

    fun searchByName(title: String): PagingSource<Int, Task> = dataSource.searchByName(title)

    suspend fun deleteTask(task: Task) = dataSource.deleteTask(task)

    suspend fun updateTask(task: Task) = dataSource.updateTask(task)

    suspend fun getById(taskId: Long): Task? = dataSource.getById(taskId)

    suspend fun updateComplete(taskId: Long, isComplete: Boolean) =
        dataSource.updateComplete(taskId, isComplete)

    suspend fun clear() = dataSource.clear()

    suspend fun clearCompletedTasks() = dataSource.clearCompletedTasks()

}