package red.tetracube.cosynestapp.settings.nest.configure.api.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

class ConfigNestResponse @JsonCreator constructor (
    @JsonProperty("nest_id")
    val nestId: UUID,

    @JsonProperty("nest_name")
    val nestName: String,

    @JsonProperty("authentication_token")
    val authenticationToken: String
)