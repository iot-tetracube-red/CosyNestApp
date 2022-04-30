package red.tetracube.cosynestapp.settings.nest.configure.model

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import red.tetracube.cosynestapp.application.settings.ConnectedNest
import red.tetracube.cosynestapp.client.ApiClient
import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus
import red.tetracube.cosynestapp.settings.nest.configure.api.SettingsService
import red.tetracube.cosynestapp.settings.nest.configure.api.payloads.ConfigNestRequest
import red.tetracube.cosynestapp.settings.settingsDataStore
import retrofit2.awaitResponse
import java.net.URI

class ConfigureNestViewModel : ViewModel() {

    private val _configureNestViewData = MutableStateFlow(ConfigureNestViewData())
    val configureNestViewData: StateFlow<ConfigureNestViewData> get() = _configureNestViewData

    fun updateFormFieldsValues(value: String, field: ConfigureNestFields) {
        when (field) {
            ConfigureNestFields.NEST_ADDRESS -> {
                _configureNestViewData.value = _configureNestViewData.value.copy(
                    nestAddress = value
                )
            }
            ConfigureNestFields.USERNAME -> {
                _configureNestViewData.value = _configureNestViewData.value.copy(
                    username = value
                )
            }
            ConfigureNestFields.PASSWORD -> {
                _configureNestViewData.value = _configureNestViewData.value.copy(
                    password = value
                )
            }
        }

        val submitButtonEnabled = !_configureNestViewData.value.password.isNullOrEmpty()
                && !_configureNestViewData.value.username.isNullOrEmpty()
                && !_configureNestViewData.value.nestAddress.isNullOrEmpty()
        _configureNestViewData.value = _configureNestViewData.value.copy(submitButtonEnabled = submitButtonEnabled)
    }

    suspend fun saveNest(context: Context) {
        val apiBaseURL = URI(
            "http",
            null,
            _configureNestViewData.value.nestAddress,
            8080,
            null,
            null,
            null
        )
        val webSocketURL = URI(
            "ws",
            null,
            _configureNestViewData.value.nestAddress,
            8081,
            null,
            null,
            null
        )

        setServiceStatus(ServiceConnectionStatus.CONNECTING)
        val apiBaseURLString = apiBaseURL.toString()

        val settingsService: SettingsService = ApiClient(apiBaseURLString)
            .retrofit
            .create(SettingsService::class.java)

        val configNestResponseBody = try {
            val username = _configureNestViewData.value.username!!
            val password = _configureNestViewData.value.password!!
            val signInResponse = settingsService.configureNest(
                ConfigNestRequest(username, password)
            )
                .awaitResponse()
            if (signInResponse.code() == 401) {
                setServiceStatus(ServiceConnectionStatus.UNAUTHORIZED)
                null
            } else {
                setServiceStatus(ServiceConnectionStatus.IDLE)
                signInResponse.body()
            }
        } catch (ex: Exception) {
            setServiceStatus(ServiceConnectionStatus.CONNECTION_ERROR)
            null
        } ?: return

        context.settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .addConnectedNests(
                    ConnectedNest.newBuilder()
                        .setCurrentServer(true)
                        .setAlias(configNestResponseBody.nestName)
                        .setUsername(_configureNestViewData.value.username)
                        .setApiBaseUrl(apiBaseURLString)
                        .setWebSocketUrl(webSocketURL.toString())
                        .setAuthToken(configNestResponseBody.authenticationToken)
                        .setNestId(configNestResponseBody.nestId.toString())
                        .build()
                )
                .build()
        }
        setServiceStatus(ServiceConnectionStatus.CONNECTION_SUCCESS)
    }

    fun setServiceStatus(newStatus: ServiceConnectionStatus) {
        _configureNestViewData.value = _configureNestViewData.value.copy(
            serviceConnectionStatus = newStatus
        )
    }
}
