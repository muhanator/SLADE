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
import android.view.View
import android.widget.*
import java.text.DateFormat
import android.widget.TextView
import java.util.*
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.EditText
import android.view.LayoutInflater




class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentTheme = SettingsData.colorMode

    override fun onCreate(savedInstanceState: Bundle?) {
        //This function runs once upon creation of the activity

        super.onCreate(savedInstanceState)

        // Below, we initialize the theme, which will determine the colors of the app
        initializeTheme()

        setContentView(R.layout.activity_settings)


        this.setTheme(R.style. DarkMode_AppTheme)

        // Initialize the colors, depending on the color mode (normal, dark mode, etc)
        // initializeColors()



        //Below, we initialize the Night Mode Switch
        val nightModeSwitch = findViewById<Switch>(R.id.switch1)

        if(SettingsData.colorMode == 1) nightModeSwitch.isChecked = true    // This line of code ie needed to remember the state of the swtich

        nightModeSwitch.setOnCheckedChangeListener{ buttonView, isChecked ->

            if(isChecked){
                SettingsData.colorMode = 1
            }
            else{
                SettingsData.colorMode = 0
            }

            // Below, we initialize the theme, which will determine the colors of the app
            initializeTheme()
            recreate()          // Recreate needs to be invoked in order to recreate the activity. This way the new theme can be applied on the current screen.
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

    override fun onResume() {

        super.onResume()

        val theme = SettingsData.colorMode

        if(theme != currentTheme) {
            // Recreate needs to be invoked in order to recreate the activity. This way the new theme can be applied on the current screen.
            recreate()
        }
    }

    private fun dpToPx(size : Double): Int{

        val scale = resources.displayMetrics.density

        return (size * scale + 0.5f).toInt()
    }

    private fun initializeTheme(){

        // Below, we set the theme of the app depending on user settings
        if(SettingsData.colorMode == 1) {
            setTheme(R.style.DarkMode_AppTheme)
        }
        else{
            setTheme(R.style.AppTheme)
        }
    }

}
