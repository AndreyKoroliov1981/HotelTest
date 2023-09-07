package com.korol.myapplication.ui.room.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.korol.domain.room.model.Room
import com.korol.myapplication.R
import com.korol.myapplication.databinding.ItemRoomDetailsBinding
import java.util.Locale

class RoomRVAdapter(
    private val onClickButtonChoice: RVOnClickButtonChoice,
    private val swipeRightImageRoom: RVSwipeRightImageRoom,
    private val swipeLeftImageRoom: RVSwipeLeftImageRoom,
) : RecyclerView.Adapter<RoomRVAdapter.RoomRVViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, RoomRVDiffUtilCallback())

    fun updateList(list: List<Room>) {
        asyncListDiffer.submitList(list)
    }

    inner class RoomRVViewHolder(val binding: ItemRoomDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivRoomHotel.setOnClickListener {
                swipeRightImageRoom.onSwipe(asyncListDiffer.currentList[absoluteAdapterPosition])
            }

            binding.ivRoomHotel.setOnClickListener {
                swipeLeftImageRoom.onSwipe(asyncListDiffer.currentList[absoluteAdapterPosition])
            }

            binding.btnChoiceRoom.setOnClickListener {
                onClickButtonChoice.onClicked(asyncListDiffer.currentList[absoluteAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomRVViewHolder {
        val binding =
            ItemRoomDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomRVViewHolder(binding)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: RoomRVViewHolder, position: Int) {
        with(holder.binding) {
            Glide.with(holder.binding.root.context)
                .load(asyncListDiffer.currentList[position].imageUrls?.get(0))
                .centerInside()
                .error(R.drawable.ic_error)
                .into(ivRoomHotel)
            tvNameRoom.text = asyncListDiffer.currentList[position].name
            val listPeculiarities = asyncListDiffer.currentList[position].peculiarities
            if (listPeculiarities != null) {
                for (index in listPeculiarities) {
                    val chip = Chip(holder.binding.root.context)
                    chip.text = index
                    chip.isClickable = false
                    chip.isCheckable = true
                    cgPeculiarities.addView(chip)
                }
            }
            tvMinimalPrice.text = holder.binding.root.context.getString(
                R.string.textForRoomPrice,
                String.format(
                    Locale.FRANCE,
                    "%,d",
                    asyncListDiffer.currentList[position]?.price,
                ),
            )
            tvPriceForIt.text = asyncListDiffer.currentList[position].pricePer
        }
    }
}

class RoomRVDiffUtilCallback : DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean = oldItem == newItem
}