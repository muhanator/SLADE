package com.example.sladetest

import java.util.*

import android.widget.*
import java.io.Serializable


class Task(taskYear: Int, taskMonth: Int, taskDay: Int, taskStartHour: Int, taskStartMinute: Int, taskEndHour: Int, taskEndMinute: Int, taskPriority: String, taskID: Int): Serializable{

    private var year        : Int
    private var month       : Int
    private var day         : Int
    private var startHour   : Int
    private var startMinute : Int
    private var endHour     : Int
    private var endMinute   : Int
    private var priority    : String
    private var nbOfCollisions   : Int
    private var status      : Int
    private var id          : Int

    private var taskDescription = "Task Description"

    init{
        year           = taskYear
        month          = taskMonth
        day            = taskDay
        startHour      = taskStartHour
        startMinute    = taskStartMinute
        endHour        = taskEndHour
        endMinute      = taskEndMinute
        priority       = taskPriority
        nbOfCollisions = 0
        status         = 0
        id             = taskID
    }

    fun setTaskDescription(newTaskDescription: String){

        taskDescription = newTaskDescription
    }
    fun getTaskDescription(): String{

        return taskDescription
    }

    fun setYear(y: Int){
        year = y
    }

    fun getYear(): Int{
        return year
    }

    fun setMonth(m: Int){
        month = m
    }

    fun getMonth(): Int{
        return month
    }

    fun setDay(d: Int){
        day = d
    }

    fun getDay(): Int{
        return day
    }

    fun setStartHour(sh: Int){
        startHour = sh
    }

    fun getStartHour(): Int{
        return startHour
    }

    fun setStartMinute(sm: Int){
        startMinute = sm
    }

    fun getStartMinute(): Int{
        return startMinute
    }

    fun setEndHour(eh: Int){
        endHour = eh
    }

    fun getEndHour(): Int{
        return endHour
    }

    fun setEndMinute(em: Int){
        endMinute = em
    }

    fun getEndMinute(): Int{
        return endMinute
    }

    fun setPriority(p: String){
        priority = p
    }

    fun getPriority(): String{
        return priority
    }

    fun setNbOfCollisions(c: Int){
        nbOfCollisions = c
    }

    fun getNbOfCollisions(): Int{
        return nbOfCollisions
    }

    fun setStatus(s: Int){
        status = s
    }

    fun getStatus(): Int{
        return status
    }

    fun setId(i: Int){
        id = i
    }

    fun getId(): Int{
        return id
    }
}