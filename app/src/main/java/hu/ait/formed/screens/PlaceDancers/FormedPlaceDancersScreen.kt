package hu.ait.formed.screens.PlaceDancers

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hu.ait.formed.data.Dancer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedPlaceDancersScreen(
    modifier: Modifier = Modifier,
    formID: Int,
    numDancers: Int,
    placeDancersViewModel: FormedPlaceDancersViewModel = hiltViewModel()
) {


    val initDancers by placeDancersViewModel.getAllDancersByForm(formID).collectAsState(initial = emptyList())

    if (initDancers.isNotEmpty()){
        initDancers.toMutableList().forEach{dancer: Dancer ->
            placeDancersViewModel.dancerList += dancer
        }
    }


    Column {
        TopAppBar(
            title = {
                Text("Place Dancers")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    placeDancersViewModel.dancerList.forEach{dancer: Dancer ->
                        placeDancersViewModel.removeDancer(dancer)
                        placeDancersViewModel.dancerList -= dancer
                    }
                }) {
                    Icon(Icons.Filled.Clear, null)
                }
            } )

        Canvas(

            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Check if clicked on existing X, then remove it
                        val clickedDancer = placeDancersViewModel.dancerList.find { isClickWithinDancer(offset, it) }
                        val clickedNum = placeDancersViewModel.getClickedDancer()
                        if (clickedDancer != null) {
                            placeDancersViewModel.dancerList -= clickedDancer
                        } else if (placeDancersViewModel.getClickedDancer() != null && !isDancerAssigned(placeDancersViewModel.dancerList, clickedNum)) {
                            // Add a new X at the clicked position
                            placeDancersViewModel.dancerList += (Dancer(0, clickedNum ?: 0, offset.x, offset.y, true, formID))
                            placeDancersViewModel.dancerList.forEach{dancer: Dancer ->
                                Log.d("dancer log", "${dancer.number}: ${dancer.x}, ${dancer.y}")
                            }
                            placeDancersViewModel.insertDancer(Dancer(0, clickedNum ?: 0, offset.x, offset.y, true, formID))
                        }
                    }
                    detectDragGestures { change, dragAmount ->
                        val currentTouchPosition = change.position
                        // Check if the initial touch was inside a dancer
                        val draggedDancer = placeDancersViewModel.dancerList.find { isClickWithinDancer(currentTouchPosition, it) }

                        // If a dancer was found, move it
                        draggedDancer?.let {
                            it.x = it.x + dragAmount.x
                            it.y = it.y + dragAmount.y
                        }
                    }

                }
        ) {
            Log.d("dancerList", placeDancersViewModel.dancerList.size.toString())
            drawDancers(placeDancersViewModel.dancerList)
        }

        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White
        ) {
            NumberedButtonList(placeDancersViewModel, numDancers)
        }
    }
}

@Composable
fun NumberedButtonList(numButtonViewModel: FormedPlaceDancersViewModel, numDancers: Int?) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (numDancers != null) {
            items(numDancers) { index ->
                NumberedButton(numButtonViewModel, index + 1)
            }
        }
    }
}


@Composable
fun NumberedButton(buttonViewModel: FormedPlaceDancersViewModel, number: Int?) {

    var isButtonClicked by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isButtonClicked = !isButtonClicked
            if (number != null) {
                buttonViewModel.setClickedDancer(number)
            }
            if (!isButtonClicked) {
                buttonViewModel.setClickedDancer(null)
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isButtonClicked) Color.Green else Color.Black
        ),
        modifier = Modifier
            .height(48.dp)
            .padding(8.dp)
    ) {
        Text(text = number.toString())
    }
}

private fun DrawScope.drawX(topleft: Offset, botright: Offset) {
    // draw X
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,topleft.y),
        end = Offset(botright.x,botright.y),
        strokeWidth = 25f
    )
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,botright.y),
        end = Offset(botright.x,topleft.y),
        strokeWidth = 25f
    )
}

private fun Offset.distanceTo(other: Offset): Float {
    val dx = x - other.x
    val dy = y - other.y
    return kotlin.math.sqrt(dx * dx + dy * dy)
}

private fun DrawScope.drawDancers(dancersList: List<Dancer>) {
    dancersList.forEach {dancer: Dancer ->
        val dancerSize = 100F
        val start = Offset(dancer.x, dancer.y)
        val end = Offset(dancer.x + dancerSize, dancer.y + dancerSize)
        drawX(start, end)
        Log.d("dancer draw log", "${dancer.number}: ${dancer.x}, ${dancer.y}")
    }
}

fun isClickWithinDancer(clickOffset: Offset, dancer: Dancer): Boolean {
    val size = 10f

    return (clickOffset.x >= dancer.x &&
            clickOffset.x <= dancer.x + size &&
            clickOffset.y >= dancer.y &&
            clickOffset.y <= dancer.y + size)
}

fun isDancerAssigned(dancersList: List<Dancer>, clickedNum: Int?): Boolean {
    for (i in 0 until dancersList.size) {
        if (dancersList[i].number == clickedNum) {
            return true
        }
    }
    return false
}