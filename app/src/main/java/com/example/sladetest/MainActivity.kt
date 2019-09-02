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
import kotlinx.android.synthetic.main.home_content.*
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
import java.time.LocalDate
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.*
import android.content.*
import androidx.viewpager.widget.ViewPager


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val calendar = Calendar.getInstance()
    var year : Int? = 0
    var month: Int? = 0
    var day  : Int? = 0
    private var currentTheme = SettingsData.colorMode

    override fun onCreate(savedInstanceState: Bundle?) {
        //This function runs once upon creation of the activity
        super.onCreate(savedInstanceState)

        // Below, we initialize the theme, which will determine the colors of the app
        initializeTheme()

        setContentView(R.layout.activity_main)

        // Initialize the task manager
        TaskManager.init(resources.displayMetrics.density)
      
        // Initialize the date for the today view. If this is upon app launch, the date will be set to today's date.
        // If the activity is being called from CalendarActivity, it will use the given date which is not necessarily today's.
        initializeDate()
      
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
                TaskManager.updateTodayView(year!!, month!!, day!!, frameLayout, timeTableRow.measuredHeight, this@MainActivity)
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

    override fun onResume() {

        super.onResume()

        val theme = SettingsData.colorMode

        if(theme != currentTheme) {
            // Recreate needs to be invoked in order to recreate the activity. This way the new theme can be applied on the current screen.
            recreate()
        }
    }

    private fun initializeDate(){

        //Below, we instantiate a calender to get the current date to display at the top of the page
        year  = intent.extras?.getInt("year")
        month = intent.extras?.getInt("month")
        day   = intent.extras?.getInt("day")

        if(year == null && month == null && day == null) {
            // If no date was passed to the main activity, set it to the current date
            year  = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1    // Months are zero-indexed in Calendar API
            day   = calendar.get(Calendar.DAY_OF_MONTH)

            val currentDate   = DateFormat.getDateInstance().format(calendar.time)
            val textViewDate  = findViewById<TextView>(R.id.textView2)
            textViewDate.text = currentDate
        }
        else{
            // If a date was passed to the main activity, set the schedule date to that date
            calendar.set(Calendar.DAY_OF_MONTH, day!!)
            calendar.set(Calendar.MONTH       , month!!)
            calendar.set(Calendar.YEAR        , year!!)

            val currentDate   = DateFormat.getDateInstance().format(calendar.time)
            val textViewDate  = findViewById<TextView>(R.id.textView2)
            textViewDate.text = currentDate

            month = month!! + 1     // The month will have to be incremented here, since in it has not been already if we end up in this else statement
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
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_calendar -> {
                intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_tools -> {
                intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
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

    private fun dpToPx(size : Double): Int{

        val scale = resources.displayMetrics.density

        return (size * scale + 0.5f).toInt()
    }

    private fun initializeTheme(){

        currentTheme = SettingsData.colorMode

        // Below, we set the theme of the app depending on user settings
        if(SettingsData.colorMode == 1) {
            setTheme(R.style.DarkMode_AppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }


}
