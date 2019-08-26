package com.example.sladetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import android.app.*
import android.content.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.DialogInterface
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.task_creation_content.*
import kotlinx.android.synthetic.main.task_edit_content.*
import java.util.*

class TaskEditActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_edit_content)

        var editDate = findViewById<Button>(R.id.edit_task_date)
        var editStartTime = findViewById<Button>(R.id.task_start_time_text)
        var editEndTime = findViewById<Button>(R.id.task_end_time_text)

        //Below, we receive data about that task
        val nullableTask = intent.extras?.getSerializable("task") as Task
        var task = nullableTask!!

        val taskSchedule = TaskManager.getSchedule(task.getDay(), task.getMonth(), task.getYear())
        for(scheduleTask in taskSchedule.tasks){
            if(task.getId() == scheduleTask.getId()){
                task = scheduleTask
            }
        }

        val taskStartHour   = intent.extras?.getString("startTime")
        val taskEndHour = intent.extras?.getString("endTime")
        val dateString = intent.extras?.getString("date")
        var currentDay = intent.extras?.getInt("currentDay")
        var currentMonth = intent.extras?.getInt("currentMonth")
        var currentYear = intent.extras?.getInt("currentYear")

        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.priority_list, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        priority_spinner2.adapter = adapter

        var startTimeBox = findViewById<Button>(R.id.task_start_time_text)
        startTimeBox.text = taskStartHour
        var endTimeBox = findViewById<Button>(R.id.task_end_time_text)
        endTimeBox.text = taskEndHour
        var dateBox = findViewById<Button>(R.id.edit_task_date)
        dateBox.text = dateString

        //edit date listener:
        editDate.setOnClickListener(){
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                currentDay = mDay
                currentYear = mYear
                currentMonth = mMonth + 1
                editDate.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        //edit Start time listener
        editStartTime.setOnClickListener(){
            setTime(editStartTime, true, task)
        }
        //edit end time listener
        editEndTime.setOnClickListener(){
            setTime(editEndTime, false, task)
        }


        val finishButton = findViewById<Button>(R.id.finish_editing_task_button)
        finishButton.setOnClickListener(){
            var priority = priority_spinner2.selectedItem.toString()
            task.setPriority(priority)


            var schedule = TaskManager.getSchedule(task.getDay(), task.getMonth(), task.getYear())
            //remove task from schedule (have to do this because schedule still has a reference to the old task)
            schedule.removeTask(task)

            task.setDay(currentDay!!)
            task.setYear(currentYear!!)
            task.setMonth(currentMonth!!)

            var newSchedule = TaskManager.getSchedule(currentDay!!, currentMonth!!, currentYear!!)
            newSchedule.addTask(task)

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setTime(theTime:Button, isStartTime:Boolean, task: Task){
        val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.time,null)
        val timePicker = view.findViewById<TimePicker>(R.id.time_picker)

        // Initialize a new instance of popup window to show out popup_layout view
        val popupWindow = PopupWindow(
            view, // This is the view we are using for our popup
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut

            val buttonPopup = view.findViewById<Button>(R.id.set_time)
            // Set a click listener for popup's button widget
            buttonPopup.setOnClickListener {
                var currentHour = timePicker.hour
                var currentMinute = timePicker.minute
                theTime.setText("" + currentHour + ":" + currentMinute)

                if (isStartTime){
                    task.setStartHour(currentHour)
                    task.setStartMinute(currentMinute)
                }
                else {
                    task.setEndHour(currentHour)
                    task.setEndMinute(currentMinute)
                }

                popupWindow.dismiss()
            }

            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(edit_task_layout)
            popupWindow.showAtLocation(
                edit_task_layout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }
    }

    fun getPriority():String{
        return priority_spinner.selectedItem.toString()
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
                finish()
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
