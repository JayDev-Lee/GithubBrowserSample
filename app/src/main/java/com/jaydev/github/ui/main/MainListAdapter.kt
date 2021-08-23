package com.jaydev.github.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jaydev.github.common.ordinal
import com.jaydev.github.databinding.ItemUserInfoBinding
import com.jaydev.github.databinding.ItemUserRepoBinding
import com.jaydev.github.domain.entity.Repo
import com.jaydev.github.domain.entity.User
import com.jaydev.github.model.MainListItem

class MainListAdapter(
    private val itemList: List<MainListItem>,
    private val onClickUser: (User) -> Unit,
    private val onClickRepo: (User, Repo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MainListItem.Header.ordinal() -> HeaderViewHolder(ItemUserInfoBinding.inflate(layoutInflater))
            MainListItem.RepoItem.ordinal() -> RepoViewHolder(ItemUserRepoBinding.inflate(layoutInflater))
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(itemList[0] as MainListItem.Header)
            is RepoViewHolder -> holder.bind(itemList[position] as MainListItem.RepoItem)
        }
    }

    override fun getItemViewType(position: Int): Int = itemList[position].ordinal()

    override fun getItemCount() = itemList.size

    private inner class HeaderViewHolder(
        private val binding: ItemUserInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainListItem.Header) {
            with(binding) {
                profileImageView.load(item.user.profileImageUrl)
                userNameTextView.text = item.user.name
                root.setOnClickListener {
                    onClickUser.invoke(item.user)
                }
            }
        }
    }

    private inner class RepoViewHolder(
        private val binding: ItemUserRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainListItem.RepoItem) {
            with(binding) {
                repoNameTextView.text = item.repo.name
                descriptionTextView.text = item.repo.description
                countOfStarTextView.text = item.repo.starCount
                root.setOnClickListener {
                    val header = itemList[0] as MainListItem.Header
                    onClickRepo.invoke(header.user, item.repo)
                }
            }
        }
    }
}