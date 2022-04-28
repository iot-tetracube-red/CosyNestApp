package red.tetracube.cosynestapp.application.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import red.tetracube.cosynestapp.enumerations.NavigationIconType

class CosyNestAppViewModel : ViewModel() {

    private val _cosyNestAppState = MutableStateFlow(CosyNestAppViewData())
    val cosyNestAppState: StateFlow<CosyNestAppViewData> get() = _cosyNestAppState

    fun setScreenTitle(newTitle: String?) {
        _cosyNestAppState.value = _cosyNestAppState.value.copy(screenTitle = newTitle)
    }

    fun setNavigationIconVisible(visible: Boolean) {
        _cosyNestAppState.value = _cosyNestAppState.value.copy(navigationIconVisible = visible)
    }

    fun setNavigationIconType(navigationIconType: NavigationIconType) {
        _cosyNestAppState.value = _cosyNestAppState.value.copy(navigationIconType = navigationIconType)
    }

}