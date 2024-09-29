package org.lizardoreyes.permissionskmp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var permissionGranted by remember { mutableStateOf(false) }
    var requestPermission by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                requestPermission = true
            }) {
                Text("Show my location")
            }

            if (requestPermission) {
                requiredPermissions { granted ->
                    permissionGranted = granted
                }
            }

            if (permissionGranted) {
                Text("Permission granted, showing your location...")
                // Aquí puedes agregar la lógica para mostrar la ubicación
            } else {
                Text("Permission not granted")
            }
        }
    }
}

@Composable
fun requiredPermissions(onResult: (Boolean) -> Unit) {
    permissionRequestEffect(Permission.COARSE_LOCATION, onResult)
}

@Composable
fun permissionRequestEffect(permission: Permission, onResult: (Boolean) -> Unit) {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)

    LaunchedEffect(controller) {
        // Mostrar el cuadro de diálogo de solicitud de permiso
        controller.providePermission(permission)
        // Esperar a que el usuario acepte o deniegue el permiso
        onResult(controller.isPermissionGranted(permission))
    }
}
