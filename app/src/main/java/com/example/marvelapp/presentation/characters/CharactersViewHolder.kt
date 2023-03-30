package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bumptech.glide.Glide
import com.example.marvelapp.R
import com.example.marvelapp.databinding.ItemCharactersBinding
import com.example.marvelapp.utils.CharacterItemClick

class CharactersViewHolder(
    itemCharactersBinding: ItemCharactersBinding,
    private val onItemClick: CharacterItemClick
) :
    RecyclerView.ViewHolder(itemCharactersBinding.root) {

    private val textName = itemCharactersBinding.textName
    private val imageCharacter = itemCharactersBinding.imageCharacter

    fun bind(character: Character) {
        imageCharacter.transitionName = character.name
        textName.text = character.name
        Glide.with(itemView)
            .load(character.imageUrl)
            .fallback(R.drawable.ic_img_loading_error)
            .into(imageCharacter)

        itemView.setOnClickListener {
            onItemClick.invoke(character, imageCharacter)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: CharacterItemClick
        ): CharactersViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharactersBinding.inflate(inflater, parent, false)
            return CharactersViewHolder(itemBinding, onItemClick)
        }
    }
}