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

    init {
        viewModelScope.launch {
            val settings = database.getSettings()
            _uiState.value = _uiState.value.copy(
                darkMode = settings.darkMode,
            )
        }

        loadImages()
    }

    private fun loadImages(){
        viewModelScope.launch(Dispatchers.IO) {
            val images = picsDir.listFiles().toList().filter { it.isFile }
            println("Loaded ${images.size} images")
            withContext(Dispatchers.Main){
                _uiState.update {
                    it.copy(
                        images = images
                    )
                }
            }
        }
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
            loadImages()
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
        val images: List<File> = emptyList(),
        val clickedImage: File? = null
    )
}