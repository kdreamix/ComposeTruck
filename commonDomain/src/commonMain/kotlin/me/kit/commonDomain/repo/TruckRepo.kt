package me.kit.commonDomain.repo

import com.me.kit.TruckDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.*
import me.kit.commonDomain.TruckResult
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import me.kit.commonDomain.network.responses.toDb
import me.kit.commonDomain.network.responses.toDomain

interface TruckRepo {
    suspend fun fetchTruckRoute(): Flow<TruckResult<List<TruckRoute>>>
    suspend fun fetchTruckLocation(): Flow<TruckResult<List<TruckLocation>>>
    fun latestTruckRoute(): Flow<List<TruckRoute>>
    suspend fun saveTruckRoute(list: List<TruckRoute>)
    suspend fun deleteAllTruckRoute()
}

class TruckRepoImpl(
    private val truckApis: TruckApis,
    private val database: TruckDatabase
) : TruckRepo {

    override suspend fun fetchTruckRoute() = resultFlow(truckApis.fetchTruckRoute())

    override suspend fun fetchTruckLocation() = resultFlow(truckApis.fetchTruckLocation())

    override fun latestTruckRoute(): Flow<List<TruckRoute>> {
        return database.truckRouteQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .flatMapMerge {
                flowOf(it.map { routes -> routes.toDomain() })
            }
    }

    override suspend fun saveTruckRoute(list: List<TruckRoute>) {
        val items = list.map { it.toDb() }
        database.truckRouteQueries.transaction {
            items.forEach {
                database.truckRouteQueries.insert(it)
            }
        }
    }

    override suspend fun deleteAllTruckRoute() {
        database.truckRouteQueries.deleteAll()
    }
}