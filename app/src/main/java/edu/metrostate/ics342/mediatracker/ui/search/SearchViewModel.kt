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

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DefaultMediaRepository(DefaultSessionRepository(application))

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType.asStateFlow()

    private val _popularItems = MutableStateFlow<List<Media>>(emptyList())
    val popularItems: StateFlow<List<Media>> = _popularItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPopularItems()
    }

    fun onQueryChange(value: String) { _query.value = value }
    fun clearQuery() { _query.value = "" }
    
    fun onTypeSelect(type: String) {
        _selectedType.value = type
        loadPopularItems()
    }

    private fun loadPopularItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetching media with no query returns default "popular" items from the API
                val page = repository.search(
                    query = "",
                    type  = _selectedType.value.ifBlank { null },
                    after = null
                )
                _popularItems.value = page.items
            } catch (e: Exception) {
                // Fallback to empty if API fails
                _popularItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}