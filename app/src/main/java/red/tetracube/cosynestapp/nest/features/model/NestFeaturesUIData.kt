package red.tetracube.cosynestapp.nest.features.model

import red.tetracube.cosynestapp.definitions.ServiceConnectionStatus

data class NestFeaturesUIData(
    val serviceConnectionStatus: ServiceConnectionStatus = ServiceConnectionStatus.IDLE,
)