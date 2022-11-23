package adil.app.mviapp.ui.main

import adil.app.mviapp.data.model.Post

sealed class MainViewState {
    object Initial : MainViewState()
    object Loading : MainViewState()
    class Error(val errorMessage: String) : MainViewState()
    class Success(val data: List<Post>) : MainViewState()
}