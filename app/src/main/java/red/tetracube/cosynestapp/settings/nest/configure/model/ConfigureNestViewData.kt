package red.tetracube.cosynestapp.settings.nest.configure.model

import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus

data class ConfigureNestViewData(
    val nestAddress: String? = null,
    val username: String? = null,
    val password: String? = null,
    val serviceConnectionStatus: ServiceConnectionStatus = ServiceConnectionStatus.IDLE,
    val submitButtonEnabled: Boolean = false
)