package com.example.dotalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotalk.databinding.ActivityMainBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar : ProgressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()

        adapter = UserAdapter(this,userList)

        //For showing the list of peoples
        userRecyclerView = binding.userRecyclerView //findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot is used exactly to get the data from the database

                userList.clear()  // for clearing the previous list
                for(postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(mAuth.currentUser?.uid!=currentUser?.uid)
                        userList.add(currentUser!!)
                }
                progressBar.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(p0: DatabaseError) {
//                TODO "Not yet implemented"

            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            //logic for logout
            mAuth.signOut()
            val intent = Intent(this@MainActivity, LogIn::class.java)

            finish()
            startActivity(intent)
            return true
        }

        return true
    }
}