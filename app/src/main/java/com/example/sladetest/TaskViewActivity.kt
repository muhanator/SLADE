package com.example.sladetest

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_view_content.*

//Used to show all the details of the task
class TaskViewActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_view)

        val nullableTask = intent.extras?.getSerializable("task") as? Task
        val task = nullableTask!!

        //Below, we receive data about that task
        val taskDescription = task.getTaskDescription()
        val taskStartHour   = task.getStartHour()
        val taskStartMinute = task.getStartMinute()
        val taskEndHour     = task.getEndHour()
        val taskEndMinute   = task.getEndMinute()
        val taskYear        = task.getYear()
        val taskMonth       = task.getMonth()
        val taskDay         = task.getDay()
        val taskPriority    = task.getPriority()

        //Below, we initialize the task information
        val taskDescriptionTextBox = findViewById<TextView>(R.id.task_description_text)
        taskDescriptionTextBox.text = taskDescription
        val taskPriorityTextBox = findViewById<TextView>(R.id.task_priority_text)
        taskPriorityTextBox.text = taskPriority.toString()
        val dateTextBox = findViewById<TextView>(R.id.task_date)
        dateTextBox.text = getString(R.string.task_date, taskDay, taskMonth, taskYear)
        val taskStartTimeTextBox = findViewById<TextView>(R.id.task_start_time_text)
        taskStartTimeTextBox.text = getString(R.string.task_time, taskStartHour, taskStartMinute)
        val taskEndTimeTextBox = findViewById<TextView>(R.id.task_end_time_text)
        taskEndTimeTextBox.text = getString(R.string.task_time, taskEndHour, taskEndMinute)

        //Below, we initialize the "Edit Task" button
        val editTaskButton = findViewById<ImageButton>(R.id.edit_task_button)

        editTaskButton.setOnClickListener {
            val intent = Intent(this, TaskEditActivity::class.java)
            intent.putExtra("startTime", taskStartTimeTextBox.text.toString())
            intent.putExtra("endTime", taskEndTimeTextBox.text.toString())
            intent.putExtra("date", dateTextBox.text.toString())
            intent.putExtra("startHour", taskStartHour)
            intent.putExtra("startMinute", taskStartMinute)
            intent.putExtra("endHour", taskEndHour)
            intent.putExtra("endMinute", taskEndMinute)
            intent.putExtra("currentYear", taskYear)
            intent.putExtra("currentMonth", taskMonth)
            intent.putExtra("currentDay", taskDay)
            intent.putExtra("task", task)
            intent.putExtra("priority", taskPriority)
            this.startActivity(intent)
        }

        //Below, we initialize the "Delete Task" button
        val deleteTaskButton = findViewById<ImageButton>(R.id.delete_task_button)

        deleteTaskButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            // set message of alert dialog
            dialogBuilder.setMessage("Are you sure you want to delete this Task?")
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Delete", DialogInterface.OnClickListener {
                        _, _ -> deleteTask(task)
                })
                // negative button text and action
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, _ -> dialog.cancel()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Delete Task?")
            // show alert dialog
            alert.show()
        }

        //Below, we initialize the completion switch
        val completionSwitch = findViewById<Switch>(R.id.completion_switch)
        completionSwitch.setOnClickListener{
            Toast.makeText(this@TaskViewActivity, "This task is now COMPLETE!", Toast.LENGTH_SHORT).show()
        }


        //Below, we initialize the action toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.create_task_plus_button)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

    private fun deleteTask(task:Task){
        val taskSchedule = TaskManager.getSchedule(task.getDay(), task.getMonth(), task.getYear())

        for(scheduleTask in taskSchedule.tasks){

            if(task.getId() == scheduleTask.getId()){
                TaskManager.removeTask(scheduleTask, task.getDay(), task.getMonth(), task.getYear())
            }
        }

        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }

    // Extension function to show toast message
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            //R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_calendar -> {
                intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
