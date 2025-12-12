package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.request.disallowAnimatedImage
import com.github.panpf.sketch.request.repeatCount
import com.github.panpf.sketch.resize.Precision
import com.github.panpf.sketch.util.Size
import theme.AppTheme
import viewmodel.MainViewModel


@Composable
@Preview
fun App(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyStaggeredGridState()
    LaunchedEffect(uiState.currentPath){
        gridState.animateScrollToItem(0)
    }
    AppTheme(darkTheme = uiState.darkMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ){
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                state = gridState,
                modifier = Modifier
                    .matchParentSize()
                    .animateContentSize()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp
            ){
                items(items = uiState.files){ file ->
                    if(file.isFile){
                        ContextMenuArea(
                            items = {
                                listOf(ContextMenuItem("Delete") {
                                    viewModel.deleteImage(file)
                                })
                            }
                        ){
                            AsyncImage(
                                request = ImageRequest(
                                    context = LocalPlatformContext.current,
                                    uri = file.absolutePath
                                ){
                                    disallowAnimatedImage(false)
                                    repeatCount(-1)
                                    size(Size.Origin)
                                    precision(Precision.SAME_ASPECT_RATIO)
                                },
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.large)
                                    .clickable(
                                        onClick = { viewModel.handleImageClick(file) }
                                    )
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .animateItem(placementSpec = spring())
                            )
                        }
                    }else if(file.isDirectory){
                        val folderInteractionSource = remember { MutableInteractionSource() }
                        val isFolderHovered = folderInteractionSource.collectIsHoveredAsState()

                        Row(
                            modifier = Modifier
                                .animateItem(placementSpec = spring())
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.large)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .hoverable(folderInteractionSource)
                                .clickable(
                                    onClick = {viewModel.updateCurrentDir(file)},
                                    indication = ripple(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    interactionSource = folderInteractionSource
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = file.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            uiState.clickedImage?.let{ image ->
                Window(
                    onCloseRequest = { viewModel.closeImageDialog() },
                    title = image.nameWithoutExtension,
                    state = rememberWindowState(position = WindowPosition.Aligned(Alignment.Center))
                ){
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                    ){
                        AsyncImage(
                            request = ImageRequest(LocalPlatformContext.current, image.absolutePath){
                                disallowAnimatedImage(false)
                                repeatCount(-1)
                                size(Size.Origin)
                                precision(Precision.EXACTLY)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize().padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}
