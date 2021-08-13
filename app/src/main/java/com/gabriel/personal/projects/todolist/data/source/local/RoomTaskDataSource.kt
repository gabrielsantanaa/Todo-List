package com.gabriel.personal.projects.todolist.data.source.local

import androidx.paging.PagingSource
import com.gabriel.personal.projects.todolist.data.db.TaskDatabaseDAO
import com.gabriel.personal.projects.todolist.data.db.entity.Task

class RoomTaskDataSource(private val databaseDao: TaskDatabaseDAO) : TaskDataSource {

    override suspend fun insert(task: Task) =
        databaseDao.insert(task)

    override fun getAll(): PagingSource<Int, Task> =
        databaseDao.getAll()

    override fun getByCompleteState(isComplete: Boolean): PagingSource<Int, Task> =
        databaseDao.getByCompleteState(isComplete)

    override fun searchByName(query: String): PagingSource<Int, Task> =
        databaseDao.searchByName(query)

    override suspend fun deleteTask(task: Task) =
        databaseDao.deleteTask(task)

    override suspend fun updateTask(task: Task) =
        databaseDao.updateTask(task)

    override suspend fun getById(taskId: Long): Task? =
        databaseDao.getById(taskId)

    override suspend fun updateComplete(taskId: Long, isComplete: Boolean) =
        databaseDao.updateComplete(taskId, isComplete)

    override suspend fun clear() =
        databaseDao.clear()

    override suspend fun clearCompletedTasks() =
        databaseDao.clearCompletedTasks()


}