package red.tetracube.cosynestapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import red.tetracube.cosynestapp.application.model.CosyNestAppViewData
import red.tetracube.cosynestapp.application.model.CosyNestAppViewModel
import red.tetracube.cosynestapp.definitions.NavigationIconType
import red.tetracube.cosynestapp.definitions.RoutesDefinitions
import red.tetracube.cosynestapp.nest.features.NestFeatures
import red.tetracube.cosynestapp.nest.features.model.NestFeaturesViewModel
import red.tetracube.cosynestapp.nest.features.model.NestFeaturesViewModelFactory
import red.tetracube.cosynestapp.settings.nest.configure.ConfigureNestSettings
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestViewModel
import red.tetracube.cosynestapp.settings.settingsDataStore
import red.tetracube.cosynestapp.shared.AlertDialogView
import red.tetracube.cosynestapp.shared.LoaderOverlay
import red.tetracube.cosynestapp.ui.theme.CosyNestAppTheme
import java.util.*

@Composable
fun CosyNestApplication(
    viewModelData: CosyNestAppViewModel = viewModel()
) {
    val navController = rememberNavController()
    val appContext = LocalContext.current
    val dataStore = appContext.settingsDataStore
    val cosyNestAppData = viewModelData.cosyNestAppState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModelData.loadApplicationSettings(dataStore)
    }

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
                MainNavigation(
                    it,
                    navController,
                    cosyNestAppData,
                    setNavigationIconVisible,
                    setTitle
                )
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

@Composable
fun MainNavigation(
    innerPadding: PaddingValues,
    navHostController: NavHostController,
    cosyNestAppData: CosyNestAppViewData,
    setNavigationIconVisible: (Boolean) -> Unit,
    screenTitleSetter: (String?) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = RoutesDefinitions.HOME,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(RoutesDefinitions.HOME) {
            if (cosyNestAppData.applicationInitialized == null) {
                LoaderOverlay()
            } else if (!cosyNestAppData.applicationInitialized) {
                ManageDialog(cosyNestAppData, navHostController)
            } else {
                setNavigationIconVisible(false)
                screenTitleSetter(null)
                val nestFeaturesViewModel: NestFeaturesViewModel = viewModel(
                    checkNotNull(LocalViewModelStoreOwner.current),
                    null,
                    NestFeaturesViewModelFactory(
                        cosyNestAppData.nestName!!,
                        cosyNestAppData.authToken!!,
                        cosyNestAppData.nestId!!,
                        cosyNestAppData.apiBaseURL!!,
                        cosyNestAppData.webSocketURL!!
                    )
                )
                NestFeatures(nestFeaturesViewModel, navHostController)
            }
        }
        composable(RoutesDefinitions.SETTINGS_NESTS_CONFIGURE) {
            setNavigationIconVisible(true)
            screenTitleSetter(stringResource(id = R.string.set_nest_page_title))
            val configNestViewModel: ConfigureNestViewModel = viewModel()
            ConfigureNestSettings(configNestViewModel, navHostController)
        }
        composable(RoutesDefinitions.FEATURE_DETAILS) {
            setNavigationIconVisible(true)
            screenTitleSetter(it.arguments?.getString("featureName"))
            /*     val viewModel: FeatureDetailsViewModel = viewModel(
                     factory = FeatureDetailsViewModelFactory(
                         featureId = UUID.fromString(it.arguments!!.getString("featureId")!!),
                         featureType = FeatureTypeEnum.valueOf(it.arguments!!.getString("featureType")!!)
                     )
                 )
                 FeatureDetails(featureDetailsViewModel = viewModel)*/
        }
    }
}

@Composable
fun ManageDialog(
    cosyNestAppData: CosyNestAppViewData,
    navHostController: NavHostController
) {
    if (cosyNestAppData.applicationInitialized != null && !cosyNestAppData.applicationInitialized) {
        AlertDialogView(
            iconId = R.drawable.outline_touch_app_24,
            titleStringId = R.string.no_nest_configured_dialog_title,
            textStringId = R.string.no_nest_configured_dialog_message,
            confirmStringId = R.string.go,
            dismissible = false,
            onDismiss = {
                navHostController.navigate(RoutesDefinitions.SETTINGS_NESTS_CONFIGURE)
            }
        )
    }
}