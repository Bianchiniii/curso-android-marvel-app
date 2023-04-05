package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharactersBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader
import com.example.marvelapp.utils.CharacterItemClick
import javax.inject.Inject

class CharactersViewHolder @Inject constructor(
    itemCharactersBinding: ItemCharactersBinding,
    private val imageLoader: GlideImageLoader,
    private val onItemClick: CharacterItemClick
) :
    RecyclerView.ViewHolder(itemCharactersBinding.root) {

    private val textName = itemCharactersBinding.textName
    private val imageCharacter = itemCharactersBinding.imageCharacter

    fun bind(character: Character) {
        imageCharacter.transitionName = character.name
        textName.text = character.name
        imageLoader.load(imageCharacter, character.imageUrl, R.drawable.ic_img_loading_error)

        itemView.setOnClickListener {
            onItemClick.invoke(character, imageCharacter)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: GlideImageLoader,
            onItemClick: CharacterItemClick
        ): CharactersViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharactersBinding.inflate(inflater, parent, false)
            return CharactersViewHolder(itemBinding, imageLoader, onItemClick)
        }
    }
}