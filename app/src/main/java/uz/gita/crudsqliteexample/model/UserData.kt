package uz.gita.crudsqliteexample.model

import android.content.ContentValues
import uz.gita.crudsqliteexample.db.MyDatabase

data class UserData(
    val id: Int,
    val name: String,
    val number: String
):java.io.Serializable {
    fun toContentValues(): ContentValues = ContentValues().apply {
        put(MyDatabase.Constants.NAME, name)
        put(MyDatabase.Constants.NUMBER, number)
    }
}
