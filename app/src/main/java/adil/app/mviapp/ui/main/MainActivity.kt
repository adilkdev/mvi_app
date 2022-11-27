package adil.app.mviapp.ui.main

import adil.app.mviapp.data.model.Post
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    private val postList: MutableState<List<Post>> = mutableStateOf(listOf())
    private var isLoading = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostList(postList)
            ProgressBar(isLoading = isLoading)
        }
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
                when (viewState) {
                    is MainViewState.Initial -> {
                        isLoading.value = true
                    }
                    is MainViewState.Loading -> {
                        isLoading.value = true
                    }
                    is MainViewState.Success -> {
                        postList.value = viewState.data
                        isLoading.value = false
                    }
                    is MainViewState.Error -> {
                        isLoading.value = false
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
    fun ProgressBar(isLoading: State<Boolean>) {
        val alpha = if (isLoading.value) 1f else 0f
        Column(
            modifier = Modifier.fillMaxHeight().fillMaxWidth().alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
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