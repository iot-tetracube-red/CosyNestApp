package red.tetracube.cosynestapp.nest.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.compose.runtime.collectAsState
import red.tetracube.cosynestapp.nest.features.model.NestFeaturesViewModel

@Composable
fun NestFeatures(
    viewModel: NestFeaturesViewModel,
    navHostController: NavHostController
) {
    val nestData = viewModel.nestData.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    //val swipeRefreshState = rememberSwipeRefreshState(false)

    LaunchedEffect(Unit) {
        viewModel.loadNestRooms()
    }

    /*NestFeaturesView(
        nestData.value,
        swipeRefreshState,
        navHostController,
        onRefresh = {
            coroutineScope.launch {
                viewModel.loadNestRooms()
            }
        }
    )*/
}