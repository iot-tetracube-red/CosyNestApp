package red.tetracube.cosynestapp.nest.features.api.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class NestResponse @JsonCreator constructor(
    @JsonProperty("id")
    val id: UUID,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("rooms")
    val rooms: List<NestRoomsResponse>
)
