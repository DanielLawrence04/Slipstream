package com.example.formula1

import ModellingLoadingScreen
import SimulateModelScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.RussoOneText
import com.example.formula1.AppComponents.SimulationsBoxComposable

class SimulateHomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SimulateHomeScreen(setQualifyingActive: (Boolean) -> Unit) {

    // Controls which screen is currently visible
    val currentScreen = remember { mutableStateOf("home") }

    // Temporary UI states for transitioning to modelling screen
    var isDimmed by remember { mutableStateOf(false) }
    var pendingModelNavigation by remember { mutableStateOf(false) }

    // Main scrollable column of simulation options
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp).padding(top = 32.dp, bottom = 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header label
        RussoOneText(text = "Simulations", fontSize = 24.sp, color = Color.White)
        Spacer(Modifier.height(15.dp))

        // Qualifying simulation card
        SimulationsBoxComposable(
            backgroundImage = painterResource(R.drawable.qualifying_image),
            titleText = "Qualifying",
            bottomLeftText = "Simulate a 2025 Formula 1 Qualifying Session",
            onButtonClick = {
                currentScreen.value = "qualifying"
                setQualifyingActive(true) // Hides navigation bar
            }
        )

        Spacer(Modifier.height(20.dp))

        // Race strategy simulation card
        SimulationsBoxComposable(
            backgroundImage = painterResource(R.drawable.pit_stop_image),
            titleText = "Race",
            bottomLeftText = "Simulate a 2025 Formula 1 Qualifying Race",
            onButtonClick = {
                currentScreen.value = "strategy"
                setQualifyingActive(true)
            }
        )

        Spacer(Modifier.height(20.dp))

        // 3D Modelling simulation card
        SimulationsBoxComposable(
            backgroundImage = painterResource(R.drawable.f1_car_model_image),
            titleText = "Model",
            bottomLeftText = "Simulate Formula 1 Car Models",
            onButtonClick = {
                setQualifyingActive(true)
                isDimmed = true                // Trigger loading overlay
                pendingModelNavigation = true // Mark navigation to 3D as pending
            }
        )

        Spacer(Modifier.height(20.dp))
    }

    // Triggered after dimming starts — ensures smooth transition into modelling screen
    LaunchedEffect(isDimmed) {
        if (isDimmed && pendingModelNavigation) {
            currentScreen.value = "modelling"
            isDimmed = false
            pendingModelNavigation = false
        }
    }

    // Optional loading overlay shown while entering 3D scene
    if (isDimmed) {
        ModellingLoadingScreen()
    }

    // Qualifying screen with slide-in animation
    AnimatedVisibility(
        visible = currentScreen.value == "qualifying",
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
    ) {
        SimulateQualifyingScreen(
            onExit = {
                currentScreen.value = "home"
                setQualifyingActive(false)
            }
        )
    }

    // Race strategy screen with identical transition
    AnimatedVisibility(
        visible = currentScreen.value == "strategy",
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
    ) {
        SimulateStrategy(
            onExit = {
                currentScreen.value = "home"
                setQualifyingActive(false)
            }
        )
    }

    // 3D Modelling screen, activated after brief loading delay
    AnimatedVisibility(
        visible = currentScreen.value == "modelling",
        enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)),
        exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(500))
    ) {
        SimulateModelScreen(
            onExit = {
                currentScreen.value = "home"
                setQualifyingActive(false)
            }
        )
    }
}

