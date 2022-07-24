package fr.hugotiem.citymapper.services

import fr.hugotiem.citymapper.model.Result
import retrofit2.Response
import retrofit2.http.GET

interface SearchService: Service {
    @GET("results")
    suspend fun getResults(): Response<MutableList<Result>>
}