package red.tetracube.cosynestapp.nest.features.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class NestFeaturesViewModelFactory(
    private val nestName: String,
    private val authToken: String,
    private val nestId: UUID,
    private val apiBaseURL: String,
    private val webSocketURL: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.cast(
            NestFeaturesViewModel(
                nestName,
                authToken,
                nestId,
                apiBaseURL,
                webSocketURL
            )
        )
        return viewModel!!
    }

}