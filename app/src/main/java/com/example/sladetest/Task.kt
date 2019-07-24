package com.example.sladetest

import java.util.*

class Task (){

    private var date = Date()
    private var taskDescription = "Task Description"

    fun setTaskDate(newDate: Date){

        date = newDate
    }

    fun getTaskDate(): Date{

        return date
    }

    fun setTaskDescription(newTaskDescription: String){

        taskDescription = newTaskDescription
    }
    fun getTaskDescription(): String{

        return taskDescription
    }
}