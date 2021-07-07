package com.ems.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private val mAuth = FirebaseAuth.getInstance()



    companion object {
        private const val RC_SIGN_IN = 1234
        private const val TAG = "MainActivityLog"


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginBtn.setOnClickListener {

            showProgressBar()
            signIn()
            hideProgressBar()
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, account.id ?: "null")
                signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, requestCode.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential).apply {
            addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "success")
                    val toVerses = Intent(this@MainActivity, VersesActivity::class.java)
                    startActivity(toVerses)
                } else {
                    Toast.makeText(this@MainActivity, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

            addOnFailureListener {
                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            val toVerses = Intent(this, VersesActivity::class.java)
            startActivity(toVerses)
        }
    }


    private fun showProgressBar() {
        loginProgressBar.visibility = ProgressBar.INVISIBLE
        loginBtn.isClickable = false
        var progressBar = findViewById(R.id.spin_kit) as ProgressBar
        val doubleBounce: Sprite = Wave()

        progressBar.indeterminateDrawable = doubleBounce


    }


    private fun hideProgressBar() {
        loginProgressBar.visibility = ProgressBar.INVISIBLE
        loginBtn.isClickable = true


    }
}