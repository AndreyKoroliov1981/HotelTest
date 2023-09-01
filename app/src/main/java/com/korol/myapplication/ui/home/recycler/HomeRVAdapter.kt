package com.korol.myapplication.ui.home.recycler

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korol.myapplication.R
import com.korol.myapplication.databinding.ItemBestsellerHomeFragmentBinding
import com.korol.network.api.home.model.BestSeller

class HomeRVAdapter(
    private val onClickListener: RVOnClickListener
) : RecyclerView.Adapter<HomeRVAdapter.HomeRVViewHolder>() {
    private val asyncListDiffer = AsyncListDiffer(this, HomeRVDiffUtilCallback())

    fun updateList(list: List<BestSeller>) {
        asyncListDiffer.submitList(list)
    }

    inner class HomeRVViewHolder(val binding: ItemBestsellerHomeFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClickListener.onClicked(asyncListDiffer.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeRVViewHolder {
        val binding =
            ItemBestsellerHomeFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeRVViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: HomeRVAdapter.HomeRVViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.binding.root.context)
                .load(asyncListDiffer.currentList[position].picture)
                .centerInside()
                .error(R.drawable.ic_error)
                .into(ivImage)
            productName.text = asyncListDiffer.currentList[position].title
            tvDiscountPrice.text = holder.binding.root.context.getString(
                R.string.itemBestSellerPrice,asyncListDiffer.currentList[position].discountPrice
            )
            if (asyncListDiffer.currentList[position].isFavorites) {
                ivFavouritesBestsellerChecked.isVisible = true
                ivFavouritesBestsellerUnchecked.isVisible = false
            } else {
                ivFavouritesBestsellerChecked.isVisible = false
                ivFavouritesBestsellerUnchecked.isVisible = true
            }
            tvPriceWithoutDiscount.text = holder.binding.root.context.getString(
                R.string.itemBestSellerPrice,asyncListDiffer.currentList[position].priceWithoutDiscount
            )
            tvDiscountPrice.paintFlags = tvDiscountPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}

class HomeRVDiffUtilCallback : DiffUtil.ItemCallback<BestSeller>() {
    override fun areItemsTheSame(oldItem: BestSeller, newItem: BestSeller): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BestSeller, newItem: BestSeller): Boolean =
        oldItem == newItem
}