package me.kit.commonDomain.network

import io.ktor.client.*
import io.ktor.client.request.*
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute

interface TruckApis {
    suspend fun fetchTruckRoute(): List<TruckRoute>
    suspend fun fetchTruckLocation(): List<TruckLocation>
}

class TruckApisImpl(private val client: HttpClient) : TruckApis {
    override suspend fun fetchTruckRoute() = client.get<List<TruckRoute>>(EndPoints.TRUCK_ROUTE) {
        parameter("page", 0)
        parameter("size", 100)
    }

    override suspend fun fetchTruckLocation() = client.get<List<TruckLocation>>(EndPoints.TRUCK_LCOATIONS) {
        parameter("page", 0)
        parameter("size", 100)
    }
}