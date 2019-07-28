package com.example.sladetest

import java.util.*

class Schedule {

    private var date = Date()
    var tasks = mutableListOf<Task>()

    fun setScheduleDate(newDate: Date){

        date = newDate
    }

    fun getScheduleDate(): Date {

        return date
    }

    fun addScheduleTask(task: Task){

        tasks.add(task)
    }
    fun getScheduleTasks(): MutableList<Task>{

        return tasks
    }
}