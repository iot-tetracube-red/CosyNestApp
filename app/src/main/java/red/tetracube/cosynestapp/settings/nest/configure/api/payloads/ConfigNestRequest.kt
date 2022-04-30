package red.tetracube.cosynestapp.settings.nest.configure.api.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ConfigNestRequest @JsonCreator constructor(
    @JsonProperty("username")
    val username: String,

    @JsonProperty("password")
    val password: String
)