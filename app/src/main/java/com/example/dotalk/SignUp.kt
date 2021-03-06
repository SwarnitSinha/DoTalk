 package com.example.dotalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dotalk.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

 class SignUp : AppCompatActivity() {
     private lateinit var edtName:EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var binding: ActivitySignUpBinding


     private lateinit var mAuth: FirebaseAuth
     private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        edtName = binding.edtName//findViewById(R.id.edt_name)
        edtEmail = binding.edtEmail//findViewById(R.id.edt_email)
        edtPassword = binding.edtPass//findViewById(R.id.edt_pass)
        btnSignUp = binding.btnSignup //findViewById(R.id.btn_signup)


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
                        Toast.makeText(this@SignUp,"Some error occurred. Try again!",Toast.LENGTH_SHORT).show()
                 }
             }
     }

     private fun addUserToDatabase(name: String, email: String, uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

         //child will add a node to database
         mDbRef.child("user").child(uid).setValue(User(name,email, uid))

     }
}