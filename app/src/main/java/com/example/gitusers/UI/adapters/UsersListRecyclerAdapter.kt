package com.example.gitusers.UI.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitusers.R
import com.example.gitusers.domain.response.Items
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.users_single_view.view.*

class UsersListRecyclerAdapter : PagingDataAdapter<Items, UsersListRecyclerAdapter.ViewHolder>(UsersComparator){

    object UsersComparator : DiffUtil.ItemCallback<Items>(){
        override fun areItemsTheSame(
            oldItem: Items,
            newItem: Items
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Items,
            newItem: Items
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(item: Items?){
            itemView.user_name_tv.text = item?.name
            itemView.user_type_tv.text = item?.type

            Picasso.with(itemView.context)
                .load(item?.avatar_url)
                .fit()
                .into(itemView.user_image_view, object : Callback {
                    override fun onSuccess() {
                        itemView.user_image_view.setBackgroundDrawable(itemView.user_image_view.getDrawable())
                    }

                    override fun onError() {}
                })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.users_single_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = getItem(position)
        item?.let {
            holder.bindItems(it)
        }

    }

}