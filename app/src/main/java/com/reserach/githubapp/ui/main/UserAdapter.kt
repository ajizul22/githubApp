package com.reserach.githubapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.reserach.githubapp.R
import com.reserach.githubapp.data.model.User
import com.reserach.githubapp.databinding.ItemUserBinding
import com.reserach.githubapp.util.GlobalFunc
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<User>()
    private val globalfunc = GlobalFunc()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    fun setList(users: ArrayList<User>) {
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val bind: ItemUserBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(user: User) {
            bind.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(ivUser)

                tvUsername.text = user.userName
                tvId.text = user.id.toString()

                val date = user.createdAt.substring(0, 10)
                tvCreatedAt.text = globalfunc.FORMAT_DATE_DD_MMM_YYYY(date)

                if (user.email != null) {
                    tvEmail.text = user.email
                } else {
                    tvEmail.text = "-"
                }

                if (user.location != null) {
                    tvLocation.text = user.location

                } else {
                    tvLocation.text = "-"

                }

                bind.root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int  = list.size

    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }

}