package com.example.sladetest


import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.view.*
import android.widget.*
import java.text.DateFormat
import android.widget.TextView
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        //This function runs once upon creation of the activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Playing around with the clock:
//        var tvw = findViewById<TextView>(R.id.textView1)
//        var picker = findViewById<TimePicker>(R.id.timePicker1)
//        picker.setIs24HourView(true)
//        var btnGet = findViewById<Button>(R.id.button1)
//        btnGet.setOnClickListener {
//            var hour: Int
//            var minute: Int
//            val am_pm: String
//            hour = picker.hour
//            minute = picker.minute
//
//            if (hour > 12) {
//                am_pm = "PM"
//                hour -= 12
//            } else {
//                am_pm = "AM"
//            }
//            tvw.text = "Selected Date: $hour:$minute $am_pm"
//        }


        // Initialize the task manager
        TaskManager.init(resources.displayMetrics.density)

        //Below, we instantiate a calender to get the current date to display at the top of the page
        val calendar      = Calendar.getInstance()
        val currentDate   = DateFormat.getDateInstance().format(calendar.time)
        val textViewDate  = findViewById<TextView>(R.id.textView2)
        textViewDate.text = currentDate


        //Below, we initialize the progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.progress = 50
        progressBar.max      = 100


        //Below, we initialize the timetable time indicator (red line) so that it can move depending on the time of the day
        val timetableIndicatorMover = findViewById<ImageView>(R.id.timetable_indicator_mover)
        val hour                    = calendar.get(Calendar.HOUR_OF_DAY).toDouble()
        val minute                  = calendar.get(Calendar.MINUTE).toDouble()
        val dpHeightInPx            = dpToPx((118 + 80 * (hour + (minute/60.toDouble())))) //calculation: ((start of 12AM row + rowHeight * ( hour + minute/60) )

        timetableIndicatorMover.layoutParams.height = dpHeightInPx
        timetableIndicatorMover.visibility          = View.INVISIBLE


        //Below, we update the schedule view with all of the tasks for the given day
        val frameLayout = findViewById<FrameLayout>(R.id.task_icon_container)
        val timeTableRow = findViewById<TableRow>(R.id.tableRow_12am)




        val content = findViewById<View>(android.R.id.content)
        content.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //Remove it here unless you want to get this callback for EVERY
                //layout pass, which can get you into infinite loops if you ever
                //modify the layout from within this method.
                content.viewTreeObserver.removeGlobalOnLayoutListener(this)

                //Now you can get the width and height from content

                TaskManager.updateTodayView(2019, 7, 25, frameLayout, timeTableRow.measuredHeight, this@MainActivity)


            }
        })

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
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_calendar -> {
                intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_tools -> {
1
            }
            R.id.nav_share -> {
                val task        = TaskManager.createTask(2019, 7, 25, 20, 0, 22, 0, 1  , 1)
                val task2       = TaskManager.createTask(2019, 7, 25, 8 , 0, 10, 0, 2  , 2)
                val task3       = TaskManager.createTask(2019, 7, 25, 3 , 15, 6 , 45, 3, 3)
                val task4       = TaskManager.createTask(2019, 7, 25, 1 , 0, 2 , 0, 4  , 4)
                val task5       = TaskManager.createTask(2019, 7, 25, 2 , 0, 4 , 0, 1  , 5)
                val task6       = TaskManager.createTask(2019, 7, 25, 2 , 0, 4 , 0, 2  , 6)
                val task7       = TaskManager.createTask(2019, 7, 25, 2 , 0, 4 , 0, 1  , 7)
                val task8       = TaskManager.createTask(2019, 7, 25, 2 , 0, 4 , 0, 2  , 8)
                val task9       = TaskManager.createTask(2019, 7, 25, 5 , 0, 6 , 0, 2  , 9)
                val task10      = TaskManager.createTask(2019, 7, 25, 0 , 0, 1 , 0, 2  , 10)
                task.setTaskDescription("This task was made created using task manager")
                task2.setTaskDescription("This task2 was made created using task manager")
                task3.setTaskDescription("This task3 was made created using task manager")
                task4.setTaskDescription("This task4 was made created using task manager")

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun dpToPx(size : Double): Int{

        val scale = resources.displayMetrics.density

        return (size * scale + 0.5f).toInt()
    }


}
