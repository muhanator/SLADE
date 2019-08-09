package com.example.sladetest

import android.R.attr.*
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.task_creation_content.*
import android.widget.PopupWindow
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams
import android.graphics.Color
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import kotlinx.android.synthetic.main.popup_layout.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


//Class to create tasks and store them in a list
class TaskCreateActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var formate = SimpleDateFormat("dd MMM, YYYY",Locale.US)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_creation_content) //setting the background to the task_creation.xml
        //val currentHour = timePicker.hour

        var startTimeButton = findViewById<Button>(R.id.start_time_popup)
        startTimeButton.setOnClickListener{
            // Initialize a new layout inflater instance
            val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.popup_layout,null)

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

            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

                // Get the widgets reference from custom view
                val tv = view.findViewById<TextView>(R.id.text_view)
                val buttonPopup = view.findViewById<Button>(R.id.dismiss)
                val startTimePicker = view.findViewById<TimePicker>(R.id.start_time_picker)

                // Set click listener for popup window's text view
                tv.setOnClickListener{
                    // Change the text color of popup window's text view
                    tv.setTextColor(Color.RED)
                }
                var currentHour = 0
                var currentMinute = 0
                var currentYear = 0
                var currentMonth =0
                var currentDay = 0
                var endHour = 0
                var endMinute = 0
                var priority = 0


                // Set a click listener for popup's button widget
                buttonPopup.setOnClickListener{
                    currentHour = startTimePicker.hour
                    currentMinute = startTimePicker.minute
                    // Dismiss the popup window
                    popupWindow.dismiss()
                }

                // Set a dismiss listener for popup window
                popupWindow.setOnDismissListener {
                    Toast.makeText(applicationContext,getString(R.string.start_time, currentHour, currentMinute),Toast.LENGTH_SHORT).show()
                }

                // Finally, show the popup window on app
                TransitionManager.beginDelayedTransition(create_task_layout)
                popupWindow.showAtLocation(
                    create_task_layout, // Location to display popup window
                    Gravity.CENTER, // Exact position of layout to display popup
                    0, // X offset
                    0 // Y offset
                )

                //NEED A WAY TO STORE THE TIME IN

//                val startTime = findViewById<TimePicker>(R.id.start_time_picker)
//                val saySomething = view.findViewById<TextView>(R.id.textView1)
//                startTime.setOnTimeChangedListener{
//                        view,hourOfDay,minute->
//                    saySomething.text = "Time(HH:MM) ${(hourOfDay)} " +
//                            ": $minute ${(hourOfDay)}"
//                }

//                val now = Calendar.getInstance()
//                val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                    val selectedTime = Calendar.getInstance()
//                    selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
//                    selectedTime.set(Calendar.MINUTE,minute)
//                    startTimeButton.text = timeFormat.format(selectedTime.time)
//                },
//                    now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
//                timePicker.show()
            }
        }
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
