@file:JvmName("IMGS")
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import theme.AppTheme
import java.awt.Dimension
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import imgs.resources.*
import org.jetbrains.compose.resources.painterResource


fun main() = application {
    startKoin {
        modules(appModule)
    }

    val viewModel = getKoin().get<MainViewModel>()

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(size = DpSize(800.dp, 600.dp)),
        alwaysOnTop = true,
        title = "IMGS - Made with Compose for Desktop Wizard",
        icon = painterResource(Res.drawable.appIcon)
    ) {
        window.minimumSize = Dimension(800, 600)

        AppTheme {
            App(
                viewModel = viewModel
            )
        }
    }
}