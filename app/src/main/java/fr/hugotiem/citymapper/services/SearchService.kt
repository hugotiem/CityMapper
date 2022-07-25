package fr.hugotiem.citymapper.services

import fr.hugotiem.citymapper.model.ApiResult
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface SearchService: Service {
    @GET("json")
    suspend fun getResults(
        @Query("input") input: String,
        @Query("key") key: String = "AIzaSyBMI7Ogm3uxUTYQUUyDZ49tP3f308C1jbc",
        @Query("components") components: String = "country:fr",
    ): Response<ResponseBody> //Response<MutableList<Result>>
}