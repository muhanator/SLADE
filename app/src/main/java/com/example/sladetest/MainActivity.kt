package com.example.sladetest

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.*
import java.util.Calendar
import android.view.ViewGroup.LayoutParams
import android.graphics.Color


import java.text.DateFormat
import android.widget.LinearLayout
import android.widget.TextView






class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Below, we have access to inputs that occur on the calendar
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }

        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance().format(calendar.time)
        val textViewDate = findViewById<TextView>(R.id.date_text)
        textViewDate.setText(currentDate)


        //Below, we initialize the progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.setProgress(50);
        progressBar.setMax(100);

        //Below, we initilize the timetable scrollview
        val timetableView = findViewById<ScrollView>(R.id.timetable_scrollview)

        //Below, we initialize the timetable time indicator so that it can move depending on the time of the day
        val  timetable_indicator_mover = findViewById<ImageView>(R.id.timetable_indicator_mover)
        val  hour = (calendar.get(Calendar.HOUR_OF_DAY)).toDouble()
        val  minute = calendar.get(Calendar.MINUTE).toDouble()
        val scale = resources.displayMetrics.density
        val dpHeightInPx = dpToPx((118 + 80 * (hour + (minute/60.toDouble()))))   //calculation: ((start of 12AM row + rowHeight * ( hour + minute/60) )
        timetable_indicator_mover.layoutParams.height = dpHeightInPx
        timetable_indicator_mover.visibility = View.INVISIBLE

        //Below, we update the schedule view with all of the tasks for the given day
        updateScheduleView()

        //Below, we initialize the action toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
            finish()
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
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_calendar -> {
                intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
                finish()
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

    fun updateScheduleView(){

        //Create a task button
        /* working
        val taskButton = ImageButton(this)
        taskButton.scaleType = ImageView.ScaleType.FIT_XY
        taskButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        taskButton.layoutParams = LinearLayout.LayoutParams(dpToPx(150.0),dpToPx(160.0))
        //taskButton.foregroundGravity(Gravity.START)
        taskButton.setImageResource(R.drawable.task_icon)
        //taskButton.paddingTop = 100 */


        //Create button #1
        val taskButton = Button(this)
        taskButton.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        var params = LinearLayout.LayoutParams(dpToPx(150.0),dpToPx(160.0))
        params.setMargins(0, dpToPx(80.0), 0, 0)
        taskButton.layoutParams = params
        taskButton.setBackgroundResource(R.drawable.task_icon)
        taskButton.setText("Task #1: Do the thing.")

        //Create button #2
        val taskButton2 = Button(this)
        taskButton2.setBackgroundColor(Color.parseColor("#00FFFFFF"))
        var params2 = LinearLayout.LayoutParams(dpToPx(150.0),dpToPx(160.0))
        params2.setMargins(0, dpToPx(160.0), 0, 0)
        taskButton2.layoutParams = params2
        taskButton2.setBackgroundResource(R.drawable.task_icon)
        taskButton2.setText("Task #2: Do the other thing.")

        //Create a new LinearLayout
        val child = LinearLayout(this)
        child.setPadding(dpToPx(70.0), dpToPx(125.0), 0, 0)
        child.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        child.addView(taskButton)

        //Create a new LinearLayout 2
        val child2 = LinearLayout(this)
        child2.setPadding(dpToPx(230.0), dpToPx(125.0), 0, 0)
        child2.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        child2.addView(taskButton2)

        //Add the linear layout to the frame layout
        val frameLayout = findViewById<FrameLayout>(R.id.schedule_frame_layout)
        frameLayout.addView(child)
        frameLayout.addView(child2)
    }


    fun dpToPx(size : Double): Int{

        val scale = resources.displayMetrics.density

        val dpHeightInPx = (size * scale + 0.5f).toInt()

        return dpHeightInPx
    }


}
