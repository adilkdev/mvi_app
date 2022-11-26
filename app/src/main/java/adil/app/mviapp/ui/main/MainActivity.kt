package adil.app.mviapp.ui.main

import adil.app.mviapp.databinding.ActivityMainBinding
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    @Inject
    lateinit var adapter: MainAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.recyclerView.adapter = adapter
        observeViewModels()

        /** Step 2 : Provide the intent */
        lifecycleScope.launch {
            viewModel.mainIntent.emit(MainIntent.GetPosts)
        }
    }

    /** Step 5 : Update the view as per the state */
    private fun observeViewModels() {
        lifecycleScope.launch() {
            viewModel.state.collect { viewState ->
                when(viewState) {
                    is MainViewState.Initial -> {
                        _binding.progressBar.visibility = View.GONE
                    }
                    is MainViewState.Loading -> {
                        _binding.progressBar.visibility = View.VISIBLE

                    }
                    is MainViewState.Success -> {
                        _binding.progressBar.visibility = View.GONE
                        adapter.addPosts(viewState.data)

                    }
                    is MainViewState.Error -> {
                        _binding.progressBar.visibility = View.GONE

                    }
                }
            }
        }
    }

}