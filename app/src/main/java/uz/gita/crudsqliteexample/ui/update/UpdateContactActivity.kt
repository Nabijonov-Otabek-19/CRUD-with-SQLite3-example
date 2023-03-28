package uz.gita.crudsqliteexample.ui.update

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.gita.crudsqliteexample.databinding.ActivityUpdateContactBinding
import uz.gita.crudsqliteexample.db.MyDatabase
import uz.gita.crudsqliteexample.model.UserData

class UpdateContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateContactBinding
    private lateinit var myDB: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDB = MyDatabase.getInstance(this)

        val intent = intent
        val user = intent.getSerializableExtra("user") as UserData

        binding.apply {
            edtName.setText(user.name)
            edtNumber.setText(user.number)

            btnUpdate.setOnClickListener {
                val update = myDB.updateData(
                    UserData(
                        user.id, edtName.text.toString(),
                        edtNumber.text.toString()
                    )
                )
                if (update) {
                    Toast.makeText(this@UpdateContactActivity, "Updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}