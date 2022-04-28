package red.tetracube.cosynestapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import red.tetracube.cosynestapp.application.model.CosyNestAppViewData
import red.tetracube.cosynestapp.application.model.CosyNestAppViewModel
import red.tetracube.cosynestapp.enumerations.NavigationIconType
import red.tetracube.cosynestapp.settings.settingsDataStore
import red.tetracube.cosynestapp.ui.theme.CosyNestAppTheme

@Composable
fun CosyNestApplication(
    viewModelData: CosyNestAppViewModel = viewModel()
) {
    val navController = rememberNavController()
    val appContext = LocalContext.current
    val dataStore = appContext.settingsDataStore
    val cosyNestAppData = viewModelData.cosyNestAppState.collectAsState().value

    LaunchedEffect(key1 = Unit, block = {
        //  viewModel.loadApplicationSettings(dataStore)
    })

    /*val applicationData = ApplicationData(
        apiBaseURL = cosyNestAppData.apiBaseURL,
        webSocketURL = cosyNestAppData.webSocketURL,
        nestId = cosyNestAppData.nestId,
        authToken = cosyNestAppData.authToken,
        nestName = cosyNestAppData.nestName
    )*/

    //CompositionLocalProvider(LocalApplicationData provides applicationData) {
    CosyNestApplicationView(
        navController,
        cosyNestAppData,
        { viewModelData.setNavigationIconVisible(it) },
        { viewModelData.setScreenTitle(it) },
        { navController.popBackStack() }
    )
    //}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CosyNestApplicationView(
    navController: NavHostController,
    cosyNestAppData: CosyNestAppViewData,
    setNavigationIconVisible: (Boolean) -> Unit,
    setTitle: (String?) -> Unit,
    onBackPressed: () -> Unit
) {
    CosyNestAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        cosyNestAppData.navigationIconVisible,
                        cosyNestAppData.navigationIconType,
                        cosyNestAppData.screenTitle,
                        onBackPressed
                    )
                }
            ) {
                /*MainNavigation(
                    navController,
                    cosyNestAppData,
                    it,
                    setNavigationIconVisible,
                    setTitle
                )*/
            }
        }
    }
}

@Composable
fun TopAppBar(
    showBackNavigation: Boolean,
    navigationIconType: NavigationIconType,
    title: String?,
    onBackPressed: () -> Unit
) {
    val navigationIcon = when (navigationIconType) {
        NavigationIconType.BACK -> R.drawable.outline_arrow_back_24
        NavigationIconType.CLOSE -> R.drawable.outline_close_24
    }
    CenterAlignedTopAppBar(
        title = { Text(title ?: stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (showBackNavigation) {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = ""
                    )
                }
            }
        },
        actions = {
        }
    )
}
