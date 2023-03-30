package com.example.marvelapp.presentation.characters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.example.marvelapp.utils.CharacterItemClick

class CharactersAdapter(
    private val onItemClick: CharacterItemClick
) : PagingDataAdapter<Character, CharactersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }

}