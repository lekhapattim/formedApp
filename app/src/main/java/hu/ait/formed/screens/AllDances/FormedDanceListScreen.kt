package hu.ait.formed.screens.AllDances

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.Form


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedDanceListScreen(
    modifier: Modifier = Modifier,
    danceListViewModel: FormedDanceListViewModel = hiltViewModel(),
    onNavigateToDanceForms: (Int, Int) -> Unit,
    onNavigateToAnimateForms: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val danceList by danceListViewModel.getAllDances().collectAsState(emptyList())

    var allDancers = mutableListOf<Dancer>()
    var allForms = mutableListOf<Form>()

    if (danceList.isNotEmpty()){
        danceList.forEach{ dance: Dance ->
            val formList: List<Form> by danceListViewModel.getAllForms(dance.id).collectAsState(emptyList())
            if (formList.isNotEmpty()) {
                Log.d("dancer type", "formList: ${formList.firstOrNull()!!::class.simpleName}, size: ${formList.size}")
                formList.forEach { form: Form ->
                    Log.d("dancer type", "form: ${form::class.simpleName}")
                    val dancersList by danceListViewModel.getAllDancers(form.id)
                        .collectAsState(emptyList())
                    if (dancersList.isNotEmpty()) {
                        Log.d("dancer type", "dancersList: ${dancersList.firstOrNull()!!::class.simpleName}, size: ${dancersList.size}")
                        if (!dancersList.first()::class.simpleName.equals("Form")) {
                            dancersList.forEach { dancer: Dancer ->
                                Log.d("dancer type", "type: ${dancer.javaClass.simpleName}")
                                allDancers.add(dancer)
                            }
                        }
                        allForms.add(form)
                    }
                }
            }
        }
    }

    var showAddDanceListDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var danceListItem: Dance? by rememberSaveable {
        mutableStateOf(null)
    }

    Column {

        TopAppBar(
            title = {
                Text("Dances List")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    danceListItem = null
                    showAddDanceListDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            }
        )

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddDanceListDialog) {
                AddNewDanceForm(danceListViewModel,
                    { showAddDanceListDialog = false},
                    danceListItem)
            }

            if (danceList.isEmpty()) {
                Text(text = "No dances")
            }
            else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(danceList) {
                        DanceListCard(allDancers, allForms, danceItem = it,
                            onRemoveItem = {
                                allForms.forEach{form: Form ->
                                    if (form.danceID == it.id){
                                        allDancers.forEach { dancer ->
                                            Log.d("dancer type", "type2: ${dancer.javaClass.simpleName}")
                                            if (dancer.formID == form.id){
                                                danceListViewModel.removeDancer(dancer)
                                            }
                                        }
                                        danceListViewModel.removeForm(form)
                                    }
                                }
                                danceListViewModel.removeDance(it)
                                           }, onNavigateToDanceForms, onNavigateToAnimateForms)
                    }
                }
            }
        }
    }


}

@Composable
fun DanceListCard(
    allDancers: List<Dancer>,
    allForms: List<Form>,
    danceItem: Dance,
    onRemoveItem: () -> Unit,
    onNavigateToDanceForms: (Int, Int) -> Unit,
    onNavigateToAnimateForms: (Int) -> Unit
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        var expanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring()
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(danceItem.title, modifier = Modifier.fillMaxWidth(0.7f))
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Click",
                    modifier = Modifier.clickable {
                        onNavigateToDanceForms(
                            danceItem.id, danceItem.numDancers
                        )
                    },
                    tint = Color.Blue
                )
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Animate",
                    modifier = Modifier.clickable {
                        if (allDancers.isNotEmpty() && allForms.isNotEmpty()){
                            onNavigateToAnimateForms(
                                danceItem.id
                            )
                        }
                    },
                    tint = Color.Blue
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }
            }

            if (expanded) {
                Text(
                    text = danceItem.numDancers.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                    )
                )
            }

            }

        }
    }



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddNewDanceForm(
    danceListViewModel: FormedDanceListViewModel,
    onDialogDismiss: () -> Unit,
    danceListItem: Dance? = null
) {
    Dialog(
        onDismissRequest = onDialogDismiss
    ) {
        var context = LocalContext.current
        var danceListTitle by rememberSaveable {
            mutableStateOf( "")
        }
        var danceListNumDancers by rememberSaveable {
            mutableStateOf( "")
        }
        var inputErrorStateTitle by rememberSaveable {
            mutableStateOf(false)
        }
        var errorTextTitle by rememberSaveable {
            mutableStateOf("")
        }

        var inputErrorState by rememberSaveable {
            mutableStateOf(false)
        }
        var errorText by rememberSaveable {
            mutableStateOf("")
        }
        fun validateNum(text: String) {
            val allDigit = text.all { char -> char.isDigit() }
            errorText = "This field can be number only"
            inputErrorState = !allDigit
        }

        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp)
        ){
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = danceListTitle,
                isError = inputErrorStateTitle,
                onValueChange = {
                    danceListTitle = it
                    inputErrorStateTitle = false
                },
                label = { if (inputErrorStateTitle) {
                    Text(
                        text = errorTextTitle,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                else {
                    Text(text = "Song Title")
                }},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                trailingIcon = {
                    if (inputErrorStateTitle)
                        Icon(Icons.Filled.Warning,
                            "Error", tint = MaterialTheme.colorScheme.error)
                }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = danceListNumDancers,
                isError = inputErrorState,
                onValueChange = {
                    danceListNumDancers = it
                    inputErrorState = false
                    validateNum(danceListNumDancers)
                },
                label = { if (inputErrorState) {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                else {
                    Text(text = "Number of Dancers")
                }},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                trailingIcon = {
                    if (inputErrorState)
                        Icon(Icons.Filled.Warning, "Error", tint = MaterialTheme.colorScheme.error)
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Button(onClick = {
                        if (danceListTitle.isNotBlank() && danceListNumDancers.isNotBlank() && !inputErrorState){
                            danceListViewModel.addNewDance(
                                Dance(
                                    0,
                                    danceListTitle,
                                    danceListNumDancers.toInt(),
                                )
                            )
                            onDialogDismiss()
                        }
                        else {
                            if(danceListTitle.isBlank()){
                                inputErrorStateTitle = true
                                errorTextTitle = "Title cannot be empty"
                            }
                            if (danceListNumDancers.isBlank()){
                                inputErrorState = true
                                errorText = "Number of dancers cannot be empty"
                            }
                        }

                        }) {
                        Text(text = "Save")
                    }
                }
            }

        }

    }

}
