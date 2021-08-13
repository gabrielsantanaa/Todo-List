package com.gabriel.personal.projects.todolist.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.gabriel.personal.projects.todolist.data.db.entity.Task

@Dao
interface TaskDatabaseDAO {

    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM my_tasks_table ORDER BY taskId DESC")
    fun getAll(): PagingSource<Int, Task>

    @Query("SELECT * FROM my_tasks_table WHERE isComplete = :isComplete ORDER BY taskId DESC")
    fun getByCompleteState(isComplete: Boolean): PagingSource<Int, Task>

    @Query("SELECT * FROM my_tasks_table WHERE taskTitle LIKE :title ORDER BY taskId DESC")
    fun searchByName(title: String): PagingSource<Int, Task>

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM my_tasks_table WHERE taskId = :taskId")
    suspend fun getById(taskId: Long): Task?

    @Query("UPDATE my_tasks_table SET isComplete = :isComplete WHERE taskId = :taskId")
    suspend fun updateComplete(taskId: Long, isComplete: Boolean)

    @Query("DELETE FROM my_tasks_table")
    suspend fun clear()

    @Query("DELETE FROM my_tasks_table WHERE isComplete = 1")
    suspend fun clearCompletedTasks()

}