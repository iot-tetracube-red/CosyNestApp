package red.tetracube.cosynestapp.application.model

import red.tetracube.cosynestapp.enumerations.NavigationIconType
import java.util.*

data class CosyNestAppViewData(
    val applicationInitialized: Boolean? = null,
    val screenTitle: String? = null,
    val navigationIconType: NavigationIconType = NavigationIconType.BACK,
    val navigationIconVisible: Boolean = false,
    val apiBaseURL: String? = null,
    val webSocketURL: String? = null,
    val authToken: String? = null,
    val nestId: UUID? = null,
    val nestName: String? = null
)