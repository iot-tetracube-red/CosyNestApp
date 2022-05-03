package red.tetracube.cosynestapp.settings.nest.configure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import red.tetracube.cosynestapp.R
import red.tetracube.cosynestapp.definitions.RoutesDefinitions.Companion.HOME
import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestFields
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestViewData
import red.tetracube.cosynestapp.settings.nest.configure.model.ConfigureNestViewModel
import red.tetracube.cosynestapp.shared.AlertDialogView
import red.tetracube.cosynestapp.shared.LoaderOverlay

@Composable
fun ConfigureNestSettings(
    viewModel: ConfigureNestViewModel,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val configureNestDataData = viewModel.configureNestViewData.collectAsState().value
    val submitButtonEnabled = viewModel.submitButtonEnabled.collectAsState().value
    val connectionStatus = viewModel.serviceConnectionStatus.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    ConfigureNestSettingsView(
        configureNestDataData,
        submitButtonEnabled,
        connectionStatus,
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
    submitButtonEnabled: Boolean,
    serviceConnectionStatus: ServiceConnectionStatus,
    setFieldValue: (String, ConfigureNestFields) -> Unit,
    onButtonSaveTap: () -> Unit,
    onDialogDismiss: (ServiceConnectionStatus) -> Unit
) {
    ServiceConnectionDialog(
        serviceConnectionStatus,
        onDialogDismiss
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            value = configureNestDataData.nestAddress ?: "",
            onValueChange = { setFieldValue(it, ConfigureNestFields.NEST_ADDRESS) },
            label = { Text(text = stringResource(id = R.string.config_nest_address_label)) }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            value = configureNestDataData.username ?: "",
            onValueChange = { setFieldValue(it, ConfigureNestFields.USERNAME) },
            label = { Text(stringResource(id = R.string.config_nest_username_label)) }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            value = configureNestDataData.password ?: "",
            onValueChange = { value: String -> setFieldValue(value, ConfigureNestFields.PASSWORD) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(id = R.string.config_nest_password_label)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            ElevatedButton(
                enabled = submitButtonEnabled,
                onClick = { onButtonSaveTap() }
            ) {
                Text(text = stringResource(id = R.string.config_nest_save_button))
            }
        }
    }
}

@Composable
fun ServiceConnectionDialog(
    serviceConnectionStatus: ServiceConnectionStatus,
    onDialogDismiss: (ServiceConnectionStatus) -> Unit
) {
    when (serviceConnectionStatus) {
        ServiceConnectionStatus.CONNECTION_SUCCESS -> {
            AlertDialogView(
                iconId = R.drawable.outline_check_circle_24,
                titleStringId = R.string.config_nest_dialog_success_title,
                textStringId = R.string.config_nest_dialog_success_message,
                confirmStringId = R.string.go,
                true,
                onDismiss = {
                    onDialogDismiss(serviceConnectionStatus)
                }
            )
        }
        ServiceConnectionStatus.CONNECTION_ERROR -> {
            AlertDialogView(
                iconId = R.drawable.outline_highlight_off_24,
                titleStringId = R.string.config_nest_dialog_connection_error_title,
                textStringId = R.string.config_nest_dialog_connection_error_message,
                confirmStringId = R.string.go,
                true,
                onDismiss = {
                    onDialogDismiss(serviceConnectionStatus)
                }
            )
        }
        ServiceConnectionStatus.UNAUTHORIZED -> {
            AlertDialogView(
                iconId = R.drawable.outline_highlight_off_24,
                titleStringId = R.string.config_nest_dialog_unauthorized_title,
                textStringId = R.string.config_nest_dialog_unauthorized_message,
                confirmStringId = R.string.go,
                true,
                onDismiss = {
                    onDialogDismiss(serviceConnectionStatus)
                }
            )
        }
        ServiceConnectionStatus.CONNECTING -> {
            LoaderOverlay()
        }
        else -> {}
    }
}