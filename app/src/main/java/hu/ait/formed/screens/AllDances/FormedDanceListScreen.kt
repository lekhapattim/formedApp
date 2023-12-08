package hu.ait.formed.screens.AllDances

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormedDanceListScreen(
    modifier: Modifier = Modifier,
    formedDanceListViewModel: FormedDanceListViewModel = hiltViewModel(),
) {