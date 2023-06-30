package com.example.marvelapp.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemParentDetailBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader

class DetailParentAdapter(
    private val detailParentList: List<DetailParentVE>,
    private val imageLoader: GlideImageLoader
) : RecyclerView.Adapter<DetailParentAdapter.DetailParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailParentViewHolder {
        return DetailParentViewHolder.create(parent, imageLoader)
    }

    override fun getItemCount() = detailParentList.size

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParentList[position])
    }

    class DetailParentViewHolder(
        itemBinding: ItemParentDetailBinding,
        private val imageLoader: GlideImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val textItemCategory: TextView = itemBinding.textItemCategory
        private val recyclerChildDetail: RecyclerView = itemBinding.recyclerItemDetail

        fun bind(detailParentVE: DetailParentVE) {
            textItemCategory.text = itemView.context.getString(detailParentVE.categoryStringResId)
            recyclerChildDetail.run {
                setHasFixedSize(true)
                adapter = DetailChildAdapter(detailParentVE.detailChildList, imageLoader)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                imageLoader: GlideImageLoader
            ): DetailParentViewHolder {
                val itemBinding = ItemParentDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return DetailParentViewHolder(itemBinding, imageLoader)
            }
        }
    }
}