package com.example.formula1.AppComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.R

@Composable
fun NavigationBar(selectedItem: String, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF402625))
            .drawBehind {
                // Draw the top border
                drawRect(
                    color = Color.White.copy(0.6f),
                    size = Size(
                        width = size.width,
                        height = 1.dp.toPx() // Border width
                    ),
                    topLeft = Offset(x = 0f, y = 0f) // Position at the top
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        ) {
        if (selectedItem == "Home") {
            NavBarItem(
                icon = painterResource(R.drawable.home_icon_filled),
                label = "Home",
                onClick = { onClick("Home") }
            )
        } else {
            NavBarItem(
                icon = painterResource(R.drawable.home_icon_outline),
                label = "Home",
                onClick = { onClick("Home") }
            )
        }
        Spacer(Modifier.width(20.dp))
        if (selectedItem == "Simulate") {
            NavBarItem(
                icon = painterResource(R.drawable.cube_filled),
                label = "Simulate",
                onClick = { onClick("Simulate") }
            )
        } else {
            NavBarItem(
                icon = painterResource(R.drawable.cube_outline),
                label = "Simulate",
                onClick = { onClick("Simulate") }
            )
        }
        Spacer(Modifier.width(20.dp))
        if (selectedItem == "Settings") {
            NavBarItem(
                icon = painterResource(R.drawable.settings_icon_filled),
                label = "Settings",
                onClick = { onClick("Settings") }
            )
        } else {
            NavBarItem(
                icon = painterResource(R.drawable.settings_icon_outline),
                label = "Settings",
                onClick = { onClick("Settings") }
            )
        }
    }
}

@Composable
fun NavBarItem(icon: Painter, label: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() } // Remove ripple effect
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(vertical = 10.dp)
            .clickable(
                onClick = onClick,
                indication = null, // Remove default ripple
                interactionSource = interactionSource // No focus effect
            )
    ) {
        Image(
            painter = icon,
            contentDescription = "Navbar icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        RegularText(text = label, fontSize = 14.sp, color = Color.White)
    }
}