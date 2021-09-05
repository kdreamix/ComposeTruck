package me.kit.common.bloc

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import me.kit.commonDomain.TruckResult
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import me.kit.commonDomain.repo.TruckRepo

data class RootState(
//    val loadingRoute: Boolean = true,
//    val loadingLocation: Boolean = true,
//    val errorRoute: String = "",
//    val errorLocation: String = "",
    val truckRoute: TruckResult<List<TruckRoute>> = TruckResult.Loading,
    val truckLocation: TruckResult<List<TruckLocation>> = TruckResult.Loading,
    val truckListVisible: Boolean = false,
    val searchText: TextFieldValue = TextFieldValue("")
)

class RootComponent(componentContext: ComponentContext, private val repo: TruckRepo) :
    ComponentContext by componentContext {
    private val _value = MutableValue(RootState())
    val state: Value<RootState> = _value

    suspend fun startLoading() {
        fetchTruckRoute().collect { result ->
            _value.reduce { it.copy(truckRoute = result) }
        }
        fetchTruckLocation().collect { result ->
            _value.reduce { it.copy(truckLocation = result) }
        }

    }

    fun onMenuClick() {
        _value.reduce { it.copy(truckListVisible = !it.truckListVisible) }
    }

    fun onSearch(searchText: TextFieldValue) {
        _value.reduce { it.copy(searchText = searchText) }
    }

    private suspend fun fetchTruckRoute(): Flow<TruckResult<List<TruckRoute>>> = repo.fetchTruckRoute()
    private suspend fun fetchTruckLocation(): Flow<TruckResult<List<TruckLocation>>> = repo.fetchTruckLocation()
}