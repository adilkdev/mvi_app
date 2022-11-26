package adil.app.mviapp.ui.main

import adil.app.mviapp.data.repository.PostRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    val mainIntent: MutableSharedFlow<MainIntent> = MutableSharedFlow()

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Initial)
    val state: StateFlow<MainViewState>
        get() = _state

    init {
        /** Step 1 : Ready to handle the incoming intent */
        handleIntent()
    }

    /** Step 3 : Process the intent */
    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.collect { intent ->
                when (intent) {
                    is MainIntent.GetPosts -> fetchPosts()
                }
            }
        }
    }

    /** Step 4 : Update the state based on the intent */
    private fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MainViewState.Loading
            try {
                _state.value = MainViewState.Success(data = postRepository.getPosts())
            } catch (exception: Exception) {
                _state.value = MainViewState.Error(errorMessage = exception.message.toString())
            }
        }
    }

}