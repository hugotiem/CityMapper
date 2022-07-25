package fr.hugotiem.citymapper.providers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.services.ApiService
import fr.hugotiem.citymapper.services.SearchService
import fr.hugotiem.citymapper.services.Service
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiProvider {

    private const val baseUrl: String =
        "http://192.168.1.40:3000/map/"

    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}