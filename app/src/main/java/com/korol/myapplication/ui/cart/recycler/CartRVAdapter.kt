package com.korol.myapplication.ui.cart.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korol.myapplication.R
import com.korol.myapplication.databinding.ItemProductCartBinding
import com.korol.network.api.cart.model.Gadget

class CartRVAdapter(
    private val onClickBasketListener: RVOnClickBasketListener,
    private val onClickCountListener: RVOnClickCountListener
) : RecyclerView.Adapter<CartRVAdapter.CartRVViewHolder>() {
    private val asyncListDiffer = AsyncListDiffer(this, CartRVDiffUtilCallback())

    fun updateList(list: List<Gadget>) {
        asyncListDiffer.submitList(list)
    }

    inner class CartRVViewHolder(val binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivBasket.setOnClickListener {
                onClickBasketListener.onClicked(asyncListDiffer.currentList[absoluteAdapterPosition])
            }
            binding.tvMinus.setOnClickListener {
                onClickCountListener.onClicked(
                    asyncListDiffer.currentList[absoluteAdapterPosition],
                    -1
                )
            }
            binding.tvPlus.setOnClickListener {
                onClickCountListener.onClicked(
                    asyncListDiffer.currentList[absoluteAdapterPosition],
//                    asyncListDiffer.currentList[absoluteAdapterPosition],
                    1
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRVViewHolder {
        val binding =
            ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartRVViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: CartRVViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.binding.root.context)
                .load(asyncListDiffer.currentList[position].images)
                .centerInside()
                .error(R.drawable.ic_error)
                .into(picture)
            productName.text = asyncListDiffer.currentList[position].title
            productTotalPrice.text = holder.binding.root.context.getString(
                R.string.itemGadgetTotalPrice,
                (asyncListDiffer.currentList[position].price * asyncListDiffer.currentList[position].count)
            )
            productCount.text = asyncListDiffer.currentList[position].count.toString()
        }
    }
}

class CartRVDiffUtilCallback : DiffUtil.ItemCallback<Gadget>() {
    override fun areItemsTheSame(oldItem: Gadget, newItem: Gadget): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Gadget, newItem: Gadget): Boolean = oldItem == newItem
}