import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formula1.AppComponents.ModellingHeaderComposable
import com.example.formula1.AppComponents.ModellingTeamHeaderComposable
import com.example.formula1.AppComponents.ModellingTeamSelectionComposable
import com.example.formula1.AppComponents.RussoOneText
import com.example.formula1.AppComponents.SwipeableModal
import com.example.formula1.R
import io.github.sceneview.Scene
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberOnGestureListener
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class SimulateModelScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }
}

// Data class holding static information for each F1 team
data class F1TeamAdditionalInformation(
    val name: String,
    @DrawableRes val logoRes: Int,
    val base: String,
    val teamChief: String,
    val techChief: String,
    val chassis: String,
    val powerUnit: String,
    val firstEntry: String,
    val worldChampionships: String,
    val highestRaceFinish: String,
    val polePositions: String,
    val fastestLaps: String
)

// Static list of team details (logos, names, stats)
val teams = listOf(
    F1TeamAdditionalInformation(
        name = "Oracle Red Bull Racing",
        logoRes = R.drawable.redbull_mini_logo,
        base = "Milton Keynes, United Kingdom",
        teamChief = "Christian Horner",
        techChief = "Pierre Waché",
        chassis = "RB21",
        powerUnit = "Honda RBPT",
        firstEntry = "1997",
        worldChampionships = "6",
        highestRaceFinish = "1 (x123)",
        polePositions = "105",
        fastestLaps = "99"
    ),
    F1TeamAdditionalInformation(
        name = "Mercedes-AMG PETRONAS Formula One Team",
        logoRes = R.drawable.mercedes_mini_logo,
        base = "Brackley, United Kingdom",
        teamChief = "Toto Wolff",
        techChief = "James Allison",
        chassis = "W16",
        powerUnit = "Mercedes",
        firstEntry = "1970",
        worldChampionships = "8",
        highestRaceFinish = "1 (x120)",
        polePositions = "133",
        fastestLaps = "101"
    ),
    F1TeamAdditionalInformation(
        name = "Scuderia Ferrari HP",
        logoRes = R.drawable.ferrari_mini_logo,
        base = "Maranello, Italy",
        teamChief = "Frédéric Vasseur",
        techChief = "Loic Serra / Enrico Gualtieri",
        chassis = "SF-25",
        powerUnit = "Ferrari",
        firstEntry = "1950",
        worldChampionships = "16",
        highestRaceFinish = "1 (x249)",
        polePositions = "253",
        fastestLaps = "263"
    ),
    F1TeamAdditionalInformation(
        name = "McLaren Formula 1 Team",
        logoRes = R.drawable.mclaren_mini_logo,
        base = "Woking, United Kingdom",
        teamChief = "Andrea Stella",
        techChief = "Peter Prodromou / Neil Houldey",
        chassis = "MCL39",
        powerUnit = "Mercedes",
        firstEntry = "1966",
        worldChampionships = "9",
        highestRaceFinish = "1 (x193)",
        polePositions = "167",
        fastestLaps = "176"
    ),
    F1TeamAdditionalInformation(
        name = "BWT Alpine Formula One Team",
        logoRes = R.drawable.alpine_mini_logo,
        base = "Enstone, United Kingdom",
        teamChief = "Oliver Oakes",
        techChief = "David Sanchez",
        chassis = "A525",
        powerUnit = "Renault",
        firstEntry = "1986",
        worldChampionships = "2",
        highestRaceFinish = "1 (x21)",
        polePositions = "20",
        fastestLaps = "16"
    ),
    F1TeamAdditionalInformation(
        name = "Aston Martin Aramco Formula One Team",
        logoRes = R.drawable.aston_martin_mini_logo,
        base = "Silverstone, United Kingdom",
        teamChief = "Andy Cowell",
        techChief = "Eric Blandin",
        chassis = "AMR25",
        powerUnit = "Mercedes",
        firstEntry = "2018",
        worldChampionships = "0",
        highestRaceFinish = "1 (x1)",
        polePositions = "1",
        fastestLaps = "3"
    )
)

// team names
val teamNames = listOf(
    "ORACLE RED BULL RACING",
    "MERCEDES-AMG PETRONAS F1 TEAM",
    "SCUDERIA FERRARI",
    "MCLAREN F1 TEAM",
    "BWT-ALPINE F1 TEAM",
    "ASTON MARTIN"
)
// team logos
val teamLogos = listOf(
    R.drawable.red_bull_logo,
    R.drawable.mercedes_logo,
    R.drawable.ferrari_logo,
    R.drawable.mclaren_logo,
    R.drawable.alpine_logo,
    R.drawable.aston_martin_logo
)
// team colours
val teamColours = listOf(
    Color(0xFF0D202F),
    Color(0xFF019C96),
    Color(0xFFFF0000),
    Color(0xFFFF7C00),
    Color(0xFF0070BB),
    Color(0xFF105C4C)
)

val driverPairs = listOf(
    "VER" to "TSU",
    "RUS" to "ANT",
    "LEC" to "HAM",
    "PIA" to "NOR",
    "GAS" to "DOO",
    "ALO" to "STR"
)

