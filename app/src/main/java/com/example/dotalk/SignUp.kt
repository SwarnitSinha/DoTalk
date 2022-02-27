 package com.example.dotalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

 class SignUp : AppCompatActivity() {
     private lateinit var edtName:EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
     private lateinit var mAuth: FirebaseAuth

     private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_pass)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            //Now for creating a new user
            signUp(name,email,password)
        }
    }
     private fun signUp(name: String,email : String, password: String){
         //Logic of creating new User
         mAuth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener(this) { task ->
                 if (task.isSuccessful) {
                     //add user to database
                         addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                     // -> Home activity
                     Intent(this@SignUp,MainActivity::class.java).also {
                         finish()
                         startActivity(it)

                     }
                 } else {
                        Toast.makeText(this@SignUp,"Some error occured. Try again!",Toast.LENGTH_SHORT).show()
                 }
             }
     }

     private fun addUserToDatabase(name: String, email: String, uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

         mDbRef.child("user").child(uid).setValue(User(name,email, uid))

     }
}