package com.project.mynotes.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class DbManager {

    // اسم قاعدة البيانات
    val dbName = "MyNotes"

    // اسم الجدول
    val dbNotesTable = "Notes"

    val dbVersion = 1

    // اسم الاعمدة
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"

    // لانشاء جدول في قاعدة البيانات
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS "+ dbNotesTable + " ("+ colID +" INTEGER PRIMARY KEY,"+
            colTitle + " TEXT, "+ colDes +" TEXT);"

    var sqlDB : SQLiteDatabase?=null

    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    // كلاس للتعامل مع الداتابيز
    inner class DatabaseHelperNotes:SQLiteOpenHelper{

        var context:Context ?= null

        constructor(context: Context):super(context,dbName,null,dbVersion){
           this.context = context
        }

        // هنا بنكون الجدول في الداتابيز
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            Log.e("database","database is created")
        }

        // عشان عند التحديث يتم حذف الداتابيز وبناءها من جديد
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS "+ dbNotesTable)
        }

    }


    // اضافة سجل
    fun insertNote(values:ContentValues):Long{
       val id = sqlDB!!.insert(dbNotesTable,"",values)

        return id
    }

    // راح نعرض البيانات المسجلة
    fun query(projection:Array<String>,selection:String,
    selectionArgs:Array<String>,sortOrder:String):Cursor{

        val db = SQLiteQueryBuilder()
        db.tables = dbNotesTable
        val cursor = db.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

    // selection : ايش بدي اختار عشان احذف
    fun delete(selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.delete(dbNotesTable,selection,selectionArgs)

        return count
    }

    //values:ContentValues : ايش الاشياء اللي بدنا نعدلهم
    //selection:String : ايش الشرط
    // selectionArgs:Array<String> : ال id مثلا 2 بدلي اياه ب values:ContentValues
    fun update(values:ContentValues,selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.update(dbNotesTable,values,selection,selectionArgs)
        return count
    }
}