package com.example.sladetest

class Schedule(givenDay: Int, givenMonth: Int, givenYear: Int) {

    val id          : Int
    val year        : Int
    val month       : Int
    val day         : Int
    var tasks = mutableListOf<Task>()

    init{
        year  = givenYear
        month = givenMonth
        day   = givenDay
        id    = (10000)*year + (100)*month + day  //ex: If the date is jan 30th 2019, the id will be 20190130
    }

    fun addTask(task: Task){

        tasks.add(task)
    }
    fun getScheduleTasks(): MutableList<Task>{

        return tasks
    }
}