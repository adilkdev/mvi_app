package adil.app.mviapp.ui.main

import adil.app.mviapp.data.model.Post
import adil.app.mviapp.databinding.ItemViewBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

class MainAdapter @Inject constructor() : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {

    var list = mutableListOf<Post>()

    fun addPosts(posts: List<Post>) =
        list.addAll(posts).also {
            notifyItemRangeInserted(list.size - posts.size,
                posts.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemViewBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val post = list[holder.adapterPosition]
        holder.tvTitle.text = post.title
        holder.tvDescription.text = post.body
    }

    override fun getItemCount() = list.size

    class ItemViewHolder(binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
    }
}