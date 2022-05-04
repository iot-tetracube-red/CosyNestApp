package red.tetracube.cosynestapp.nest.features.api

import red.tetracube.cosynestapp.nest.features.api.payloads.NestResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.*

interface NestFeaturesService {

    @GET("/nest/{id}")
    fun getNestData(
        @Header("authToken") authToken: String,
        @Path("id") id: UUID
    ): Call<NestResponse>
}