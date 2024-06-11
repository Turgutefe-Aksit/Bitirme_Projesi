package com.example.bitirme_projesi.View
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bitirme_projesi.R
import com.example.bitirme_projesi.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun Registration(view: View){
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val Nickname = binding.editTextText.text.toString()

        /*if (Nickname.isNullOrEmpty()){
            Toast.makeText(applicationContext,"Kullanıcı adı boş olamaz.",Toast.LENGTH_SHORT).show()
        }
        if (password.isNullOrEmpty()){
            Toast.makeText(applicationContext,"Parola boş olamaz.",Toast.LENGTH_SHORT).show()
        }
        if (email.isNullOrEmpty()){
            Toast.makeText(applicationContext,"E-posta boş olamaz.",Toast.LENGTH_SHORT).show()
        }*/
        if (Nickname.isNullOrEmpty()){
            Toast.makeText(applicationContext,"Kullanıcı adı boş olamaz.",Toast.LENGTH_SHORT).show()
        }
        else if (password.isNullOrEmpty()){
            Toast.makeText(applicationContext,"Şifre boş olamaz.",Toast.LENGTH_SHORT).show()
        }
        else if (email.isNullOrEmpty()){
            Toast.makeText(applicationContext,"E-posta adresi boş olamaz.",Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){

                    /*val ref = FirebaseDatabase.getInstance().getReference("/users/$currUserID")
                    val user = UserInfo(currUserID.toString(),email.toString(), Nickname.toString())
                    ref.setValue(user).addOnSuccessListener {

                    }*/
                    val currUserID = auth.currentUser!!.uid

                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("NickName",Nickname)
                    postHashMap.put("Email",email)
                    postHashMap.put("UserID",currUserID)

                    //database.collection("Users").add(postHashMap)
                    database.collection("users").document("$currUserID").collection("info").add(postHashMap)


                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }


        }


    }
}
