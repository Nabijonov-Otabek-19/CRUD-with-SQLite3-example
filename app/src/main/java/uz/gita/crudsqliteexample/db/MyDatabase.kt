package uz.gita.crudsqliteexample.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.gita.crudsqliteexample.model.UserData
import uz.gita.crudsqliteexample.utils.Constants

class MyDatabase(context: Context) : SQLiteOpenHelper(context, Constants.DB_NAME, null, 1) {


    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table ${Constants.TABLE_NAME} (${Constants.NAME} Text primary key, ${Constants.NUMBER} Text)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists ${Constants.DB_NAME}")
    }

    fun saveUserData(user: UserData): Boolean {
        val p = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constants.NAME, user.name)
        contentValue.put(Constants.NUMBER, user.number)

        val result = p.insert(Constants.DB_NAME, null, contentValue)
        if (result == (-1).toLong()) {
            return false
        }
        return true
    }

    fun getUser(): Cursor? {
        val p = this.writableDatabase
        return p.rawQuery("select * from ${Constants.DB_NAME}", null)
    }

}