@Composable
fun SimulateModelScreen(onExit: () -> Unit) {
    // Track which team is currently selected by the user
    var selectedTeamIndex by remember { mutableIntStateOf(0) }

    // Set up the 3D rendering environment
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    // Central node that camera rotates around
    val centerNode = rememberNode(engine)

    // Camera definition, looking at central pivot point
    val cameraNode = rememberCameraNode(engine) {
        position = Position(y = 1f, z = 3f)
        lookAt(centerNode)
        centerNode.addChildNode(this)
    }

    // Continuous camera orbit rotation (360° loop)
    val cameraTransition = rememberInfiniteTransition(label = "CameraTransition")
    val cameraRotation by cameraTransition.animateRotation(
        initialValue = Rotation(y = 0.0f),
        targetValue = Rotation(y = 360.0f),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20.seconds.toInt(DurationUnit.MILLISECONDS))
        )
    )

    val redbullNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/red_bull.glb"
            ),
            centerOrigin = Position(y=0.31f,z=-0.05f),
            scaleToUnits = 1.5f
        )
    }
    val mercedesNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mercedes.glb"
            ),
            centerOrigin = Position(y=0.31f,z=0.05f),
            scaleToUnits = 1.5f
        )
    }
    val ferrariNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/ferrari.glb"
            ),
            centerOrigin = Position(y=0.31f,z=0.05f),
            scaleToUnits = 1.5f
        )
    }
    val mclarenNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/mclaren.glb"
            ),
            centerOrigin = Position(y=0.31f,z=-0.05f),
            scaleToUnits = 1.5f
        )
    }
    val alpineNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/alpine.glb"
            ),
            centerOrigin = Position(y=0.31f,z=-0.05f),
            scaleToUnits = 1.5f
        )
    }
    val astonmartinNode = rememberNode() {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = "models/aston_martin.glb"
            ),
            centerOrigin = Position(y=0.31f,z=-0.05f),
            scaleToUnits = 1.5f
        )
    }

    // Track currently displayed model
    val selectedCarNode = remember { mutableStateOf(redbullNode) }

    // Boolean to toggle modal visibility
    val showModal = remember { mutableStateOf(false) }

    // 3D scene and interface
    Box(modifier = Modifier.fillMaxSize()) {
        Scene(
            modifier = Modifier.fillMaxSize(),
            engine = engine,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            cameraManipulator = rememberCameraManipulator(
                orbitHomePosition = cameraNode.worldPosition,
                targetPosition = centerNode.worldPosition
            ),
            childNodes = listOf(
                centerNode,
                selectedCarNode.value!!,
                rememberNode {
                    // Podium beneath model for context
                    ModelNode(
                        modelInstance = modelLoader.createModelInstance("models/podium_white.glb"),
                        scaleToUnits = 1.65f
                    )
                }
            ),
            onFrame = {
                // Auto-rotate the car in sync with camera
                centerNode.rotation = cameraRotation
                cameraNode.lookAt(centerNode)
            },
            onGestureListener = rememberOnGestureListener(
                onSingleTapConfirmed = { _, _ -> showModal.value = true }
            ),
            environment = environmentLoader.createHDREnvironment("envs/environment.hdr")!!
        )

        // Overlay UI layered on top of the 3D content
        Column(Modifier.fillMaxSize()) {
            // Top header with return/back button
            ModellingHeaderComposable(onReturnButtonClicked = { onExit() })

            Spacer(Modifier.height(40.dp))

            Column(Modifier.fillMaxSize()) {
                // Display current team name
                ModellingTeamHeaderComposable(team = teamNames[selectedTeamIndex])

                Spacer(Modifier.height(750.dp)) // Space for full-screen 3D preview

                // Bottom carousel of team logos (acts as selector)
                ModellingTeamSelectionComposable(
                    teamLogos = teamLogos,
                    teamColours = teamColours,
                    onTeamSelected = { idx ->
                        selectedTeamIndex = idx
                        selectedCarNode.value = when (idx) {
                            0 -> redbullNode
                            1 -> mercedesNode
                            2 -> ferrariNode
                            3 -> mclarenNode
                            4 -> alpineNode
                            5 -> astonmartinNode
                            else -> selectedCarNode.value
                        }
                    }
                )
            }
        }

        // Pop-up modal that shows team statistics and details
        if (showModal.value) {
            SwipeableModal(
                showModal = showModal.value,
                team = teams[selectedTeamIndex],
                teamColor = teamColours[selectedTeamIndex],
                driverPairs = driverPairs[selectedTeamIndex],
                onDismiss = { showModal.value = false }
            )
        }
    }
}

// Separate loading screen used for 3D engine boot-up or model download
@Composable
fun ModellingLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon representing 3D content
            Image(
                painter = painterResource(id = R.drawable.three_d_modelling),
                contentDescription = "3D Model",
                modifier = Modifier.size(128.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grey progress bar stub
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFBFBFBF))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Loading label
            RussoOneText(
                text = "Loading Models",
                fontSize = 24.sp,
                color = Color.White
            )
        }
    }
}