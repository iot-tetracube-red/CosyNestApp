package red.tetracube.cosynestapp.nest.features.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class NestFeaturesViewModel(
    private val nestName: String,
    private val authToken: String,
    private val nestId: UUID,
    private val apiBaseURL: String,
    private val webSocketURL: String
) : ViewModel() {

    private val _nestData = MutableStateFlow(NestData())
    val nestData: StateFlow<NestData> get() = _nestData

    private val _nestFeaturesServiceData = MutableStateFlow(NestFeaturesServiceData())
    val nestFeaturesServiceData: StateFlow<NestFeaturesServiceData> get() = _nestFeaturesServiceData

    private val _nestFeaturesUIData = MutableStateFlow(NestFeaturesUIData())
    val nestFeaturesUIData: StateFlow<NestFeaturesUIData> get() = _nestFeaturesUIData

}
