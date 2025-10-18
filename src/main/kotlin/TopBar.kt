import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import theme.getJetbrainsMonoFamily

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
    WindowDraggableArea(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerHoverIcon(PointerIcon.Hand)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
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
                Row(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    TopBarIcon(
                        onClick = { viewModel.showSettingsDropDown() },
                        icon = {
                            Box{
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = null
                                )

                                DropdownMenu(
                                    expanded = uiState.isSettingsShown,
                                    onDismissRequest = { viewModel.hideSettingsDropDown() },
                                    shape = MaterialTheme.shapes.large,
                                    border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onSurface),
                                    containerColor = MaterialTheme.colorScheme.surface
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Theme") },
                                        onClick = { viewModel.toggleDarkMode() },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if(uiState.darkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                                contentDescription = null
                                            )
                                        },
                                        modifier = Modifier
                                            .pointerHoverIcon(PointerIcon.Hand)
                                            .padding(horizontal = 3.dp)
                                            .clip(MaterialTheme.shapes.large)
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Preview on hover") },
                                        onClick = { viewModel.togglePreviewOnHover() },
                                        trailingIcon = {
                                            Switch(
                                                checked = uiState.isPreviewOnHoverOn,
                                                onCheckedChange = { viewModel.togglePreviewOnHover() },
                                                thumbContent = {
                                                    Text(
                                                        text = if(uiState.isPreviewOnHoverOn) "On" else "Off",
                                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                                        fontFamily = getJetbrainsMonoFamily()
                                                    )
                                                }
                                            )
                                        },
                                        modifier = Modifier
                                            .pointerHoverIcon(PointerIcon.Hand)
                                            .padding(horizontal = 3.dp)
                                            .clip(MaterialTheme.shapes.large)
                                    )
                                }
                            }

                        }
                    )

                    TopBarIcon(
                        onClick = { viewModel.showInfoDialog() },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null
                            )
                        }
                    )
                }


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