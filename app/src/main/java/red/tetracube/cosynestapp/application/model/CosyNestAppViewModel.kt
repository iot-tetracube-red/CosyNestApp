package red.tetracube.cosynestapp.application.model

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import red.tetracube.cosynestapp.application.settings.CosyNestAppSettings
import red.tetracube.cosynestapp.definitions.NavigationIconType
import java.util.*

class CosyNestAppViewModel : ViewModel() {

    private val _cosyNestAppState = MutableStateFlow(CosyNestAppViewData())
    val cosyNestAppState: StateFlow<CosyNestAppViewData> get() = _cosyNestAppState

    fun setScreenTitle(newTitle: String?) {
        _cosyNestAppState.value = _cosyNestAppState.value.copy(screenTitle = newTitle)
    }

    fun setNavigationIconVisible(visible: Boolean) {
        _cosyNestAppState.value = _cosyNestAppState.value.copy(navigationIconVisible = visible)
    }

    fun setNavigationIconType(navigationIconType: NavigationIconType) {
        _cosyNestAppState.value =
            _cosyNestAppState.value.copy(navigationIconType = navigationIconType)
    }

    suspend fun loadApplicationSettings(dataStore: DataStore<CosyNestAppSettings>) {
        dataStore.data.map { applicationSettingsData ->
            val currentServer = applicationSettingsData
                .connectedNestsOrBuilderList
                .firstOrNull { nest -> nest.currentServer }
                    ?: return@map _cosyNestAppState.value.copy(
                        applicationInitialized = false
                    )
            return@map _cosyNestAppState.value.copy(
                applicationInitialized = applicationSettingsData.isInitialized,
                apiBaseURL = currentServer.apiBaseUrl,
                webSocketURL = currentServer.webSocketUrl,
                authToken = currentServer.authToken,
                nestId = UUID.fromString(currentServer.nestId),
                nestName = currentServer.alias
            )
        }
            .collect { _cosyNestAppState.value = it }
    }
}