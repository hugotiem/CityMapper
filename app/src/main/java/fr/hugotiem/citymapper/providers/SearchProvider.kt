package fr.hugotiem.citymapper.providers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.services.SearchService
import fr.hugotiem.citymapper.services.Service
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SearchProvider {

    private const val baseUrl: String =
        "https://maps.googleapis.com/maps/api/place/autocomplete/"

    val service: SearchService by lazy {
        retrofit.create(SearchService::class.java)
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