package red.tetracube.cosynestapp.nest.features.api.payloads

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class NestRoomsResponse(
    @JsonProperty("id")
    val id: UUID,

    @JsonProperty("name")
    val name: String
)