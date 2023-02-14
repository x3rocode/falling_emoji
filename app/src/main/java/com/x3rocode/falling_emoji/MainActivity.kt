package com.x3rocode.falling_emoji

import android.graphics.Paint
import android.os.Bundle
import android.util.Half.toFloat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.x3rocode.falling_emoji.ui.theme.Falling_emojiTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Falling_emojiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
    }
}

enum class EmojiState {
    Show,
    Hide
}

@Composable
fun fallingEmoji(modifier: Modifier,
          count: Int,
          text: String,
          fontSize: Int
) {
    val textPaint = Paint().apply {
        textSize = fontSize.toFloat()
        color = Color.Black.toArgb()
    }

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val xRandom = Random.nextInt(0, width)
    val fallingSecond = Random.nextInt(1000, 2000)

    val state = remember {
        mutableStateOf(EmojiState.Show)
    }

    val offsetYAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            EmojiState.Show -> 0.dp
            else -> height.dp
        },
        animationSpec = tween(fallingSecond)
    )

    val offsetXAnimation: Dp by remember { mutableStateOf(xRandom.dp) }

    val rotateAnimation: Float by animateFloatAsState(
        targetValue = when (state.value) {
            EmojiState.Show -> 0f
            else -> xRandom.toFloat()
        },
        animationSpec = tween(1000)
    )
    LaunchedEffect(key1 = state, block = {
        state.value = when (state.value) {
            EmojiState.Show -> EmojiState.Hide
            EmojiState.Hide -> EmojiState.Show
        }
    })

    AnimatedVisibility(
        visible = state.value == EmojiState.Show,
        enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        exit = fadeOut(animationSpec = tween(durationMillis = 1000))
    ) {
        Canvas(modifier = modifier
            .offset(y = offsetYAnimation, x = offsetXAnimation),
            onDraw = {
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        text,
                        0f,
                        0f,
                        textPaint
                    )
                }
            }
        )
    }
}

@Composable
fun Screen() {
    val heartCount = remember { mutableStateOf(5) }
    val onClick = remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {


        if(onClick.value){
            repeat(heartCount.value) {
                fallingEmoji(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 36.dp),
                    fontSize = 110,
                    text = "a",
                    count = 3
                )
            }
        }

        Button(
            onClick = {
                onClick.value = !onClick.value
            },
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentHeight()
                .wrapContentWidth()
        ) {
            Text(
                text = "click",
                color = Color.White
            )
        }

    }
}