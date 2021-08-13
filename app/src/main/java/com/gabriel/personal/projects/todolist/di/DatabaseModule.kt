package com.gabriel.personal.projects.todolist.di

import android.content.Context
import com.gabriel.personal.projects.todolist.data.db.TaskDatabase
import com.gabriel.personal.projects.todolist.data.source.local.RoomTaskDataSource
import com.gabriel.personal.projects.todolist.data.source.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun getRepository(@ApplicationContext context: Context): TaskRepository =
        TaskRepository(
            RoomTaskDataSource(
                TaskDatabase.getInstance(context).databaseDAO
            )
        )

}