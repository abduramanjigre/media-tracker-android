package edu.metrostate.ics342.mediatracker.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.datastore.DefaultSessionRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.network.DefaultMediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchResultsViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = DefaultMediaRepository(DefaultSessionRepository(application))

    private val _results = MutableStateFlow<List<Media>>(emptyList())
    val results: StateFlow<List<Media>> = _results.asStateFlow()

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentQuery = ""
    private var nextCursor: String? = null
    private var hasMore = true

    fun search(query: String) {
        currentQuery = query
        _results.value = emptyList()
        nextCursor = null
        hasMore = true
        loadNextPage()
    }

    fun onTypeSelect(type: String) {
        _selectedType.value = type
        _results.value = emptyList()
        nextCursor = null
        hasMore = true
        loadNextPage()
    }

    fun loadNextPage() {
        if (_isLoading.value || !hasMore) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val page = mediaRepository.search(
                    query = currentQuery,
                    type  = _selectedType.value.ifBlank { null },
                    after = nextCursor
                )
                _results.value += page.items
                nextCursor     = page.nextCursor
                hasMore        = page.hasMore
            } catch (e: Exception) {
                // network errors silently ignored
            } finally {
                _isLoading.value = false
            }
        }
    }
}
