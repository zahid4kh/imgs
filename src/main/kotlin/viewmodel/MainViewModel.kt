package viewmodel

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

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val homeDir = System.getProperty("user.home")
    private val picsDir = File("$homeDir/Pictures")

    private val imageExtensions = setOf(
        "png", "jpg", "jpeg", "gif", "bmp", "webp", "heic", "heif", "svg", "ico", "tiff", "tif"
    )

    init {
        viewModelScope.launch {
            generatePathSegments()
        }

        println("Current path is: ${_uiState.value.currentPath?.absolutePath}")
        loadFiles()
    }

    private fun loadFiles(){
        viewModelScope.launch(Dispatchers.IO) {
            val files = _uiState.value.currentPath.listFiles()
                ?.filter { it.isDirectory || (it.isFile && !it.isHidden && it.extension.lowercase() in imageExtensions) }
                ?.sorted()
                ?.map { file ->
                    FileEntry(
                        file = file,
                        imageCount = if (file.isDirectory) countImages(file) else null
                    )
                }
                ?: emptyList()
            println("Loaded ${files.size} files")
            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        files = files
                    )
                }
            }
        }
    }

    private fun countImages(dir: File): Int {
        return dir.listFiles()
            ?.count { it.isFile && !it.isHidden && it.extension.lowercase() in imageExtensions }
            ?: 0
    }

    fun updateCurrentDir(dir: File){
        _uiState.update { it.copy(currentPath = dir, files = emptyList()) }
        println("Current path is: ${_uiState.value.currentPath}")
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

    fun generatePathSegments(){
        val pathSegments = generateSequence(_uiState.value.currentPath) { it.parentFile }
            .toList()
            .asReversed()

        _uiState.update { it.copy(pathSegments = pathSegments) }
    }

    fun showInfoDialog(){
        _uiState.update { it.copy(isInfoDialogShown = true) }
    }

    fun hideInfoDialog(){
        _uiState.update { it.copy(isInfoDialogShown = false) }
    }

    fun showSettingsDropDown(){
        _uiState.update { it.copy(isSettingsShown = true) }
    }

    fun hideSettingsDropDown(){
        _uiState.update { it.copy(isSettingsShown = false) }
    }

    fun toggleDarkMode() {
        val newDarkMode = !_uiState.value.darkMode
        _uiState.value = _uiState.value.copy(darkMode = newDarkMode)
    }

    data class FileEntry(
        val file: File,
        val imageCount: Int? = null
    )

    data class UiState(
        val darkMode: Boolean = false,
        val files: List<FileEntry> = emptyList(),
        val clickedImage: File? = null,
        val currentPath: File = File("${System.getProperty("user.home")}/Pictures"),
        val pathSegments: List<File> = emptyList(),
        val isInfoDialogShown: Boolean = false,
        val isSettingsShown: Boolean = false
    )
}