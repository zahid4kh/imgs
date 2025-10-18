import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.size.Size
import theme.AppTheme


@Composable
@Preview
fun App(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyStaggeredGridState()
    AppTheme(darkTheme = uiState.darkMode) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(200.dp),
                state = gridState,
                modifier = Modifier.matchParentSize().padding(10.dp),
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
                                model = file.absolutePath,
                                contentDescription = null,
                                modifier = Modifier
                                    .animateItem(placementSpec = spring())
                                    .clip(MaterialTheme.shapes.large)
                                    .clickable(
                                        onClick = { viewModel.handleImageClick(file) }
                                    )
                                    .pointerHoverIcon(PointerIcon.Hand)
                            )
                        }
                    }else if(file.isDirectory){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.large)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable{
                                    viewModel.updateCurrentDir(file)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                            Text(
                                text = file.name
                            )
                        }
                    }
                }
            }

            uiState.clickedImage?.let{ image ->
                Window(
                    onCloseRequest = { viewModel.closeImageDialog() },
                    title = image.nameWithoutExtension
                ){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(image.absolutePath)
                            .size(Size.ORIGINAL)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(20.dp)
                    )
                }
            }
        }

    }
}
