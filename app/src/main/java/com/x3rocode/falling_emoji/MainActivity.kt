package com.x3rocode.falling_emoji

import android.content.res.Resources
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
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
fun Screen() {
    val heartCount = remember { mutableStateOf(5) }
    val onClick = remember { mutableStateOf(false) }



    Log.d("gggggggggggggggggg", onClick.value.toString())
    Box(modifier = Modifier.fillMaxSize()) {


        Button(
            onClick = {
                onClick.value = true
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

        if(onClick.value){
            fallingEmoji(
                modifier = Modifier
                    .fillMaxSize(),
                fontSize = 110,
                text = "a",
                count = 3,
                onClick = onClick
            )
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun fallingEmoji(
    modifier: Modifier,
    count: Int,
    text: String,
    fontSize: Int,
    onClick : MutableState<Boolean>
) {

    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val xRandom = Random.nextInt(0, width)
    var xRandomList = Array<Dp>(count) { Random.nextInt(0, width).dp }.asList()


    val fallingSecond = Random.nextInt(1000, 2000)
    var fallingSecondList = IntArray(count) { Random.nextInt(1000, 2000) }.asList()

    val state = remember {
        mutableStateOf(EmojiState.Show)
    }


    val offsetYAnimation: Dp by animateDpAsState(
        targetValue = when (state.value) {
            EmojiState.Show -> 0.dp
            else -> height.dp
        },
        animationSpec = tween(fallingSecond),
        finishedListener = {
            onClick.value = false
            state.value = EmojiState.Hide
        }
    )
    val offsetYAnimationList = Array<Dp>(count) {
        offsetYAnimation
    }.asList()

    val offsetXAnimation by remember { mutableStateOf(xRandomList) }

    val rotateAnimation: Float by animateFloatAsState(
        targetValue = when (state.value) {
            EmojiState.Show -> 0f
            else -> xRandom.toFloat()
        },
        animationSpec = tween(1000)
    )
    LaunchedEffect(key1 = state, block = {
        when (state.value) {
            EmojiState.Show -> {
                state.value = EmojiState.Hide
            }
            EmojiState.Hide -> {
                state.value = EmojiState.Show
            }
        }
    })
    val textMeasure = rememberTextMeasurer()


    Canvas(modifier = modifier
        .border(width = 1.dp, color = Color.Black),
        onDraw = {
            repeat(count){
                drawText(
                    textMeasurer = textMeasure,
                    text = text,
                    style = TextStyle(
                        fontSize = fontSize.toSp(),
                    ),
                    topLeft = Offset(offsetXAnimation[it]?.toPx(), offsetYAnimation.toPx())
                )
            }
        }
    )
}

