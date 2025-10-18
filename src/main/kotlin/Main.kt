@file:JvmName("IMGS")
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import theme.AppTheme
import java.awt.Dimension
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import imgs.resources.*
import org.jetbrains.compose.resources.painterResource


fun main(){
    startKoin {
        modules(appModule)
    }

    application{
        val viewModel = getKoin().get<MainViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val windowState = rememberWindowState(size = DpSize(800.dp, 600.dp))

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            alwaysOnTop = false,
            title = "IMGS",
            icon = painterResource(Res.drawable.appIcon),
            undecorated = true
        ) {
            window.minimumSize = Dimension(800, 600)

            AppTheme {
                Column(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
                ) {
                    TopBar(
                        onMinimizeWindow = { windowState.isMinimized = true },
                        onHandleWindowSize = {
                            windowState.placement = if (windowState.placement == WindowPlacement.Maximized)
                                WindowPlacement.Floating
                            else
                                WindowPlacement.Maximized
                        },
                        onCloseApplication = { exitApplication() },
                        uiState = uiState,
                        viewModel = viewModel
                    )

                    App(
                        viewModel = viewModel
                    )
                }

            }
        }
    }
}