package com.jaydev.github.ui.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jaydev.github.databinding.ItemRepoForkBinding
import com.jaydev.github.domain.entity.Fork

class RepoDetailAdapter : RecyclerView.Adapter<RepoDetailAdapter.ViewHolder>() {
    private val itemList = mutableListOf<Fork>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRepoForkBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(list: List<Fork>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemRepoForkBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Fork) {
            with(binding) {
                forkNameTextView.text = item.fullName
                userNameTextView.text = item.name
                profileImageView.load(item.owner.profileImageUrl)
            }
        }
    }
}