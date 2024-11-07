package com.example.calendarcompose.presentation.welcome

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calendarcompose.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WelcomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    createAcct: () -> Unit,
    loginScreen: () -> Unit
){
    val google: Painter = painterResource(id = R.drawable.goog)
    val arrow: Painter = painterResource(id = R.drawable.arrow)
    var isButtonClicked by remember { mutableStateOf(false) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 200.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = google,
                contentDescription = "Google Logo",
                modifier = modifier,
                contentScale = ContentScale.Crop, // You can adjust this based on your needs

            )
            Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(60.dp)

                ) {
                    Button(
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "two"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        onClick = {
                            createAcct()
                        }) {
                        Text(
                            text = "SIGN UP",
                            color = Color.White
                        )
                    }
                    Button(
                        modifier = Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "one"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        ),
                        onClick = {
                            isButtonClicked = true
                            loginScreen()
                        }) {
                        Text(
                            text = "LOGIN",
                            color = Color.White
                        )
                    }

                }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start // Aligns content to the start (left)
            ) {
                Image(
                    painter = arrow,
                    contentDescription = "Arrow image",
                    modifier = Modifier
                        .size(120.dp),
                    contentScale = ContentScale.Fit, // You can adjust this based on your needs

                )
                Image(
                    painter = arrow,
                    contentDescription = "Arrow image",
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = (170).dp)
                        .graphicsLayer(scaleX = -1f),
                    contentScale = ContentScale.Fit, // You can adjust this based on your needs

                )
            }
            }
        }





