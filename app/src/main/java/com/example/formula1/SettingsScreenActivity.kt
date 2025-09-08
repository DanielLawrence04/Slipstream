package com.example.formula1

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.formula1.AppComponents.SettingsItem
import kotlin.system.measureTimeMillis

class SettingsScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

@Composable
fun SettingsScreen() {
    // Access the current Android context (needed for logout and navigation)
    val context = LocalContext.current

    // Function to handle logging out and returning to login screen
    fun logout(context: Context) {
        // Clear login state using shared preferences
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

        // Create intent to navigate back to login/register screen
        val intent = Intent(context, LoginOrRegisterActivity::class.java)

        // Apply fade in/out animation during transition
        val options = ActivityOptions.makeCustomAnimation(
            context,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        context.startActivity(intent, options.toBundle())

        // Finish current activity to prevent returning via back button
        (context as? android.app.Activity)?.apply { finish() }
    }

    // Main settings list layout
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Static list of configurable items
        SettingsItem(title = "Account") { /* Placeholder for future use */ }
        SettingsItem(title = "Car Setup Preferences") { /* Placeholder */ }
        SettingsItem(title = "Telemetry & Data Settings") { /* Placeholder */ }
        SettingsItem(title = "Race Strategy Settings") { /* Placeholder */ }
        SettingsItem(title = "Notification Preferences") { /* Placeholder */ }
        SettingsItem(title = "Subscription & Billing") { /* Placeholder */ }
        SettingsItem(title = "Accessibility Options") { /* Placeholder */ }
        SettingsItem(title = "Simulation Settings") { /* Placeholder */ }
        SettingsItem(title = "Connected Data Sources (APIs)") { /* Placeholder */ }
        SettingsItem(title = "Help & Support") { /* Placeholder */ }
        SettingsItem(title = "Legal & Privacy Policies") { /* Placeholder */ }

        // Final item: logs the user out and returns to login screen
        SettingsItem(title = "Sign Out") {
            logout(context)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}