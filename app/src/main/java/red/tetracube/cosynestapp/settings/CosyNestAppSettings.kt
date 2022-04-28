package red.tetracube.cosynestapp.settings

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import red.tetracube.cosynestapp.application.settings.CosyNestAppSettings
import java.io.InputStream
import java.io.OutputStream

object CosyNestSettingsAppSettingsSerializer : Serializer<CosyNestAppSettings> {

    override val defaultValue: CosyNestAppSettings = CosyNestAppSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CosyNestAppSettings {
        try {
            return CosyNestAppSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: CosyNestAppSettings,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.settingsDataStore: DataStore<CosyNestAppSettings> by dataStore(
    fileName = "settings.pb",
    serializer = CosyNestSettingsAppSettingsSerializer
)