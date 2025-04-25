package com.vozmediano.f1trivia.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.R
import com.vozmediano.f1trivia.ui.MainMenu.MainMenuActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var usernameLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("LoginActivity", "onCreate called")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        Log.i("LoginActivity", "GoogleSignInOptions created")

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.i("LoginActivity", "Sign-in result received")
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.i("LoginActivity", "Sign-in successful: ${account.id}")
                    authenticateWithFirebase(account)
                } catch (e: ApiException) {
                    Log.e("LoginActivity", "Sign-in failed: ${e.statusCode}")
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            } else {
                Log.i("LoginActivity", "Sign-in canceled or failed")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        usernameLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Username was saved successfully
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // User canceled or there was an error in UsernameActivity
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        // Launch sign-in
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun authenticateWithFirebase(account: GoogleSignInAccount?) {
        if (account == null) {
            Log.e("LoginActivity", "Google account is null")
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        Log.i("LoginActivity", "Authenticating with Firebase: ${account.id}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.i("LoginActivity", "Firebase sign-in successful")
                val user = authResult.user
                user?.uid?.let { uid ->
                    FirebaseFirestore.getInstance().collection("users").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists() && document.contains("username")) {
                                val username = document.getString("username")
                                Log.i("LoginActivity", "Username found: $username")
                                // Proceed directly to MainMenuActivity
                                val intent = Intent(this, MainMenuActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.i("LoginActivity", "Username not found, navigating to UsernameActivity")
                                // Navigate to UsernameActivity to get the username
                                val intent = Intent(this, UsernameActivity::class.java)
                                usernameLauncher.launch(intent)
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("LoginActivity", "Error checking for username: ${e.message}")
                            // Handle the error appropriately, perhaps still go to UsernameActivity
                            val intent = Intent(this, UsernameActivity::class.java)
                            usernameLauncher.launch(intent)
                        }
                }
            }
            .addOnFailureListener {
                Log.e("LoginActivity", "Firebase sign-in failed: ${it.message}")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
    }
}