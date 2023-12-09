package hu.ait.formed.screens.PlaceDancers

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Form
import hu.ait.formed.data.Dancer
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedPlaceDancersScreen(
    modifier: Modifier = Modifier,
    formID: Int,
    numDancers: Int,
    placeDancersViewModel: FormedPlaceDancersViewModel = hiltViewModel()
) {

    val initDancers by placeDancersViewModel.getAllDancersByForm(formID).collectAsState(initial = emptyList())

//    var dancerList by rememberSaveable {
//        mutableStateOf(mutableListOf<Dancer>())
//    }

    LaunchedEffect(key1 = Unit) {
        placeDancersViewModel.dancerList = initDancers.toMutableList()
    }



    val form = placeDancersViewModel.getForm(formID).collectAsState(initial = 0).value


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
                    // save everything here and navigate back
                }) {
                    Icon(Icons.Filled.Done, null)
                }
                IconButton(onClick = {
                    //clear all the Xs here
                }) {
                    Icon(Icons.Filled.Clear, null)
                }

            })

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
                            placeDancersViewModel.dancerList.remove(clickedDancer)
                        } else if (placeDancersViewModel.getClickedDancer() != null && !isDancerAssigned(placeDancersViewModel.dancerList, clickedNum)) {
                            // Add a new X at the clicked position
                            placeDancersViewModel.dancerList.add(Dancer(0, clickedNum ?: 0, offset.x, offset.y, true, formID))
                            placeDancersViewModel.dancerList.forEach{dancer: Dancer ->
                                Log.d("dancer log", "${dancer.number}: ${dancer.x}, ${dancer.y}")
                            }
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
            drawX(Offset(0F,0F), Offset(10F,10F))
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
        strokeWidth = 50f
    )
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,botright.y),
        end = Offset(botright.x,topleft.y),
        strokeWidth = 50f
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