package com.example.formula1.AppComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.R
import com.example.formula1.RaceList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Header (logo/banner image)
@Composable
fun HomeHeaderComposable(painter: Painter) {
    Row(Modifier.fillMaxWidth().padding(bottom = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painter,
            contentDescription = "Rotated Image",
            modifier = Modifier.height(65.dp)
        )
    }
}

// Daily tip card
@Composable
fun DailyTipComposable(painter: Painter, tip: String) {
    Box(
        modifier = Modifier
            .height(225.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .border(1.dp, Color.White.copy(0.25f), RoundedCornerShape(25.dp))
            .background(Color.Transparent)
    ) {
        // Background image
        Image(
            painter = painter,
            contentDescription = "Rotated Image",
            modifier = Modifier
                .matchParentSize()
                .alpha(0.25f)
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )
        // Foreground text
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = tip, fontSize = 24.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.russo_one_regular)), textAlign = TextAlign.Center)
        }
    }
}

// Next race card
@Composable
fun NextRaceComposable(nextRace: RaceList?) {
    Box(
        modifier = Modifier
            .height(275.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .border(1.dp, Color.White.copy(0.25f), RoundedCornerShape(25.dp))
            .background(Color.Transparent)
    ) {
        // Background image
        Image(
            painter = painterResource(R.drawable.f1_next_race_image),
            contentDescription = "Rotated Image",
            modifier = Modifier
                .matchParentSize()
                .alpha(0.25f) // Set opacity to 25%
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.matchParentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(75.dp))
            // Round number
            Text(text = "ROUND ${nextRace!!.round}", fontSize = 24.sp, color = Color.Red,fontFamily = FontFamily(Font(R.font.russo_one_regular)))
            // Race name
            Text(text = nextRace.raceName, fontSize = 24.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.russo_one_regular)))
            // Year overlay
            Box {
                Text(
                    text = "2025",
                    fontSize = 24.sp,
                    color = Color.Red,
                    fontFamily = FontFamily(Font(R.font.russo_one_regular)),
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = 1.1f
                            scaleY = 1.1f
                        }
                )
                Text(
                    text = "2025",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.russo_one_regular))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer bar with race and date
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(
                        color = Color(0xFF3D2A2A).copy(0.85f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(0.25f),
                        shape = RoundedCornerShape(25.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedDate = LocalDate.parse(nextRace.date).format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                // First text
                Text(text = nextRace.raceName, fontSize = 16.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.dmsans_medium)), modifier = Modifier.padding(horizontal = 16.dp))
                // Second text
                Text(text = formattedDate, fontSize = 16.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.dmsans_medium)), modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun SelectStandingsComposable(selectedOption: MutableState<String>) {
    Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drivers Standing Option
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { selectedOption.value = "Drivers" }
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Drivers Standing",
                color = if (selectedOption.value == "Drivers") Color.White else Color.White.copy(alpha = 0.5f),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.russo_one_regular))
            )
            Spacer(Modifier.height(5.dp))
            if (selectedOption.value == "Drivers") {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(205.dp)
                        .background(Color.White)
                )
            }
        }

        // Constructors Standing Option
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { selectedOption.value = "Constructors" }
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "Constructors Standing",
                color = if (selectedOption.value == "Constructors") Color.White else Color.White.copy(alpha = 0.5f),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.russo_one_regular))
            )
            Spacer(Modifier.height(2.dp))
            if (selectedOption.value == "Constructors") {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(274.dp)
                        .background(Color.White)
                )
            }
        }
    }
}




@Composable
fun StandingsItem(position: String, nationality: Painter, firstName: String, lastName:String, points: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .height(35.dp)
            .background(color = Color(0xFF3D2A2A).copy(0.85f), shape = RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Color.White.copy(0.25f),
                shape = RoundedCornerShape(25.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
         //Position
        Text(text = position, fontSize = 16.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.dmsans_medium)), modifier = Modifier.padding(start = 16.dp).padding(end = 4.dp).width(24.dp))
         //Nationality
        Image(
            painter = nationality,
            contentDescription = "Nationality Image",
            modifier = Modifier
                .width(24.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        //Name
        Text(
            text = AnnotatedString.Builder().apply {
                withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.dmsans_medium)))) {
                    append(firstName)
                }
                append(" ")
                withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.dmsans_bold)))) {
                    append(lastName)
                }
            }.toAnnotatedString(),
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 18.dp).weight(1f)
        )

        Box(
            modifier = Modifier
                .padding(3.dp)
                .wrapContentWidth()
                .fillMaxHeight()
                .background(
                    color = Color(0xFFD9D9D9).copy(0.3f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Points
            Text(text = "$points PTS", fontSize = 16.sp, color = Color.White,fontFamily = FontFamily(Font(R.font.dmsans_medium)), textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 12.dp))
        }
    }

}

