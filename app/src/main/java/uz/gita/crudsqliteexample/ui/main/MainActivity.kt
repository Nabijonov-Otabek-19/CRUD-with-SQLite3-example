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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var list: ArrayList<UserData>
    private lateinit var myDB: MyDatabase
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDB = MyDatabase(this)

        binding.apply {

            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recycler.setHasFixedSize(true)
            loadUsers()

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
                list.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(binding.recycler, "Removed", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        list.add(position, deletedCourse)
                        adapter.notifyItemInserted(position)
                    }.show()
            }
        }).attachToRecyclerView(binding.recycler)
    }

    private fun loadUsers() {
        val cursor: Cursor? = myDB.getUser()
        list = ArrayList()
        while (cursor!!.moveToNext()) {
            val name = cursor.getString(0)
            val number = cursor.getString(1)
            list.add(UserData(name, number))
        }

        binding.apply {
            adapter = MyAdapter(list)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}