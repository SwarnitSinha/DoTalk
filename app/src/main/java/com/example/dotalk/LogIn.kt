package com.example.dotalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.dotalk.MainActivity
import com.example.dotalk.R
import com.google.firebase.auth.FirebaseAuth

class   LogIn : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_pass)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener{
            Intent(this,SignUp::class.java).also{
                startActivity(it)
            }
        }
        btnLogin.setOnClickListener{
            if(edtEmail.text.isEmpty()||edtPassword.text.isEmpty()){
                Toast.makeText(this@LogIn,"Empty Input",Toast.LENGTH_SHORT).show()
            }
            else{
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()
                login(email,password)
            }
        }
    }

    private fun login(email:String,password: String){
//logic for logging in  user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for loggin in user
                    Intent(this@LogIn, MainActivity::class.java).also {
                        finish()
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(this@LogIn,"User doesn't exist",Toast.LENGTH_SHORT).show()
                }
            }
    }
}