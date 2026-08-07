package edu.metrostate.ics342.mediatracker.ui.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.LibraryItem
import edu.metrostate.ics342.mediatracker.data.model.LibraryStatus
import edu.metrostate.ics342.mediatracker.data.model.Priority
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LibraryUiState {
    data object Loading : LibraryUiState
    data class Error(val message: String) : LibraryUiState
    data class Success(
        val items: List<LibraryItem>,
        val priorities: List<Priority> = emptyList()
    ) : LibraryUiState
}

class LibraryViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: DefaultMediaRepository =
        DefaultMediaRepository(DefaultSessionRepository(application))
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
                val priorities = if (status == LibraryStatus.WANT_TO) {
                    repository.getPriorities().sortedBy { it.orderIndex }
                } else {
                    emptyList()
                }
                _uiState.value = LibraryUiState.Success(page.items, priorities)
            } catch (e: Exception) {
                _uiState.value = LibraryUiState.Error(e.message ?: "Failed to load library")
            }
        }
    }

    fun retry() = loadLibrary(currentStatus)

    fun clearError() {
        _errorMessage.value = null
    }

    fun addPriority(mediaId: Int, priorityLevel: Int, hours: Int?) {
        if (mediaId <= 0) return
        val current = _uiState.value as? LibraryUiState.Success ?: return
        if (current.priorities.size >= 5) {
            _errorMessage.value = "Priority list is full (max 5 items)"
            return
        }
        if (current.priorities.any { it.mediaId == mediaId }) return

        val newPriority = Priority(
            mediaId = mediaId,
            priority = priorityLevel,
            orderIndex = current.priorities.size,
            estimatedTimeHours = hours,
            media = current.items.find { it.mediaId == mediaId }?.media
        )

        val newList = current.priorities + newPriority
        _uiState.value = current.copy(priorities = newList)

        viewModelScope.launch {
            try {
                repository.updatePriorities(newList)
            } catch (e: Exception) {
                _uiState.value = current // Revert on failure
                _errorMessage.value = e.message ?: "Couldn't save priority. Try again."
            }
        }
    }

    fun updatePriority(mediaId: Int, priorityLevel: Int, hours: Int?) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        val newList = current.priorities.map {
            if (it.mediaId == mediaId) {
                it.copy(priority = priorityLevel, estimatedTimeHours = hours)
            } else {
                it
            }
        }
        
        _uiState.value = current.copy(priorities = newList)

        viewModelScope.launch {
            try {
                repository.updatePriorities(newList)
            } catch (e: Exception) {
                _uiState.value = current // Revert
                _errorMessage.value = e.message ?: "Couldn't update priority. Try again."
            }
        }
    }

    fun removePriority(mediaId: Int) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        val newList = current.priorities.filter { it.mediaId != mediaId }
            .mapIndexed { index, p -> p.copy(orderIndex = index) }
        
        _uiState.value = current.copy(priorities = newList)

        viewModelScope.launch {
            try {
                repository.updatePriorities(newList)
            } catch (e: Exception) {
                _uiState.value = current // Revert
                _errorMessage.value = e.message ?: "Couldn't update priorities. Try again."
            }
        }
    }

    fun movePriority(fromIndex: Int, toIndex: Int) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        val newList = current.priorities.toMutableList()
        if (fromIndex !in newList.indices || toIndex !in newList.indices || fromIndex == toIndex) return
        
        val item = newList.removeAt(fromIndex)
        newList.add(toIndex, item)
        
        val indexedList = newList.mapIndexed { index, p -> p.copy(orderIndex = index) }
        _uiState.value = current.copy(priorities = indexedList)
        
        viewModelScope.launch {
            try {
                repository.updatePriorities(indexedList)
            } catch (e: Exception) {
                _uiState.value = current // Revert
                _errorMessage.value = e.message ?: "Couldn't save new order. Try again."
            }
        }
    }

    fun removeItem(mediaId: Int) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        val backupItems = current.items
        val backupPriorities = current.priorities
        
        val newItems = current.items.filter { it.mediaId != mediaId }
        val newPriorities = current.priorities.filter { it.mediaId != mediaId }
            .mapIndexed { index, p -> p.copy(orderIndex = index) }

        _uiState.value = current.copy(items = newItems, priorities = newPriorities)
        
        viewModelScope.launch {
            try {
                repository.removeFromLibrary(mediaId)
                if (backupPriorities.any { it.mediaId == mediaId }) {
                    repository.updatePriorities(newPriorities)
                }
            } catch (e: Exception) {
                _uiState.value = current.copy(items = backupItems, priorities = backupPriorities)
                _errorMessage.value = "Couldn't remove item. Try again."
            }
        }
    }

    /**
     * Optimistic: since the active tab already filters to [currentStatus] server-side, a status
     * change away from that status makes the item disappear from the visible list instantly.
     * PUT /library/{mediaId} happens in the background; a failure restores the item with its
     * original status.
     */
    fun updateStatus(mediaId: Int, newStatus: LibraryStatus) {
        val current = _uiState.value as? LibraryUiState.Success ?: return
        val backupItems = current.items
        val backupPriorities = current.priorities
        
        val itemToUpdate = current.items.find { it.mediaId == mediaId } ?: return
        if (itemToUpdate.status == newStatus) return
        
        val newItems = current.items.filter { it.mediaId != mediaId }
        val newPriorities = if (newStatus != LibraryStatus.WANT_TO) {
            current.priorities.filter { it.mediaId != mediaId }
                .mapIndexed { index, p -> p.copy(orderIndex = index) }
        } else {
            current.priorities
        }

        _uiState.value = current.copy(items = newItems, priorities = newPriorities)
        
        viewModelScope.launch {
            try {
                repository.updateLibraryStatus(mediaId, newStatus)
                if (newStatus != LibraryStatus.WANT_TO && backupPriorities.any { it.mediaId == mediaId }) {
                    repository.updatePriorities(newPriorities)
                }
            } catch (e: Exception) {
                _uiState.value = current.copy(items = backupItems, priorities = backupPriorities)
                _errorMessage.value = "Couldn't update status. Try again."
            }
        }
    }
}

