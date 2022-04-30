package red.tetracube.cosynestapp.settings.nest.configure.api

import red.tetracube.cosynestapp.settings.nest.configure.api.payloads.ConfigNestRequest
import red.tetracube.cosynestapp.settings.nest.configure.api.payloads.ConfigNestResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SettingsService {

    @POST("/settings/nest/configure")
    fun configureNest(@Body addNestRequest: ConfigNestRequest): Call<ConfigNestResponse>
}