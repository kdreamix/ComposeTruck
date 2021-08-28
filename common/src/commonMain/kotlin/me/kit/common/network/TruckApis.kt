package me.kit.common.network

import io.ktor.client.*
import io.ktor.client.request.*
import me.kit.common.network.responses.TruckLocationResponse
import me.kit.common.network.responses.TruckRoute

interface TruckApis {
    suspend fun fetchTruckRoute(): List<TruckRoute>
    suspend fun fetchTruckLocation(): TruckLocationResponse
}

class TruckApisImpl(private val client: HttpClient) : TruckApis {
    override suspend fun fetchTruckRoute() = client.get<List<TruckRoute>>(EndPoints.TRUCK_ROUTE){
        parameter("page", 0)
        parameter("size", 10)
    }
    override suspend fun fetchTruckLocation() = client.get<TruckLocationResponse>(EndPoints.TRUCK_LCOATIONS){
        parameter("page", 0)
        parameter("size", 10)
    }
}