package com.example.sladetest

import java.util.*

class Task (taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int){

    var year        : Int
    var month       : Int
    var day         : Int
    var startHour   : Int
    var startMinute : Int
    var endHour     : Int
    var endMinute   : Int

    private var taskDescription = "Task Description"

    init{
        year        = taskYear
        month       = taskMonth
        day         = taskDay
        startHour   = taskStartHour
        startMinute = taskStartMinute
        endHour     = taskEndHour
        endMinute   = taskEndMinute
    }

    fun setTaskDescription(newTaskDescription: String){

        taskDescription = newTaskDescription
    }
    fun getTaskDescription(): String{

        return taskDescription
    }
}