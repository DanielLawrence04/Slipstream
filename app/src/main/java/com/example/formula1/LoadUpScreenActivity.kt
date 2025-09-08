package com.example.formula1

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.PathMeasure
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.lang.Math.toDegrees
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoadUpScreen()
        }
    }
}

@Composable
fun LoadUpScreen() {
    // variables
    var showLoadingBar by remember { mutableStateOf(false) }
    var animateDots by remember { mutableStateOf(true) }
    val context = LocalContext.current
    // media player for the f1 sound
    val mediaPlayer: MediaPlayer? = remember { MediaPlayer.create(context, R.raw.f1_sound) }

    // Play the formula 1 sound
    LaunchedEffect(Unit) {
        delay(250)
        mediaPlayer?.seekTo(950)
        mediaPlayer?.start() // Play sound
        delay(7000)
        mediaPlayer?.release() // Release resources
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // F1 red logo
        Image(
            painter = painterResource(id = R.drawable.f1_logo),
            contentDescription = "F1 Logo",
            modifier = Modifier
                .width(300.dp).height(200.dp)
        )
        // Box for the animation
        Box(modifier = Modifier.width(300.dp).height(500.dp)) {
            val pathProgress = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                pathProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 3000)
                )
            }
            Canvas(modifier = Modifier
                .matchParentSize()) {
                val startX = 44.dp.toPx()
                val startY = 326.dp.toPx()

                val abbeyX = startX + 86f
                val abbeyY = startY - 108f

                val farmX = abbeyX + 88f
                val farmY = abbeyY + 8f

                val villageX = farmX + 92f
                val villageY = farmY - 68f

                val theLoopX = villageX + 30f
                val theLoopY = villageY + 60f

                val turnAfterTheLoopX = theLoopX + 20f
                val turnAfterTheLoopY = theLoopY - 82f

                val wellingtonStraightX = turnAfterTheLoopX - 200f
                val wellingtonStraightY = turnAfterTheLoopY - 184f

                val endOfBrooklandsX = wellingtonStraightX - 38f
                val endOfBrooklandsY = wellingtonStraightY + 10f

                val startOfLuffieldX = endOfBrooklandsX - 4f
                val startOfLuffieldY = endOfBrooklandsY + 32f

                val endOfLuffieldX = startOfLuffieldX - 46f
                val endOfLuffieldY = startOfLuffieldY - 18f

                val woodcoteX = endOfLuffieldX + 88f
                val woodcoteY = endOfLuffieldY - 94f

                val startOfCopseX = woodcoteX + 180f
                val startOfCopseY = woodcoteY - 18f

                val endOfCopseX = startOfCopseX + 36f
                val endOfCopseY = startOfCopseY + 22f

                val point1AfterCopseX = endOfCopseX  + 18f
                val point1AfterCopseY = endOfCopseY + 70f

                val maggotsX = point1AfterCopseX  + 8f
                val maggotsY = point1AfterCopseY + 122f

                val beckettsTurn1X = maggotsX + 22f
                val beckettsTurn1Y = maggotsY + 38f

                val beckettsTurn2X = beckettsTurn1X - 2f
                val beckettsTurn2Y = beckettsTurn1Y + 98f

                val beckettsTurn3X = beckettsTurn2X + 2f
                val beckettsTurn3Y = beckettsTurn2Y + 46f

                val hangarStraightX = beckettsTurn3X - 202f
                val hangarStraightY = beckettsTurn3Y + 304f

                val stoweX = hangarStraightX - 68f
                val stoweY = hangarStraightY + 2f

                val startOfValeX = stoweX - 88f
                val startOfValeY = stoweY - 120f

                val endOfValeX = startOfValeX - 28f
                val endOfValeY = startOfValeY + 16f

                val clubX = endOfValeX - 50f
                val clubY = endOfValeY - 65f


                val pathStartToTurn1 = Path().apply {
                    moveTo(startX,startY)
                    lineTo(abbeyX,abbeyY)
                    cubicTo(abbeyX + 15f, abbeyY - 20f, abbeyX + 30f, abbeyY + 15,  farmX, farmY)
                    cubicTo(farmX + 25f, farmY + 3f, villageX - 5f, villageY - 20f, villageX + 12f, villageY + 20f)
                    cubicTo(theLoopX  + 15f, theLoopY + 80f, turnAfterTheLoopX + 30f, turnAfterTheLoopY + 30f, turnAfterTheLoopX, turnAfterTheLoopY)
                    lineTo(wellingtonStraightX,wellingtonStraightY + 10f)
                    cubicTo(wellingtonStraightX - 20f, wellingtonStraightY , endOfBrooklandsX, endOfBrooklandsY, endOfBrooklandsX, endOfBrooklandsY + 10f)
                    lineTo(startOfLuffieldX,startOfLuffieldY)
                    cubicTo(startOfLuffieldX - 5f, startOfLuffieldY + 70f, endOfLuffieldX - 18f, endOfLuffieldY + 45f, endOfLuffieldX, endOfLuffieldY + 10f)
                    quadraticTo(woodcoteX - 40f, woodcoteY + 15f, woodcoteX, woodcoteY + 10f)
                    lineTo(startOfCopseX,endOfCopseY)
                    quadraticTo(startOfCopseX + 70f, endOfCopseY + 5f, maggotsX + 5f, maggotsY)
                    quadraticTo(maggotsX + 5f, maggotsY + 20f, beckettsTurn1X + 5f, beckettsTurn1Y + 10f)
                    cubicTo(beckettsTurn1X + 22f, beckettsTurn1Y + 20f, beckettsTurn2X - 20f, beckettsTurn2Y - 10f,  beckettsTurn2X - 2f, beckettsTurn2Y)
                    quadraticTo(beckettsTurn2X + 40f, beckettsTurn2Y + 50f, beckettsTurn3X, beckettsTurn3Y + 15f)
                    quadraticTo(beckettsTurn3X - 45f, beckettsTurn3Y + 30f, beckettsTurn3X - 55f, beckettsTurn3Y + 60f)
                    lineTo(hangarStraightX,hangarStraightY)
                    quadraticTo(hangarStraightX - 40f, hangarStraightY + 60f, stoweX, stoweY)
                    cubicTo(stoweX - 50f, stoweY - 105f, stoweX - 30f, stoweY - 30f, startOfValeX, startOfValeY)
                    quadraticTo(startOfValeX - 10f, startOfValeY - 5f, endOfValeX, endOfValeY)
                    cubicTo(endOfValeX - 10f, endOfValeY + 25f, clubX - 15f, clubY + 20f, clubX, clubY)

                    lineTo(startX, startY)


                }

                val androidPath = android.graphics.Path().apply {
                    addPath(pathStartToTurn1.asAndroidPath()) // Convert Compose Path to Android Path
                }

                val pathMeasure = PathMeasure(androidPath, false)
                val pathLength = pathMeasure.length

                // Create a new path that will be drawn based on the progress of the animation
                val animatedPath = Path().apply {
                    pathMeasure.getSegment(0f, pathProgress.value * pathLength, this.asAndroidPath(), true)
                }

                // Draw the animated path
                drawPath(
                    path = animatedPath,
                    color = Color.Black,
                    style = Stroke(width = 4.dp.toPx())
                )

                // Get the current position along the path
                val pos = FloatArray(2) // To store position (x, y)
                val tan = FloatArray(2) // To store tangent (direction)
                val currentPathProgress = pathProgress.value * pathLength

                if (pathProgress.value < 1f && pathMeasure.getPosTan(currentPathProgress, pos, tan)) {
                    val angleInRadians = atan2(tan[1], tan[0]).toDouble()
                    val angleInDegrees = toDegrees(angleInRadians).toFloat()

                    // Rotate the image according to the path direction
                    rotate(degrees = angleInDegrees, pivot = Offset(pos[0], pos[1])) {
                        drawCircle(
                            color = Color.Red,
                            radius = 10f,
                            center = Offset(pos[0], pos[1])
                        )
                    }
                }

            }
        }
        // Show loading bar after animation complete, then move to login/register screen
        LaunchedEffect(Unit) {
            delay(3000)
            showLoadingBar = true
            delay(2000)
            animateDots = false

            // Intent to start login/register screen
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
        Spacer(Modifier.height(100.dp))
        // Showing loading bar
        Box(
            modifier = Modifier
                .height(30.dp)
        ) {
            if (showLoadingBar) DotsFlashing(animate = animateDots)
        }
        // Space from bottom
        Spacer(Modifier.height(70.dp))
    }
}

