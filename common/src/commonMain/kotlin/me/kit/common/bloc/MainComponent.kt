package me.kit.common.bloc

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import me.kit.commonDomain.TruckResult
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import me.kit.commonDomain.repo.TruckRepo

data class MainState(
    val truckRoute: TruckResult<List<TruckRoute>> = TruckResult.Loading,
    val truckLocation: TruckResult<List<TruckLocation>> = TruckResult.Loading,
    val truckListVisible: Boolean = false,
    val searchText: TextFieldValue = TextFieldValue("")
)

class MainComponent(private val repo: TruckRepo) {
    private val _value = MutableValue(MainState())
    val state: Value<MainState> = _value

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