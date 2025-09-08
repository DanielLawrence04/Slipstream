package com.example.formula1

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.LoginAndRegisterButton

class LoginOrRegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if the user is already logged in
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            loginSuccess(this) // Navigate to MainScreenActivity if logged in
            return
        }
        enableEdgeToEdge()
        setContent {
            LoginOrRegisterScreen()
        }
    }
}

@Composable
fun LoginOrRegisterScreen() {
    val context = LocalContext.current
    fun loginButtonPressed() {
        val intent = Intent(context, LoginScreenActivity::class.java)
        context.startActivity(intent)
        // Finish current activity
        (context as? android.app.Activity)?.apply {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    fun registerButtonPressed() {
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
    // Background image
    Image(
        painter = painterResource(id = R.drawable.f1_login_or_register_background),
        contentDescription = "F1 background",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top-left text
        Text(
            text = "Quicker Laps, Smarter Strategies.",
            style = TextStyle(
                fontSize = 56.sp,
                color = Color.White,
                fontFamily = FontFamily(
                    Font(R.font.dmsans_regular)
                )
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(500.dp)
                .padding(50.dp)
        )

        // Center elements: button and other text
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginAndRegisterButton(text = "Login", cornerRadius = 10.dp, opacity = 0.75f) { loginButtonPressed() }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Create an account",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(
                        Font(R.font.dmsans_medium)
                    )
                ), modifier = Modifier.clickable { registerButtonPressed() }
            )
            Spacer(Modifier.height(50.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginOrRegisterScreenPreview() {
    LoginOrRegisterScreen()
}