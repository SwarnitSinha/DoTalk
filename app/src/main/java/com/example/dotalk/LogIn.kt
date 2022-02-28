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
import com.example.dotalk.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class   LogIn : AppCompatActivity() {

    //these are the views variable
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private lateinit var binding: ActivityLogInBinding

    //IMPORTANT instance of firebase authentication
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        //IMPORTANT Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        edtEmail = binding.edtEmail //findViewById(R.id.edt_email)
        edtPassword =binding.edtPass //findViewById(R.id.edt_pass)
        btnLogin = binding.btnLogin //findViewById(R.id.btn_login)
        btnSignUp = binding.btnSignup //findViewById(R.id.btn_signup)

        if(mAuth.currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        //this intent on click will redirect to signup page
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

                //now the email and password is sent to the login function
                login(email,password)
            }
        }
    }

    //IMPORTANT this function will verify if the user is valid or not

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