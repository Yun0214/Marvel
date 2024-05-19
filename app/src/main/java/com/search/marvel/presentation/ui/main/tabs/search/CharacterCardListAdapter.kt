package com.search.marvel.presentation.ui.main.tabs.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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

    fun updateFavoriteState(newList: List<CharacterCardModel>) {
        val result = currentList.toList()
        result.forEachIndexed { index, it ->
            if (it.isFavorite && !newList.contains(it)) {
                it.isFavorite = false
                notifyItemChanged(index)
            } else if (!it.isFavorite && newList.find { newItem -> diffCallback.areItemsTheSame(it, newItem) } != null) {
                it.isFavorite = true
                notifyItemChanged(index)
            }
        }
    }

    inner class CharacterCardViewHolder(private val binding: RowCharacterCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isClicked: Boolean = false

        fun bind(item: CharacterCardModel) = with(binding) {
            //todo - thumbnail 업데이트

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
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: CharacterCardModel, newItem: CharacterCardModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}