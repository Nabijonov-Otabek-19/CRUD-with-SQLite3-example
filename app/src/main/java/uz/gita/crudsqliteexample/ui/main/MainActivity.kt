package uz.gita.crudsqliteexample.ui.main

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import uz.gita.crudsqliteexample.adapter.MyAdapter
import uz.gita.crudsqliteexample.databinding.ActivityMainBinding
import uz.gita.crudsqliteexample.db.MyDatabase
import uz.gita.crudsqliteexample.model.UserData
import uz.gita.crudsqliteexample.ui.add.AddContactActivity
import uz.gita.crudsqliteexample.ui.update.UpdateContactActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var list: ArrayList<UserData>
    private var myDB: MyDatabase = MyDatabase.getInstance(this)
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recycler.setHasFixedSize(true)

            btnAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddContactActivity::class.java))
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedCourse: UserData = list[position]
                myDB.deleteData(list[position])
                list.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(binding.recycler, "Removed", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        myDB.saveUserData(deletedCourse)
                        list.add(position, deletedCourse)
                        adapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun loadUsers() {
        val cursor: Cursor? = myDB.getUser()
        list = ArrayList()
        while (cursor!!.moveToNext()) {
            val id = cursor.getString(0)
            val name = cursor.getString(1)
            val number = cursor.getString(2)
            list.add(UserData(id.toInt(), name, number))
        }
        cursor.close()

        binding.apply {
            adapter = MyAdapter(list)
            recycler.adapter = adapter
            adapter.notifyItemInserted(list.size)
        }

        adapter.setOnItemClickListener {
            val inten = Intent(this, UpdateContactActivity::class.java)
            inten.putExtra("user", it)
            startActivity(inten)
        }
    }
}