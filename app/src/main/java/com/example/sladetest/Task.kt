package com.example.sladetest

import java.util.*

import android.widget.*
import java.io.Serializable


class Task(taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int, taskPriority: String, taskID: Int): Serializable{

    var year        : Int
    var month       : Int
    var day         : Int
    var startHour   : Int
    var startMinute : Int
    var endHour     : Int
    var endMinute   : Int
    var priority    : String
    var nbOfCollisions   : Int
    var status      : Int
    var id          : Int

    private var taskDescription = "Task Description"

    init{
        year           = taskYear
        month          = taskMonth
        day            = taskDay
        startHour      = taskStartHour
        startMinute    = taskStartMinute
        endHour        = taskEndHour
        endMinute      = taskEndMinute
        priority       = taskPriority
        nbOfCollisions = 0
        status         = 0
        id             = taskID
    }

    fun setTaskDescription(newTaskDescription: String){

        taskDescription = newTaskDescription
    }
    fun getTaskDescription(): String{

        return taskDescription
    }
}