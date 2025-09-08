package com.example.formula1

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.EmailOutlinedTextField
import com.example.formula1.AppComponents.LoginAndRegisterButton
import com.example.formula1.AppComponents.LoginFooterComposable
import com.example.formula1.AppComponents.LoginHeaderComposable
import com.example.formula1.AppComponents.MediumText
import com.example.formula1.AppComponents.NameOutlinedTextField
import com.example.formula1.AppComponents.PasswordOutlinedTextField
import com.example.formula1.AppComponents.RegistrationCompleteDialogBox
import com.example.formula1.AppComponents.RegularText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException


class RegisterScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen()
        }
    }
}

@Composable
fun RegisterScreen() {
    // Declaring variables
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var progress by remember { mutableStateOf(0f) }

    // Error states for input fields
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var uniqueEmailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var errorMessageText by remember { mutableStateOf("") }

    // Registration complete variable
    var registrationComplete by remember { mutableStateOf(false) }


    fun registerButtonClicked() {
        val passwordValue = password.value
        val confirmPasswordValue = confirmPassword.value

        // Reset error messages
        errorMessageText = ""
        nameError = false
        emailError = false
        uniqueEmailError = false
        passwordError = false
        confirmPasswordError = false

        // Name validation
        if (name.isBlank()) {
            nameError = true
        }

        // Email validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = true
        }

        // Password and confirm password empty validation
        if (passwordValue.isBlank()) {
            passwordError = true
        }
        if (confirmPasswordValue.isBlank()) {
            confirmPasswordError = true
        }

        // Password validation
        if (passwordValue.length <= 8) {
            passwordError = true
            errorMessageText += "At least 8 characters in length."
        }

        if (!passwordValue.any { it.isUpperCase() }) {
            passwordError = true
            if (errorMessageText != "") errorMessageText += "\n"
            errorMessageText += "Contains an uppercase character."
        }

        if (passwordValue != confirmPasswordValue) {
            passwordError = true
            confirmPasswordError = true
            if (errorMessageText != "") errorMessageText += "\n"
            errorMessageText += "Passwords must match."
        }

        // Resetting passwords if they're incorrect
        if (errorMessageText != "") {
            password.value = ""
            confirmPassword.value = ""
            return
        } else {
            errorMessageText = ""
        }

        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, passwordValue)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    name = ""
                    email = ""
                    password.value = ""
                    confirmPassword.value = ""
                    registrationComplete = true
                } else {
                    task.exception?.let {
                        password.value = ""
                        confirmPassword.value = ""
                        when (it) {
                            is FirebaseAuthUserCollisionException -> {
                                // email already registered
                                emailError = true
                                uniqueEmailError = true
                            }

                            else -> {
                                // Some other error occurred
                                println("Registration failed: ${it.message}")
                                progress = 0f
                            }
                        }
                    }
                    progress = 0f
                }
            }
    }

    Column(Modifier.fillMaxSize().background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(75.dp))
        fun loginTextClicked() {
            val intent = Intent(context, LoginScreenActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                context,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            context.startActivity(intent, options.toBundle())
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
            (context as? Activity)?.apply { finish() }
        }
        LoginHeaderComposable(R.drawable.f1_register_icon) { returnButtonClicked() }
        Spacer(Modifier.height(50.dp))
        // Register text
        MediumText("Register", 48.sp, Color.Red)
        Spacer(Modifier.height(15.dp))
        // Login to your account text
        RegularText("Create your new account", 16.sp, colorResource(R.color.secondary_text))
        Spacer(Modifier.height(50.dp))
        Column(Modifier.width(500.dp)) {
            NameOutlinedTextField(name, onValueChange = { name = it }, isError = nameError)
            // Name error handling
            if (nameError) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.exclamation_red_icon),
                        contentDescription = "Exclamation Mark",
                        modifier = Modifier.size(16.dp)
                    )
                    RegularText(text = "Name cannot be empty.", color = Color.Red, fontSize = 16.sp)
                }

            }
            Spacer(Modifier.height(25.dp))
            EmailOutlinedTextField(email, onValueChange = { email = it }, isError = emailError)
            // Email error handling
            if (emailError) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.exclamation_red_icon),
                        contentDescription = "Exclamation Mark",
                        modifier = Modifier.size(16.dp)
                    )
                    RegularText(text = if (!uniqueEmailError) "Please enter a valid email address." else "This email address is already taken.", color = Color.Red, fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(25.dp))
            PasswordOutlinedTextField(password, placeholder = "Password", isError = passwordError)
            Spacer(Modifier.height(25.dp))
            PasswordOutlinedTextField(confirmPassword, placeholder = "Confirm Password", isError = confirmPasswordError)
            // Display error messages dynamically
            if (errorMessageText.isNotBlank()) {
                Spacer(Modifier.height(5.dp))
                Column(
                    modifier = Modifier.background(Color.Red).fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(6.dp)) {
                        RegularText(text = "Your password must have:", color = Color.White, fontSize = 16.sp)
                    }
                    errorMessageText.split("\n").forEach { errorLine ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(6.dp) // Spacing between error messages
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.error_white_icon),
                                contentDescription = "Exclamation Mark",
                                modifier = Modifier.size(12.dp)
                            )
                            RegularText(text = errorLine, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(50.dp))
            LoginAndRegisterButton("Register", cornerRadius = 25.dp, opacity = 1f) { registerButtonClicked() }
            Spacer(Modifier.height(75.dp))
            Row(Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                LoginFooterComposable(firstText = "Already have an account?", secondText = "Login", textClicked = { loginTextClicked() })
            }
        }
    }


    if (registrationComplete) {
        RegistrationCompleteDialogBox(onContinue = { registrationComplete = false })
    }

}

@Preview(showBackground = true, device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}


