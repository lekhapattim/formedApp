package hu.ait.formed.screens.PlaceDancers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Form
import hu.ait.formed.data.Dancer

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
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedPlaceDancersScreen(
    modifier: Modifier = Modifier,
    danceID: Int,
    placeDancersViewModel: FormedPlaceDancersViewModel = viewModel()
) {

    var dance by rememberSaveable{
        mutableStateOf<Dance?>(null)
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val launchedDance = placeDancersViewModel.getDance(danceID)

            dance = launchedDance
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
                    placeDancersViewModel.updateDance(dance)
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
        ) {
            // Draw your content on the canvas here
        }

        BottomAppBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White
        ) {
            NumberedButtonList(placeDancersViewModel, numDancers = dance?.numDancers)
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
        strokeWidth = 5f
    )
    drawLine(
        color = Color.Magenta,
        start = Offset(topleft.x,botright.y),
        end = Offset(botright.x,topleft.y),
        strokeWidth = 5f
    )
}