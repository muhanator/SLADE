package com.example.sladetest

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
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
import androidx.appcompat.widget.ActionBarContainer
import kotlinx.android.synthetic.main.home_content.*
import java.util.*
import java.time.LocalDate
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment

class SwipeLeftAdapter (fm:FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return SwipeRightActivity()
            1 -> return SwipeRightActivity()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 2
    }
}