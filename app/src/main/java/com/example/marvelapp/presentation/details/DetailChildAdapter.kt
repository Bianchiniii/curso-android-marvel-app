package com.example.marvelapp.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemChildDetailBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader

class DetailChildAdapter(
    private val detailChildList: List<DetailChildVE>,
    private val imageLoader: GlideImageLoader
) : RecyclerView.Adapter<DetailChildAdapter.DetailChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailChildViewHolder {
        return DetailChildViewHolder.create(parent, imageLoader)
    }

    override fun getItemCount() = detailChildList.size

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) {
        holder.bind(detailChildList[position])
    }

    class DetailChildViewHolder(
        itemBinding: ItemChildDetailBinding,
        private val imageLoader: GlideImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val imageCategory: ImageView = itemBinding.imageItemCategory

        fun bind(detailChildVE: DetailChildVE) {
            imageLoader.load(
                imageCategory,
                detailChildVE.imageUrl,
            )
        }

        companion object {
            fun create(
                parent: ViewGroup,
                imageLoader: GlideImageLoader
            ): DetailChildViewHolder {
                val itemBinding = ItemChildDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return DetailChildViewHolder(itemBinding, imageLoader)
            }
        }
    }
}