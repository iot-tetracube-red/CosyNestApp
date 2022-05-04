package red.tetracube.cosynestapp.nest.features.model

import java.util.*

data class NestData(
    val nestId: UUID? = null,
    val nestName: String? = null,
    val authToken: String? = null,
    val rooms: List<Room> = emptyList()
)