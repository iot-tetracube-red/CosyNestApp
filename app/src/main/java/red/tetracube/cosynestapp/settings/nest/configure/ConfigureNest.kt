package red.tetracube.cosynestapp.settings.nest.configure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import red.tetracube.cosynestapp.definitions.RoutesDefinitions.Companion.HOME
import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestFields
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestViewData
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestViewModel

@Composable
fun ConfigureNestSettings(
    viewModel: ConfigureNestViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val configureNestDataData = viewModel.configureNestViewData.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    ConfigureNestSettingsView(
        configureNestDataData,
        { value, field -> viewModel.updateFormFieldsValues(value, field) },
        {
            coroutineScope.launch {
                viewModel.saveNest(context)
            }
        },
        {
            if (it == ServiceConnectionStatus.CONNECTION_SUCCESS) {
                navHostController.navigate(HOME)
            }
            viewModel.setServiceStatus(ServiceConnectionStatus.IDLE)
        }
    )
}

@Composable
fun ConfigureNestSettingsView(
    configureNestDataData: ConfigureNestViewData,
    setFieldValue: (String, ConfigureNestFields) -> Unit,
    onButtonSaveTap: () -> Unit,
    onDialogDismiss: (ServiceConnectionStatus) -> Unit
) {

}