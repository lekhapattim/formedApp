package hu.ait.formed.screens.AllForms

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.Form
import hu.ait.formed.screens.AllDances.FormedDanceListViewModel
import hu.ait.formed.screens.AllDances.FormedFormsListViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedFormsListScreen(
    modifier: Modifier = Modifier,
    danceID: Int,
    numDancers: Int,
    formsListViewModel: FormedFormsListViewModel = hiltViewModel(),
    onNavigateToPlaceDancer: (Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val formList by
    formsListViewModel.getAllForms(danceID).collectAsState(emptyList())

    val dance by
    formsListViewModel.getDance(danceID).collectAsState(null)

    var allDancers = mutableListOf<Dancer>()

    formList.forEach{form: Form ->
        val dancersList by formsListViewModel.getAllDancers(form.id).collectAsState(emptyList())
        dancersList.forEach { dancer: Dancer ->
            allDancers.add(dancer)
        }
    }

    var showAddFormListDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var formListItem: Form? by rememberSaveable {
        mutableStateOf(null)
    }

    Column {

        TopAppBar(
            title = {
                Text(dance?.title ?: "Forms List")
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = {
                    allDancers.forEach{dancer: Dancer ->
                        formsListViewModel.removeDancer(dancer)
                        allDancers.remove(dancer)
                    }
                    formsListViewModel.clearAllForms(danceID)
                }) {
                    Icon(Icons.Filled.Delete, null)
                }
                IconButton(onClick = {
                    formListItem = null
                    showAddFormListDialog = true
                }) {
                    Icon(Icons.Filled.AddCircle, null)
                }
            }
        )

        Column(modifier = modifier.padding(10.dp)) {

            if (showAddFormListDialog) {
                AddNewFormForm(danceID, formsListViewModel,
                    { showAddFormListDialog = false},
                    formListItem)
            }

            if (formList.isEmpty()) {
                Text(text = "No forms")
            }
            else {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    items(formList) {
                        FormListCard(numDancers, formItem = it,
                            onRemoveItem = {
                                allDancers.forEach { dancer: Dancer ->
                                    if (dancer.formID == it.id) {
                                        formsListViewModel.removeDancer(dancer)
                                        allDancers.remove(dancer)
                                    }
                                }
                                formsListViewModel.removeForm(it)}, onNavigateToPlaceDancer)
                    }
                }
            }
        }
    }


}

@Composable
fun FormListCard(
    numDancers: Int,
    formItem: Form,
    onRemoveItem: () -> Unit,
    onNavigateToPlaceDancer: (Int, Int) -> Unit
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
                Text(formItem.title, modifier = Modifier.fillMaxWidth(0.8f))
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        onNavigateToPlaceDancer(
                            formItem.id, numDancers
                        )
                    },
                    tint = Color.Blue
                )
            }

        }

    }
}



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddNewFormForm(
    danceID: Int,
    formsListViewModel: FormedFormsListViewModel,
    onDialogDismiss: () -> Unit,
    formListItem: Form? = null
) {
    Dialog(
        onDismissRequest = onDialogDismiss
    ) {
        var context = LocalContext.current
        var formListTitle by rememberSaveable {
            mutableStateOf( "")
        }
        var inputErrorStateTitle by rememberSaveable {
            mutableStateOf(false)
        }
        var errorTextTitle by rememberSaveable {
            mutableStateOf("")
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
                value = formListTitle,
                isError = inputErrorStateTitle,
                onValueChange = {
                    formListTitle = it
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
                    Text(text = "Form Title")
                }},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                trailingIcon = {
                    if (inputErrorStateTitle)
                        Icon(Icons.Filled.Warning,
                            "Error", tint = MaterialTheme.colorScheme.error)
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Button(onClick = {
                        if (formListTitle.isNotBlank()){
                            formsListViewModel.addNewForm(
                                Form(
                                    0,
                                    formListTitle,
                                    danceID,
                                )
                            )
                            onDialogDismiss()
                        }
                        else {
                            if(formListTitle.isBlank()){
                                inputErrorStateTitle = true
                                errorTextTitle = "Title cannot be empty"
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
