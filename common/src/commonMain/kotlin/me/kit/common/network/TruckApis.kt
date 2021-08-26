package me.kit.common.network

import io.ktor.client.*
import io.ktor.client.request.*
import me.kit.common.network.responses.TruckLocationResponse
import me.kit.common.network.responses.TruckRouteResponse

interface TruckApis {
    suspend fun fetchTruckRoute(): TruckRouteResponse
    suspend fun fetchTruckLocation(): TruckLocationResponse
}

class TruckApisImpl(private val client: HttpClient) : TruckApis {
    override suspend fun fetchTruckRoute() = client.get<TruckRouteResponse>(EndPoints.TRUCK_ROUTE)
    override suspend fun fetchTruckLocation() = client.get<TruckLocationResponse>(EndPoints.TRUCK_LCOATIONS)
}