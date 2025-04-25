package com.vozmediano.f1trivia

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {

    val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.i("LoginActivity", "Sign-in result received")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.i("LoginActivity", "Sign-in successful: ${account.id}")
                setResult(Activity.RESULT_OK)
                doSthWithAccount(account) // 👈 This is where you handle the account
            } catch (e: ApiException) {
                Log.e("LoginActivity", "Sign-in failed: ${e.statusCode}")
                setResult(Activity.RESULT_CANCELED) // 👈 This!
            }
        } else {
            Log.i("LoginActivity", "Sign-in canceled or failed")
            setResult(Activity.RESULT_CANCELED) // 👈 Also handle cancelation
        }
        finish()
    }

    private fun doSthWithAccount(account: GoogleSignInAccount?) {
        // Handle the signed-in account here
        Log.i("LoginActivity", "Account: ${account?.displayName}")
        // You can also save the account information to SharedPreferences or a database
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LoginActivity", "onCreate called")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()
        Log.i("LoginActivity", "GoogleSignInOptions created")

        val googleSignInClient = GoogleSignIn.getClient(this, gso)


        // To launch sign-in
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }
}