package com.example.sladetest

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import java.util.*
import android.view.ViewGroup.LayoutParams
import android.util.DisplayMetrics





class TaskManager(identifier: Int, screenDensity: Float) {

    var schedules = mutableListOf<Schedule>()
    val id        = "$identifier"
    val density   = screenDensity
    var allTasks   = mutableListOf<Task>()

    fun addSchedule(schedule: Schedule){

        schedules.add(schedule)
    }
    fun getSchedule(date: Date): Schedule{

        var index = 0

        while(schedules.get(index).getScheduleDate().date != date.date)
            {
                index++
            }
        return schedules.get(index)
    }


    fun createTask(taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int, taskPriority: Int): Task{

        val task = Task(taskYear, taskMonth, taskDay, taskStartHour, taskStartMinute, taskEndHour, taskEndMinute, taskPriority)

        allTasks.add(task)

        //future implementation: add task to schedule of that day

        return task
    }

    private fun createTaskIcon(task: Task, column: Int, nbOfColumns: Int, description: String, frameLayout: FrameLayout, context: Context): Button{

        //Create the button
        val taskButton = Button(context)

        //Make the actual button invisible so only our task icon background shows
        taskButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))

        //Calculate the task button width
        val taskButtonWidth = (frameLayout.width)/nbOfColumns

        //Create the size, and layout parameters of the button
        val params = LinearLayout.LayoutParams(taskButtonWidth,dpToPx((task.endHour-task.startHour + (task.endMinute/60.0 - task.startMinute/60.0)))*80)     //Create button dimensions depending on task length
        params.setMargins(0, dpToPx((task.startHour + (task.startMinute/60.0))*80.0), 0, 0)
        taskButton.layoutParams = params

        //Set the buttons's background, and text description
        if(task.priority == 1)taskButton.setBackgroundResource(R.drawable.task_icon_priority1)
        if(task.priority == 2)taskButton.setBackgroundResource(R.drawable.task_icon_priority2)
        if(task.priority == 3)taskButton.setBackgroundResource(R.drawable.task_icon_priority3)
        if(task.priority == 4)taskButton.setBackgroundResource(R.drawable.task_icon_priority4)
        taskButton.text = description
        taskButton.setPadding(dpToPx(15.0),dpToPx(10.0),dpToPx(5.0), dpToPx(15.0))

        //Create a parent linear layout view for the button, so it can be placed in the right spot on the page
        val child = LinearLayout(context)
        child.setPadding((taskButtonWidth)*(column),0, 0, 0)
        child.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        child.addView(taskButton)

        //Add button to frame layout
        frameLayout.addView(child)

        //Add a clickListener to the button, so that clicking on a task can bring you to a page with more details of that task
        taskButton.setOnClickListener {

            val intent = Intent(context, TaskViewActivity::class.java)
            intent.putExtra("taskDescription", task.getTaskDescription())
            intent.putExtra("taskStartHour"  , task.startHour)
            intent.putExtra("taskStartMinute", task.startMinute)
            intent.putExtra("taskEndHour"    , task.endHour)
            intent.putExtra("taskEndMinute"  , task.endMinute)
            intent.putExtra("taskYear"       , task.year)
            intent.putExtra("taskMonth"      , task.month)
            intent.putExtra("taskDay"        , task.day)
            intent.putExtra("taskPriority"   , task.priority)
            context.startActivity(intent)
        }

        return taskButton
    }

    fun updateTodayView(year: Int, month: Int, day: Int, frameLayout: FrameLayout, context: Context){

        //Currently, we can support up to 4 columns of overlapping tasks
        val tasksAdded   = mutableListOf<Task>()

        for(item in allTasks){
            //For all tasks in the task manager task list

            if(item.year == year && item.month == month && item.day == day){
                //If the task is for the requested day, add it to the view

                var nbOfCollisions = 0

                for(otherItem in allTasks) {
                    //For all tasks in the task manager task list

                    if (otherItem.year == year && otherItem.month == month && otherItem.day == day && item != otherItem) {
                        // For each other task that day

                        if(tasksCollide(item, otherItem)){
                            //Check if they collide
                            nbOfCollisions++;
                        }

                    }
                }
                //Store the number of collisions
                item.nbOfCollisions = nbOfCollisions
            }
        }

        for(item in allTasks){
            //For all tasks in the task manager task list

            var nbOfCollisionsWithAlreadyAddedTasks = 0

            if(item.year == year && item.month == month && item.day == day){
                //If the task is for the requested day, add it to the view

                for(addedItem in tasksAdded) {
                    //For all tasks in the list of tasks already added

                    if(tasksCollide(item, addedItem)){

                        nbOfCollisionsWithAlreadyAddedTasks++

                    }

                }
            }

            createTaskIcon(item, nbOfCollisionsWithAlreadyAddedTasks, item.nbOfCollisions + 1, item.getTaskDescription(), frameLayout, context)
            tasksAdded.add(item)
        }

    }

    private fun dpToPx(size : Double): Int{
        //This function receives a size as input in dp units, and coverts it into pixel units

        val scale = density

        return (size * scale + 0.5f).toInt()
    }

    private fun tasksCollide(task1: Task, task2: Task): Boolean{
        // This function receives two tasks as input parameters, and returns true if their times overlap by any amount

        var overlaps = false

        //Calculate task start and end times in fractional hours, as these numbers will be reused often
        val task1StartTime = task1.startHour.toDouble()  + task1.startMinute.toDouble()/60.0
        val task1EndTime   = task1.endHour.toDouble()    + task1.endMinute.toDouble()  /60.0
        val task2StartTime = task2.startHour.toDouble()  + task2.startMinute.toDouble()/60.0
        val task2EndTime   = task2.endHour.toDouble()    + task2.endMinute.toDouble()  /60.0

        //Below, we make 4 comparisons that can guarantee whether or not two tasks overlap
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
        if(task1StartTime == task2StartTime && task1EndTime == task2EndTime){
            overlaps = true
        }

        return overlaps
    }
}