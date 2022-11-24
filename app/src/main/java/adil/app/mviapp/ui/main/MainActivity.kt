package adil.app.mviapp.ui.main

import adil.app.mviapp.data.model.Post
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    val postList: MutableState<List<Post>> = mutableStateOf(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostList(postList)
        }
        observeViewModels()

        /** Step 1 : Provide the intent */
        lifecycleScope.launch {
            viewModel.mainIntent.emit(MainIntent.GetPosts)
        }
    }

    /** Step 4 : Update the view as per the state */
    private fun observeViewModels() {
        lifecycleScope.launch() {
            viewModel.state.collect { viewState ->
                when (viewState) {
                    is MainViewState.Initial -> {
                        //_binding.progressBar.visibility = View.GONE
                    }
                    is MainViewState.Loading -> {
                        //_binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainViewState.Success -> {
                        postList.value = viewState.data
                        //_binding.progressBar.visibility = View.GONE
                        //adapter.addPosts(viewState.data)
                    }
                    is MainViewState.Error -> {
                        //_binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    @Composable
    fun PostList(list: State<List<Post>>) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)) {
            items(list.value.size) { index ->
                val post = list.value[index]
                PostItem(title = post.title, description = post.body)
            }
        }
    }

    @Composable
    fun PostItem(title: String, description: String) {
        Column {
            Text(text = title, letterSpacing = 0.1.sp, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, letterSpacing = 0.1.sp, color = Color.Gray)
        }
    }

    @Preview
    @Composable
    fun Preview() {
        PostItem(title = "Title",
            description = "This is the Description")
    }

}