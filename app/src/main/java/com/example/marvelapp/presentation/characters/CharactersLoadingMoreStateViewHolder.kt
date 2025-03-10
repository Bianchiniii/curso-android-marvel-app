package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLoadMoreStateBinding

class CharactersLoadingMoreStateViewHolder(
    itemBinding: ItemCharacterLoadMoreStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val binding = ItemCharacterLoadMoreStateBinding.bind(itemView)
    private val progressBarLoadingMore = binding.progressLoadingMore
    private val textErrorTryAgain = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressBarLoadingMore.isVisible = loadState is LoadState.Loading
        textErrorTryAgain.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(
            parent: ViewGroup,
            retry: () -> Unit
        ): CharactersLoadingMoreStateViewHolder {
            val itemBinding = ItemCharacterLoadMoreStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return CharactersLoadingMoreStateViewHolder(itemBinding, retry)
        }
    }
}