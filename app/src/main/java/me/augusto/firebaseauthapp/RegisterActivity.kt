package me.augusto.firebaseauthapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = Firebase.auth

        val emailField = findViewById<EditText>(R.id.emailField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val registerButton = findViewById<Button>(R.id.registerBtn)

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(passwordField.text.toString().length < 6){
                    passwordField.error = "Must be at least 6 characters long"
                    registerButton.isEnabled = false
                }else{
                    registerButton.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        findViewById<TextView>(R.id.signinText).setOnClickListener { redirectToMainPage() }
        registerButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            if(validateConfirmPassword(password)){
                registerUser(email, password)
            }
        }
    }

    fun redirectToMainPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun validateConfirmPassword(password : String) : Boolean {
        val confirmPasswordView = findViewById<EditText>(R.id.confirmPasswordField)
        val confirmPassword = confirmPasswordView.text.toString()
        if(password != confirmPassword){
            confirmPasswordView.error = "Passwords must match"
            return false
        }
        return true
    }

    fun registerUser(email : String, password : String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    redirectToMainPage()
                }else{
                    Toast.makeText(baseContext, "Failed to register user.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}