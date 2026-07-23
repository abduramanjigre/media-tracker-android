package edu.metrostate.ics342.mediatracker.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LibraryUiState {
    data object Loading : LibraryUiState
    data class Error(val message: String) : LibraryUiState
    data class Success(val items: List<LibraryItem>) : LibraryUiState
}

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DefaultMediaRepository(DefaultSessionRepository(application))

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private var currentStatus = LibraryStatus.WANT_TO

    init {
        loadLibrary(currentStatus)
    }

    fun loadLibrary(status: LibraryStatus) {
        currentStatus = status
        _uiState.value = LibraryUiState.Loading
        viewModelScope.launch {
            try {
                val page = repository.getLibrary(status)
                _uiState.value = LibraryUiState.Success(page.items)
            } catch (e: Exception) {
                _uiState.value = LibraryUiState.Error(e.message ?: "Failed to load library")
            }
        }
    }

    fun retry() = loadLibrary(currentStatus)

    fun removeItem(mediaId: Int) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        _uiState.value = current.copy(items = current.items.filter { it.mediaId != mediaId })
    }

    fun updateStatus(mediaId: Int, newStatus: LibraryStatus) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        _uiState.value = current.copy(items = current.items.map { item ->
            if (item.mediaId == mediaId) item.copy(status = newStatus) else item
        })
    }
}
