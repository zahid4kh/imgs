import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope

@Composable
fun WindowScope.TopBar(
    onMinimizeWindow: () -> Unit,
    onCloseApplication: () -> Unit,
    onHandleWindowSize: () -> Unit,
    uiState: MainViewModel.UiState,
    viewModel: MainViewModel
){
    LaunchedEffect(uiState.currentPath){
        viewModel.generatePathSegments()
    }
    WindowDraggableArea {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .pointerHoverIcon(PointerIcon.Hand),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            PathSegments(
                pathSegments = uiState.pathSegments,
                onPathSelected = { viewModel.updateCurrentDir(it) },
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                TopBarIcon(
                    onClick = { onMinimizeWindow() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Minimize,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )

                TopBarIcon(
                    onClick = { onHandleWindowSize() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CropSquare,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )

                TopBarIcon(
                    onClick = { onCloseApplication() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = null
                        )
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarIcon(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
){
    IconButton(
        onClick = { onClick() },
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
    ){
        icon()
    }

}