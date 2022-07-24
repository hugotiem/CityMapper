package fr.hugotiem.citymapper.providers

import fr.hugotiem.citymapper.services.SearchService
import fr.hugotiem.citymapper.services.Service

object SearchProvider: Provider() {

    override val baseUrl: String = ""

    override val service: SearchService by lazy {
        retrofit.create(SearchService::class.java)
    }

}