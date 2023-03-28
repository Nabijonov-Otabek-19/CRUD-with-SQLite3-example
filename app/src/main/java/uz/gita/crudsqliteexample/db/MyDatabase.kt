package uz.gita.crudsqliteexample.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.gita.crudsqliteexample.model.UserData

class MyDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context, Constants.DB_NAME, null, 1) {

    companion object {
        private lateinit var instance: MyDatabase

        fun getInstance(context: Context): MyDatabase {
            if (!(::instance.isInitialized)) {
                instance = MyDatabase(context)
            }
            return instance
        }
    }

    object Constants {
        const val DB_NAME = "Userdata"
        const val TABLE_NAME = "Users"
        const val ID = "id"
        const val NAME = "name"
        const val NUMBER = "number"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(
            """
            CREATE TABLE ${Constants.TABLE_NAME} (
            ${Constants.ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
            ${Constants.NAME} Text,
            ${Constants.NUMBER} Text)
        """.trimIndent()
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists ${Constants.DB_NAME}")
    }

    fun saveUserData(user: UserData): Boolean {
        val p = this.writableDatabase
        val result = p.insert(Constants.TABLE_NAME, null, user.toContentValues())
        if (result == (-1).toLong()) {
            return false
        }
        return true
    }

    fun deleteData(user: UserData): Boolean {
        val db = this.writableDatabase
        val success =
            db.delete(Constants.TABLE_NAME, "${Constants.ID} =?", arrayOf(user.id.toString()))
                .toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun updateData(user: UserData): Boolean {
        val db = this.writableDatabase
        val result = db.update(
            Constants.TABLE_NAME, user.toContentValues(),
            "${Constants.ID} =?", arrayOf(user.id.toString())
        ).toLong()
        db.close()
        return Integer.parseInt("$result") != -1
    }

    fun getUser(): Cursor? {
        val p = this.writableDatabase
        return p.rawQuery("select * from ${Constants.TABLE_NAME}", null)
    }
}