import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MainViewModel(
    private val database: Database,
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val homeDir = System.getProperty("user.home")
    private val picsDir = File("$homeDir/Pictures")

    private val _currentPath = MutableStateFlow(File("$picsDir"))
    val currentPath = _currentPath.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = database.getSettings()
            _uiState.value = _uiState.value.copy(
                darkMode = settings.darkMode,
            )
        }
        println("Current path is: ${_currentPath.value.absolutePath}")
        loadFiles()
    }

    private fun loadFiles(){
        viewModelScope.launch(Dispatchers.IO) {
            val files = _currentPath.value.listFiles().toList().sorted()
            println("Loaded ${files.size} files")
            withContext(Dispatchers.Main){
                _uiState.update {
                    it.copy(
                        files = files
                    )
                }
            }
        }
    }

    fun updateCurrentDir(dir: File){
        _currentPath.value = dir
        println("Current path is: ${dir.absolutePath}")
        loadFiles()
    }

    fun handleImageClick(image: File){
        _uiState.update { it.copy(clickedImage = image) }
    }

    fun closeImageDialog(){
        _uiState.update { it.copy(clickedImage = null) }
    }

    fun deleteImage(image: File){
        try {
            image.delete()
            loadFiles()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun toggleDarkMode() {
        val newDarkMode = !_uiState.value.darkMode
        _uiState.value = _uiState.value.copy(darkMode = newDarkMode)

        viewModelScope.launch {
            val settings = database.getSettings()
            database.saveSettings(settings.copy(darkMode = newDarkMode))
        }
    }

    data class UiState(
        val darkMode: Boolean = false,
        val files: List<File> = emptyList(),
        val clickedImage: File? = null
    )
}