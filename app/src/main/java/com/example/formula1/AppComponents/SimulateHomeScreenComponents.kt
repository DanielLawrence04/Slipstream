package com.example.formula1.AppComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.R
import com.example.formula1.RaceList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SimulationsBoxComposable(backgroundImage: Painter, titleText: String, bottomLeftText: String, onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.White.copy(0.25f), RoundedCornerShape(20.dp))
            .background(Color.Transparent)
    ) {
        // background image
        Image(
            painter = backgroundImage,
            contentDescription = "Rotated Image",
            modifier = Modifier
                .matchParentSize()
                .alpha(0.5f)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Background + Border wrapper
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF3D2A2A).copy(0.85f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(0.25f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(Modifier.padding(16.dp)) {
                    // Title
                    RussoOneText(text = titleText, fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Underline
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Footer row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RegularText(text = bottomLeftText, fontSize = 16.sp, color = Color.White)

                        // Simulate Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF6D1313))
                                .border(1.dp, Color.White.copy(0.25F), RoundedCornerShape(20.dp))
                                .clickable(onClick = { onButtonClick() })
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            RegularText(text = "Simulate Now", fontSize = 16.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}