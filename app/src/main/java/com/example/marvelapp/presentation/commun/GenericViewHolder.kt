package com.example.marvelapp.presentation.commun

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewbinding.ViewBinding

abstract class GenericViewHolder<T>(
    itemBinding: ViewBinding,
) : RecyclerView.ViewHolder(itemBinding.root) {

    abstract fun bind(data: T)
}