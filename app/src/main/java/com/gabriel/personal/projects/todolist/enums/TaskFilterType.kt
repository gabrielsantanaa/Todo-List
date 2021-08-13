package com.gabriel.personal.projects.todolist.enums

import android.content.Context
import com.gabriel.personal.projects.todolist.R

sealed class TaskFilterType {

    object ALL : TaskFilterType() {
        override val menuId: Int
            get() = R.id.ic_filter_all_tasks

        override fun getToolbarTitle(context: Context): String {
            return context.getString(R.string.title_all_tasks)
        }
    }

    object COMPLETED : TaskFilterType() {
        override val menuId: Int
            get() = R.id.ic_filter_completed_tasks

        override fun getToolbarTitle(context: Context): String {
            return context.getString(R.string.title_completed_tasks)
        }
    }

    object UNCOMPLETED : TaskFilterType() {
        override val menuId: Int
            get() = R.id.ic_filter_uncompleted_tasks

        override fun getToolbarTitle(context: Context): String {
            return context.getString(R.string.title_uncompleted_tasks)
        }
    }

    class SEARCH(val query: String) : TaskFilterType() {
        override val menuId: Int
            get() = R.id.ic_action_search

        override fun getToolbarTitle(context: Context): String {
            return context.getString(R.string.search_toolbar_title, query)
        }
    }

    abstract val menuId: Int
    abstract fun getToolbarTitle(context: Context): String

}