@Preview(showBackground = true, device = "spec:width=800dp,height=1280dp,dpi=240")
@Composable
fun LoadUpScreenPreview() {
    LoadUpScreen()
}

@Composable
fun DotsFlashing(animate: Boolean) {
    val minAlpha = 0.1f
    val numberOfDots = 5
    val dotSize = 25.dp
    val dotColor: Color = Color.Red
    val delayUnit = 250
    val duration = numberOfDots * delayUnit
    val spaceBetween = 2.dp

    @Composable
    fun Dot(alpha: Float) = Spacer(
        Modifier
            .size(dotSize)
            .alpha(alpha)
            .background(
                color = dotColor, shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition(label = "")

    @Composable
    fun animateAlphaWithDelay(delay: Int): State<Float> {
        return if (animate) {
            infiniteTransition.animateFloat(
                initialValue = minAlpha,
                targetValue = minAlpha,
                animationSpec = infiniteRepeatable(animation = keyframes {
                    durationMillis = duration
                    minAlpha at delay using LinearEasing
                    1f at delay + delayUnit using LinearEasing
                    minAlpha at delay + duration
                }), label = ""
            )
        } else {
            remember { mutableFloatStateOf(1f) }
        }
    }

    val alphas = arrayListOf<State<Float>>()
    for (i in 0 until numberOfDots) {
        alphas.add(animateAlphaWithDelay(delay = i * delayUnit))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        alphas.forEach {
            Dot(it.value)
            Spacer(Modifier.width(spaceBetween))
        }
    }
}