package hu.ait.formed.screens.animateDance

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.formed.R
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Form
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimateDanceScreen(
    modifier: Modifier = Modifier,
    animateViewModel: AnimateViewModel = hiltViewModel(),
    danceID: Int
) {

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val launchedDance = animateViewModel.getDance(danceID)

            animateViewModel.dance = launchedDance
        }
    }


    Column {

        TopAppBar(
            title = {
                Text("Animate Dance: ${animateViewModel.dance?.title}")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    animateViewModel.isPlaying = animateViewModel.isPlaying
                }) {
                    if (animateViewModel.isPlaying) {
                        Icon(Icons.Filled.CheckCircle, null)
                    } else {
                        Icon(Icons.Filled.PlayArrow, null)
                    }
                }
                IconButton(onClick = {

                }) {
                    Icon(Icons.Filled.Refresh, null)
                }
            })

        Column(modifier = modifier.padding(10.dp)) {
            if (animateViewModel.dance?.forms?.isEmpty()!!) {
                Text("No forms created for this dance!")
            } else {
                Canvas(
                    modifier = Modifier
                        .size(300.dp)
                ) {
                    val dancerSize = 10F
                    animateViewModel.points.forEach {start: Offset ->
                        val end = Offset(start.x + dancerSize, start.y + dancerSize)
                        drawX(start, end)
                    }


                }
            }

        }
    }
}

private fun DrawScope.drawX(topleft: Offset, botright: Offset) {
    // draw X
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,topleft.y),
        end = Offset(botright.x,botright.y),
        strokeWidth = 5f
    )
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,botright.y),
        end = Offset(botright.x,topleft.y),
        strokeWidth = 5f
    )
}