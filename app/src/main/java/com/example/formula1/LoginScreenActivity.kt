package com.example.formula1

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.ContinueWithComposable
import com.example.formula1.AppComponents.EmailOutlinedTextField
import com.example.formula1.AppComponents.GoogleSignInButton
import com.example.formula1.AppComponents.LoginAndRegisterButton
import com.example.formula1.AppComponents.LoginFooterComposable
import com.example.formula1.AppComponents.LoginHeaderComposable
import com.example.formula1.AppComponents.MediumText
import com.example.formula1.AppComponents.PasswordOutlinedTextField
import com.example.formula1.AppComponents.RegularText
import com.example.formula1.AppComponents.RememberMeRow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginScreenActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("722339274943-i1v20pm0h5g8msu6ks48lhrsis484vd3.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        fun firebaseAuthWithGoogle(idToken: String?) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        loginSuccess(context = this@LoginScreenActivity)
                    } else {
                        // Sign in fail
                        runOnUiThread {
                            Toast.makeText(this@LoginScreenActivity, "Google sign-in failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
        }

        // Google Sign-In intent launcher
        val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                if (e.statusCode == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
                    Log.w("GOOGLESIGNIN", "Google sign-in was canceled by the user.")
                } else {
                    Log.w("GOOGLESIGNIN", "Google sign-in failed: ${e.statusCode}", e)
                }
            }
        }

        // Function to launch Google Sign-In
        fun signInWithGoogle() {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                signInLauncher.launch(signInIntent)
            }
        }

        enableEdgeToEdge()
        setContent {
            LoginScreen(auth = auth, signInWithGoogle = { signInWithGoogle() })
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth, signInWithGoogle: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var rememberMeChecked by remember { mutableStateOf(true) }

    fun onLoginButtonClicked() {
        if (email.isNotEmpty() && password.value.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Save login state if "Remember Me" is checked
                        if (rememberMeChecked) {
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        }
                        loginSuccess(context)
                    } else {
                        // Login failed
                        loginErrorMessage = task.exception?.localizedMessage ?: "Login failed. Please try again."
                    }
                }
        } else {
            // Email or password empty
            loginErrorMessage = "Email and password must not be empty."
        }
    }

    Column(Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(75.dp))
        fun registerTextClicked() {
            val intent = Intent(context, RegisterScreenActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                context,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            context.startActivity(intent, options.toBundle())
            // Finish activity so you cannot return
            (context as? android.app.Activity)?.apply {finish()}
        }
        fun returnButtonClicked() {
            val intent = Intent(context, LoginOrRegisterActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                context,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            context.startActivity(intent, options.toBundle())
            // Finish activity so you cannot return
            (context as? android.app.Activity)?.apply {finish()}
        }
        LoginHeaderComposable(R.drawable.f1_login_icon) { returnButtonClicked() }
        Spacer(Modifier.height(50.dp))
        // Welcome back text
        MediumText("Welcome Back", 48.sp, Color.Red)
        Spacer(Modifier.height(15.dp))
        // Login to your account text
        RegularText("Login to your account", 16.sp, colorResource(R.color.secondary_text))
        Spacer(Modifier.height(50.dp))
        Column(Modifier.width(500.dp)) {
            EmailOutlinedTextField(email, onValueChange = { email = it }, isError = false)
            Spacer(Modifier.height(25.dp))
            PasswordOutlinedTextField(password, placeholder = "Password", isError = false)
            Spacer(Modifier.height(20.dp))
            RememberMeRow { isChecked -> rememberMeChecked = isChecked }
            Spacer(Modifier.height(25.dp))
            LoginAndRegisterButton("Login", cornerRadius = 25.dp, opacity = 1f) { onLoginButtonClicked() }
            loginErrorMessage?.let {
                Spacer(Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RegularText(text = it, fontSize = 14.sp, color = Color.Red)
                }
            }
            Spacer(Modifier.height(50.dp))
            ContinueWithComposable()
            Spacer(Modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                GoogleSignInButton {signInWithGoogle()}
            }
            Spacer(Modifier.height(75.dp))
            Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                LoginFooterComposable(firstText = "Don't have an account?", secondText = "Sign up", textClicked = { registerTextClicked() })
            }
        }
    }
}

fun loginSuccess(context: Context) {
    // Login successful
    val intent = Intent(context, MainScreenActivity::class.java)
    val options = ActivityOptions.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )
    context.startActivity(intent, options.toBundle())
    // Finish activity so you cannot return
    (context as? android.app.Activity)?.apply {finish()}
}