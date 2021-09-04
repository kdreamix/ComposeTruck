package me.kit.commonDomain.repo

import com.me.kit.TruckDatabase
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute

interface TruckRepo {
    suspend fun fetchTruckRoute(): List<TruckRoute>
    suspend fun fetchTruckLocation(): List<TruckLocation>

}

class TruckRepoImpl(private val truckApis: TruckApis, private val database: TruckDatabase) : TruckRepo {
    override suspend fun fetchTruckRoute(): List<TruckRoute> {
        return truckApis.fetchTruckRoute()
    }

    override suspend fun fetchTruckLocation(): List<TruckLocation> {
        return truckApis.fetchTruckLocation()
    }
}