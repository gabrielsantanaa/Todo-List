package com.gabriel.personal.projects.todolist.ui.todolist

import android.graphics.Paint
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.gabriel.personal.projects.todolist.data.db.entity.Task

@BindingAdapter("taskTitle")
fun TextView.setTaskTitle(task: Task?) {
    task?.let {
        text = task.taskTitle
    }
}

@BindingAdapter("taskDescription")
fun TextView.setTaskDescription(task: Task?) {
    task?.let {
        text = task.taskDescription
    }
}

@BindingAdapter("app:onLongClickTask")
fun CardView.setOnLongClickListener(
    func: () -> Unit
) {
    setOnLongClickListener {
        func()
        true
    }
}

@BindingAdapter("completedTask")
fun TextView.setStyle(enabled: Boolean) {
    if (enabled) {
        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}
