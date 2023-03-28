package uz.gita.crudsqliteexample.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import uz.gita.crudsqliteexample.databinding.ActivityAddContactBinding
import uz.gita.crudsqliteexample.db.MyDatabase
import uz.gita.crudsqliteexample.model.UserData

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddContactBinding
    private lateinit var myDB: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDB = MyDatabase.getInstance(this)

        binding.apply {
            btnSave.setOnClickListener {

                val name = edtName.text.toString()
                val number = edtNumber.text.toString()
                val saveData = myDB.saveUserData(UserData(0, name, number))

                if (edtName.text.toString().isEmpty() || edtNumber.text.toString().isEmpty()) {
                    Toast.makeText(this@AddContactActivity, "Fill data", Toast.LENGTH_SHORT).show()

                } else {
                    if (saveData) {
                        Toast.makeText(this@AddContactActivity, "Save Contact", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(this@AddContactActivity, "Exist Contact", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}