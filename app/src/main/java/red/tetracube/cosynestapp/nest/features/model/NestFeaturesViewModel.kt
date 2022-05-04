package red.tetracube.cosynestapp.nest.features.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import red.tetracube.cosynestapp.client.ApiClient
import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus
import red.tetracube.cosynestapp.nest.features.api.NestFeaturesService
import retrofit2.awaitResponse
import java.net.ConnectException
import java.util.*

class NestFeaturesViewModel(
    nestName: String,
    authToken: String,
    nestId: UUID,
    apiBaseURL: String,
    webSocketURL: String
) : ViewModel() {

    private val _nestData = MutableStateFlow(NestData())
    val nestData: StateFlow<NestData> get() = _nestData

    private val _nestFeaturesServiceData = MutableStateFlow(NestFeaturesServiceData())
    val nestFeaturesServiceData: StateFlow<NestFeaturesServiceData> get() = _nestFeaturesServiceData

    private val _nestFeaturesUIData = MutableStateFlow(NestFeaturesUIData())
    val nestFeaturesUIData: StateFlow<NestFeaturesUIData> get() = _nestFeaturesUIData

    init {
        _nestData.value = _nestData.value.copy(
            nestId = nestId,
            nestName = nestName,
            authToken = authToken,
        )

        _nestFeaturesServiceData.value = _nestFeaturesServiceData.value.copy(
            apiBaseURL = apiBaseURL,
            webSocketURL = webSocketURL
        )
    }

    suspend fun loadNestRooms() {
        if (_nestFeaturesServiceData.value.apiBaseURL.isBlank()) {
            return
        }

        val nestFeatureService: NestFeaturesService =
            ApiClient(_nestFeaturesServiceData.value.apiBaseURL)
                .retrofit
                .create(NestFeaturesService::class.java)
        val homeFeatures = try {
            setServiceStatus(ServiceConnectionStatus.CONNECTING)
            val response = nestFeatureService.getNestData(
                "Bearer ${_nestData.value.authToken}",
                _nestData.value.nestId!!
            )
                .awaitResponse()
            when {
                response.code() == 401 -> {
                    setServiceStatus(ServiceConnectionStatus.UNAUTHORIZED)
                    null
                }
                response.code() == 404 -> {
                    setServiceStatus(ServiceConnectionStatus.NOT_FOUND)
                    null
                }
                else -> {
                    setServiceStatus(ServiceConnectionStatus.IDLE)
                    response.body()
                }
            }
        } catch (ex: Exception) {
            if (ex is ConnectException) {
                setServiceStatus(ServiceConnectionStatus.CONNECTION_ERROR)
            }
            null
        } ?: return

        _nestData.value = _nestData.value.copy(
            nestId = homeFeatures.id,
            nestName = homeFeatures.name,
            rooms = homeFeatures.rooms.map { roomResponse ->
                Room(
                    roomResponse.id,
                    roomResponse.name
                )
            }
        )
    }

    private fun setServiceStatus(newStatus: ServiceConnectionStatus) {
        _nestFeaturesUIData.value = _nestFeaturesUIData.value.copy(
            serviceConnectionStatus = newStatus
        )
    }

}
