package com.example.bitirme_projesi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.bitirme_projesi.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
//import kotlinx.android.synthetic.main.activity_main.emailText
//import kotlinx.android.synthetic.main.activity_main.passWordText

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currUser = auth.currentUser
        if (currUser != null){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    fun register(view: View) {

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        /*val email = binding.emailText.text.toString()
        val password = binding.passWordText.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                //diğer aktiviteye git
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
        }*/

    }

    fun login(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passWordText.text.toString()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){

                Toast.makeText(this, "Hoş Geldiniz: ${email}",Toast.LENGTH_LONG ).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }

    }


}