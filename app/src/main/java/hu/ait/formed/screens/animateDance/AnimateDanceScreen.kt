package hu.ait.formed.screens.animateDance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.Form
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimateDanceScreen(
    modifier: Modifier = Modifier,
    animateViewModel: AnimateViewModel = hiltViewModel(),
    danceID: Int
) {

    val formsList by animateViewModel.getFormsByDance(danceID).collectAsState(emptyList())
    val dance by animateViewModel.getDance(danceID).collectAsState(null)


    Column {
        TopAppBar(
            title = {
                Text("Animate ${dance?.title ?: "no dance"}")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    animateViewModel.isPlaying = !animateViewModel.isPlaying
                }) {
                    if (animateViewModel.isPlaying) {
                        Icon(Icons.Filled.CheckCircle, null)
                    } else {
                        Icon(Icons.Filled.PlayArrow, null)
                    }
                }
                IconButton(onClick = {
                    animateViewModel.isPlaying = false
                    animateViewModel.isPlaying = true
                }) {
                    Icon(Icons.Filled.Refresh, null)
                }
            })
        Column(modifier = modifier.padding(10.dp)) {
            if (formsList.isEmpty()) {
                Text("No forms created for this dance!")
            } else {
                if (animateViewModel.isPlaying) {
                    startAnimation(formsList = formsList, animateViewModel.dancerList, animateViewModel = animateViewModel)
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    drawDancers(animateViewModel.dancerList)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawDancers(dancersList: List<Dancer>) {
    dancersList.forEach {dancer: Dancer ->
        val dancerSize = 10F
        val start = Offset(dancer.x, dancer.y)
        val end = Offset(dancer.x + dancerSize, dancer.y + dancerSize)
        drawX(start, end)
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

@Composable
fun startAnimation(formsList: List<Form>, dancersList: List<Dancer>, animateViewModel: AnimateViewModel){
    val coroutineScope = rememberCoroutineScope()
    while(animateViewModel.isPlaying) {
        formsList.forEach { curForm: Form ->
            val curDancers by animateViewModel.getAllDancersByForm(curForm.id).collectAsState(emptyList())
            coroutineScope.launch {
                animateViewModel.dancerList = emptyList()
                curDancers.forEach {dancer: Dancer ->
                    animateViewModel.dancerList += dancer
                }
                delay(2000)
            }
        }
        animateViewModel.isPlaying = false
    }
}