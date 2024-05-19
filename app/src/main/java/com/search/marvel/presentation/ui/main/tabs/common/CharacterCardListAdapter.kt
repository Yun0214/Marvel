package com.search.marvel.presentation.ui.main.tabs.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.search.marvel.Utils.setOnAntiDoubleClickListener
import com.search.marvel.databinding.RowCharacterCardBinding
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page

class CharacterCardListAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<CharacterCardModel, CharacterCardListAdapter.CharacterCardViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: CharacterCardViewHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterCardViewHolder {
        return CharacterCardViewHolder(RowCharacterCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateList(page: Page<CharacterCardModel>) {
        with(page) {
            if (pageIndex == 0) {
                submitList(getList())
            } else {
                currentList.toMutableList().apply {
                    addAll(getList())
                }.also {
                    submitList(it)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteState(newList: List<CharacterCardModel>) {
        val result = currentList.toList()
        result.forEach {
            if (it.isFavorite && !newList.contains(it)) {
                it.isFavorite = false
            } else if (!it.isFavorite && newList.find { newItem -> diffCallback.areItemsTheSame(it, newItem) } != null) {
                it.isFavorite = true
            }
        }
        notifyDataSetChanged()
    }

    fun removeFavorite(item: CharacterCardModel) {
        val result = currentList.toMutableList().apply {
            remove(item)
        }
        submitList(result)
    }

    inner class CharacterCardViewHolder(private val binding: RowCharacterCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterCardModel) = with(binding) {
            Glide.with(root).load(item.thumbnail).centerCrop().into(thumbnail)
            name.text = item.name
            description.text = item.description
            root.isSelected = item.isFavorite

            root.setOnAntiDoubleClickListener {
                root.isSelected = (!root.isSelected).also {
                    item.isFavorite = it
                }
                itemClickListener.onItemClick(item)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(item: CharacterCardModel)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CharacterCardModel>() {

            override fun areItemsTheSame(oldItem: CharacterCardModel, newItem: CharacterCardModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CharacterCardModel, newItem: CharacterCardModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}