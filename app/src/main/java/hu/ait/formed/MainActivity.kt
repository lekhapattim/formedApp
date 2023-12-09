package hu.ait.formed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.formed.screens.AllDances.FormedDanceListScreen
import hu.ait.formed.screens.AllForms.FormedFormsListScreen
import hu.ait.formed.screens.animateDance.AnimateDanceScreen
import hu.ait.formed.ui.theme.FormedTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormedNavHost()
                }
            }
        }
    }
}

@Composable
fun FormedNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "dancelist"
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable("dancelist") {
            FormedDanceListScreen(
            onNavigateToDanceForms = {danceID->
                navController.navigate("formlist/$danceID")
            }, onNavigateToAnimateForms = {danceID->
                navController.navigate("animateforms/$danceID")
            }
        )
        }

        composable("formlist/{danceID}",
            arguments = listOf(
                navArgument("danceID"){type = NavType.IntType})
        ) {
            val ID = it.arguments?.getInt("danceID")
            if (ID != null) {
                FormedFormsListScreen(
                    danceID = ID,
                    onNavigateToPlaceDancer = {danceID->
                        navController.navigate("placedancer/$danceID")
                    }
                )
            }
        }

        composable("animatedance/{danceID}",
            arguments = listOf(
                navArgument("danceID"){type = NavType.IntType})
        ) {
            val id = it.arguments?.getInt("danceID")
            if (id != null) {
                AnimateDanceScreen(
                    danceID = id
                )
            }
        }
    }
}