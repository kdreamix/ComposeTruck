package me.kit.common.bloc

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import me.kit.commonDomain.repo.TruckRepo

data class RootState(
    val truckRoute: List<TruckRoute> = emptyList(),
    val truckLocation: List<TruckLocation> = emptyList(),
    val truckListVisible: Boolean = false,
    val searchText: TextFieldValue = TextFieldValue("")
)

class RootComponent(componentContext: ComponentContext, val repo: TruckRepo) : ComponentContext by componentContext {
    private val _value = MutableValue(RootState())
    val state: Value<RootState> = _value

    suspend fun startLoading() {
        val route = fetchTruckRoute()
        val locations = fetchTruckLocation()
        _value.reduce { it.copy(truckRoute = route, truckLocation = locations) }
    }

    fun onMenuClick(){
        _value.reduce { it.copy(truckListVisible = !it.truckListVisible) }
    }

    fun onSearch(searchText: TextFieldValue){
        _value.reduce { it.copy(searchText = searchText) }
    }

    private suspend fun fetchTruckRoute(): List<TruckRoute> = repo.fetchTruckRoute()
    private suspend fun fetchTruckLocation(): List<TruckLocation> = repo.fetchTruckLocation()
}