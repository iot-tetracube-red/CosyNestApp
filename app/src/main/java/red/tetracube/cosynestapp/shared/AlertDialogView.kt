package red.tetracube.cosynestapp.shared

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun AlertDialogView(
    iconId: Int,
    titleStringId: Int,
    textStringId: Int,
    confirmStringId: Int,
    dismissable: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        icon = { painterResource(id = iconId) },
        onDismissRequest = {
            if (dismissable) {
                onDismiss()
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(id = confirmStringId))
            }
        },
        title = { Text(text = stringResource(id = titleStringId)) },
        text = { Text(text = stringResource(id = textStringId)) }
    )
}
