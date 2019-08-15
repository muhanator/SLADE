package com.example.sladetest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.view.ViewGroup.LayoutParams

//This class is used to store all the information about each of the tasks
object TaskManager{

    var density = 0.toFloat()
    val TABLE_BORDER_THICKNESS = 3

    fun init(screenDensity: Float){
        density   = screenDensity
    }

    var allSchedules = mutableListOf<Schedule>()
    var allTasks   = mutableListOf<Task>() //List of all the tasks


    fun createSchedule(day: Int, month : Int, year : Int): Schedule{

        val newSchedule = Schedule(day, month, year)    // Create a new schedule
        allSchedules.add(newSchedule)                   // Add it to the internal list of schedules for the task manager
        return newSchedule                              // Return the newly created schedule
    }
    fun getSchedule(day: Int, month : Int, year : Int): Schedule{

        val searchId = (10000)*year + (100)*month + day //Create the search ID

        for (schedule in allSchedules){
        // Here, we search the task manager's internal schedule list for the desired schedule

            if(schedule.id == searchId){
                return schedule
            }
        }

        // If we did not find the schedule in the list, we create it
        return createSchedule(day, month, year)
    }

    //TODO: Task-14: This function will have to get called when after you have input the values to create a task
    fun createTask(taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int, taskPriority: String, taskID: Int): Task{

        val task = Task(taskYear, taskMonth, taskDay, taskStartHour, taskStartMinute, taskEndHour, taskEndMinute, taskPriority, taskID)

        val scheduleForTask = getSchedule(taskDay, taskMonth, taskYear)
        scheduleForTask.addTask(task)

        return task
    }

    private fun createTaskIcon(task: Task, column: Int, nbOfColumns: Int, rowHeight: Int, description: String, frameLayout: FrameLayout, context: Context): Button{

        //Create the button
        val taskButton = Button(context)

        //Make the actual button invisible so only our task icon background shows
        taskButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))

        //Calculate the task button width
        val taskButtonWidth = (frameLayout.width)/nbOfColumns

        //Create the size, and layout parameters of the button
        val params = LinearLayout.LayoutParams(taskButtonWidth,((task.endHour-task.startHour + (task.endMinute/60.0 - task.startMinute/60.0))*rowHeight).toInt())     //Create button dimensions depending on task length
        params.setMargins(0, dpToPx((task.startHour + (task.startMinute/60.0))*80.0)- TABLE_BORDER_THICKNESS, 0, 0)
        taskButton.layoutParams = params

        //Set the buttons's background, and text description
        if(task.priority.contains("1"))taskButton.setBackgroundResource(R.drawable.task_icon_priority1)
        if(task.priority.contains("2"))taskButton.setBackgroundResource(R.drawable.task_icon_priority2)
        if(task.priority.contains("3"))taskButton.setBackgroundResource(R.drawable.task_icon_priority3)
        if(task.priority.contains("4"))taskButton.setBackgroundResource(R.drawable.task_icon_priority4)
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
            intent.putExtra("task", task)
            context.startActivity(intent)
        }

        return taskButton
    }


   //TODO: This function will need to be called for task-14 (after finished creating a task)
    fun updateTodayView(year: Int, month: Int, day: Int, frameLayout: FrameLayout, rowHeight: Int, context: Context){

        //Currently, we can support up to 4 columns of overlapping tasks
        val tasksAdded   = mutableListOf<Task>()
        var tasksThatDay = mutableListOf<Task>()

        tasksThatDay = getSchedule(day, month, year).getScheduleTasks()

        for(item in tasksThatDay){
            //For all tasks that given day

            var nbOfCollisions = 0

            for(otherItem in tasksThatDay) {
                //For all tasks that given day

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

        for(item in tasksThatDay){
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

            createTaskIcon(item, nbOfCollisionsWithAlreadyAddedTasks, item.nbOfCollisions + 1, rowHeight, item.getTaskDescription(), frameLayout, context)
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