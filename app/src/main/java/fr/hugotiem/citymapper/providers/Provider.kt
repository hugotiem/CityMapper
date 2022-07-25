package fr.hugotiem.citymapper.providers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.hugotiem.citymapper.services.Service
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class Provider() {
    //private const val BASE_URL: String = "https://jsonplaceholder.typicode.com/"

    abstract val baseUrl: String;
    abstract val service: Service



}