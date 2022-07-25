package fr.hugotiem.citymapper.services

import fr.hugotiem.citymapper.viewModel.ResultBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("navitia-get-result")
    suspend fun getResults(@Body() body: ResultBody): Response<ResponseBody>
}