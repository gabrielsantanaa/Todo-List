package com.gabriel.personal.projects.todolist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_tasks_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "taskTitle")
    var taskTitle: String,

    @ColumnInfo(name = "taskDescription")
    var taskDescription: String,

    @ColumnInfo(name = "isComplete")
    var isComplete: Boolean = false

)