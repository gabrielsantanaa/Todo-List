package com.gabriel.personal.projects.todolist.data.source.local

import androidx.paging.PagingSource
import com.gabriel.personal.projects.todolist.data.db.entity.Task

interface TaskDataSource {

    suspend fun insert(task: Task)

    fun getAll(): PagingSource<Int, Task>

    fun getByCompleteState(isComplete: Boolean): PagingSource<Int, Task>

    fun searchByName(query: String): PagingSource<Int, Task>

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun getById(taskId: Long): Task?

    suspend fun updateComplete(taskId: Long, isComplete: Boolean)

    suspend fun clear()

    suspend fun clearCompletedTasks()
}