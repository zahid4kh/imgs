import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import coil3.compose.AsyncImage
import deskit.dialogs.info.InfoDialog
import deskit.dialogs.info.InfoDialogSample
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
                items(items = uiState.images){image ->
                    AsyncImage(
                        model = image.absolutePath,
                        contentDescription = null,
                        modifier = Modifier
                            .animateItem(placementSpec = spring())
                            .clip(MaterialTheme.shapes.large)
                            .clickable(
                                onClick = { viewModel.handleImageClick(image) }
                            )
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            }

            uiState.clickedImage?.let{ image ->
                Window(
                    onCloseRequest = { viewModel.closeImageDialog() },
                    title = image.nameWithoutExtension
                ){
                    AsyncImage(
                        model = image.absolutePath,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(20.dp)
                    )
                }
            }
        }

    }
}
