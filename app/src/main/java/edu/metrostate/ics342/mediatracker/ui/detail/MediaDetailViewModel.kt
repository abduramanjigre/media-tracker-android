package edu.metrostate.ics342.mediatracker.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.MediaDetail
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MediaDetailUiState {
    object Loading : MediaDetailUiState()
    data class Success(val detail: MediaDetail, val libraryItem: LibraryItem?) : MediaDetailUiState()
    data class Error(val message: String) : MediaDetailUiState()
}

class MediaDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DefaultMediaRepository(DefaultSessionRepository(application))

    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

    fun loadData(mediaId: Int) {
        viewModelScope.launch {
            _uiState.value = MediaDetailUiState.Loading
            try {
                val detail = repository.getMediaDetail(mediaId)
                if (detail == null) {
                    _uiState.value = MediaDetailUiState.Error("Media not found")
                } else {
                    val libItem = repository.getLibraryStatus(mediaId)
                    _uiState.value = MediaDetailUiState.Success(detail, libItem)
                }
            } catch (e: Exception) {
                _uiState.value = MediaDetailUiState.Error("Network error. Please try again.")
            }
        }
    }
}
