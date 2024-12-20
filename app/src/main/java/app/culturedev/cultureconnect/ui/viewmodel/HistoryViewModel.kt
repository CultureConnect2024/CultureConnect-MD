package app.culturedev.cultureconnect.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.culturedev.cultureconnect.data.database.DataEntity
import app.culturedev.cultureconnect.data.database.HistoryData
import app.culturedev.cultureconnect.data.repository.CafeRepo
import app.culturedev.cultureconnect.data.repository.RecommendationRepository
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: RecommendationRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listCafeHistory = MutableLiveData<List<DataEntity>?>()
    val listCafeHistory: LiveData<List<DataEntity>?> = _listCafeHistory

    fun addHistory(history: HistoryData) {
        viewModelScope.launch {
            repository.addHistory(history)
        }
    }

    fun searchHistory(query: String): LiveData<List<HistoryData>> {
        return repository.searchHistory(query)
    }
}
