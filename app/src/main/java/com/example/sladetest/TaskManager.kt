package com.example.sladetest

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.util.*
import android.view.ViewGroup.LayoutParams

import com.example.sladetest.MainActivity
import android.util.DisplayMetrics
import android.view.ViewGroup



import androidx.appcompat.app.AppCompatActivity


import java.text.DateFormat

import android.widget.TextView





class TaskManager(identifier: Int, screenDensity: Float) {

    var schedules = mutableListOf<Schedule>()
    val id        = "$identifier"
    val density   = screenDensity
    var allTasks   = mutableListOf<Task>()

    companion object MainActivity {

    }


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


    fun createTask(taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int): Task{

        var task = Task(taskYear, taskMonth, taskDay, taskStartHour, taskStartMinute, taskEndHour, taskEndMinute)

        allTasks.add(task)

        //futur implementation: add task to schedule of that day

        return task
    }

    private fun createTaskIcon(task: Task, column: Int, description: String, frameLayout: FrameLayout, context: Context): Button{

        //Create the button
        val taskButton = Button(context)                                              //Create the button
        taskButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))            //Make the actual button invisible
        var params = LinearLayout.LayoutParams(dpToPx(150.0),dpToPx((task.endHour-task.startHour + (task.endMinute/60.0 - task.startMinute/60.0)))*80)     //Create button dimensions depending on task length
        params.setMargins(0, dpToPx((task.startHour + (task.startMinute/60.0))*80.0), 0, 0)
        taskButton.layoutParams = params
        taskButton.setBackgroundResource(R.drawable.task_icon)
        taskButton.setText(description)

        //Create a parent linear layout for the button, for formatting reasons
        val child = LinearLayout(context)
        child.setPadding(dpToPx(70.0 + 150*(column-1)), dpToPx(125.0), 0, 0)
        child.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        child.addView(taskButton)

        //Add button to frame layout
        frameLayout.addView(child)

        /*
        var task = Task()
        allTasks.add()
        */

        return taskButton
    }

    fun updateTodayView(year: Int, month: Int, day: Int, frameLayout: FrameLayout, context: Context){

        var column1   = mutableListOf<Task>()
        var column2   = mutableListOf<Task>()
        var column3   = mutableListOf<Task>()
        var column4   = mutableListOf<Task>()

        for(item in allTasks){
        //For all tasks in the task manager task list

            if(item.year == year && item.month == month && item.day == day){
            //If the task is for the requested day, add it to the view

                ////////////////////////////
                //Try placing it in column 1
                ////////////////////////////

                var placeInColumn1 = true

                for(columnMember in column1){

                    if(tasksCollide(item, columnMember)){

                        placeInColumn1 = false
                    }
                }
                if(placeInColumn1 == true){

                    createTaskIcon(item, 1, item.getTaskDescription(), frameLayout, context)
                    column1.add(item)
                    continue             //move onto the next task
                }

                ////////////////////////////
                //Try placing it in column 2
                ////////////////////////////

                var placeInColumn2 = true

                for(columnMember in column2){

                    if(tasksCollide(item, columnMember)){

                        placeInColumn2 = false
                    }
                }
                if(placeInColumn2 == true){

                    createTaskIcon(item, 2, item.getTaskDescription(), frameLayout, context)
                    column2.add(item)
                    continue             //move onto the next task
                }

                ////////////////////////////
                //Try placing it in column 3
                ////////////////////////////

                var placeInColumn3 = true

                for(columnMember in column3){

                    if(tasksCollide(item, columnMember)){

                        placeInColumn3 = false
                    }
                }
                if(placeInColumn3 == true){

                    createTaskIcon(item, 3, item.getTaskDescription(), frameLayout, context)
                    column3.add(item)
                    continue             //move onto the next task
                }

                ////////////////////////////
                //Try placing it in column 4
                ////////////////////////////

                var placeInColumn4 = true

                for(columnMember in column4){

                    if(tasksCollide(item, columnMember)){

                        placeInColumn4 = false
                    }
                }
                if(placeInColumn4 == true){

                    createTaskIcon(item, 4, item.getTaskDescription(), frameLayout, context)
                    column4.add(item)
                    continue             //move onto the next task
                }
            }
        }
    }

    private fun dpToPx(size : Double): Int{

        val metrics = DisplayMetrics()

        val scale = density

        val dpHeightInPx = (size * scale + 0.5f).toInt()

        return dpHeightInPx
    }

    private fun tasksCollide(task1: Task, task2: Task): Boolean{
        // This function receives two tasks as input parameters, and returns true if their times overlap by any amount

        var overlaps = false

        //Calculate task start and end times in fractional hours, as these numbers will be reused often
        val task1StartTime = task1.startHour.toDouble()  + task1.startMinute.toDouble()/60.0
        val task1EndTime   = task1.endHour.toDouble()    + task1.endMinute.toDouble()  /60.0
        val task2StartTime = task2.startHour.toDouble()  + task2.startMinute.toDouble()/60.0
        val task2EndTime   = task2.endHour.toDouble()    + task2.endMinute.toDouble()  /60.0

        if(task2StartTime>task1StartTime && task2StartTime<task1EndTime) {
            overlaps = true
        }
        if(task2EndTime>task1StartTime && task2EndTime<task1EndTime) {
            overlaps = true
        }
        if(task1StartTime>task2StartTime && task1StartTime<task2EndTime) {
            overlaps = true
        }
        if(task1EndTime>task2StartTime && task1EndTime<task2EndTime) {
            overlaps = true
        }

        return overlaps
    }

}