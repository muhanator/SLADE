package com.example.sladetest

import java.util.*

class TaskManager(identifier: Int) {

    var schedules = mutableListOf<Schedule>()
    val id        = "$identifier"

    fun addSchedule(schedule: Schedule){

        schedules.add(schedule)
    }
    fun getSchedule(date: Date): Schedule{

        var searchDate = Date()
        var index = 0

        while(schedules.get(index).getScheduleDate().date != date.date)
            {
                index = index + 1
            }
        return schedules.get(index)

    }
}