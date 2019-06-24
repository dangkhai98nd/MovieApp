package com.example.glidetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.glidetest.Models.ApiImages
import com.example.glidetest.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.backdrop.*

class BackdropAdapter(
    private var mContext: Context?
) : RecyclerView.Adapter<BackdropAdapter.ItemViewHolder>() {


    var mImages: MutableList<ApiImages.Image>? = null


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.backdrop, p0, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = mImages?.size ?: 0


    override fun onBindViewHolder(p0: ItemViewHolder, p1: Int) {


        Glide.with(this.mContext ?: return)
            .load(mImages?.get(p1)?.get_image_path())
            .thumbnail(Glide.with(this.mContext!!).load(R.drawable.icon_load_backdrop))
            .fitCenter()
            .into(p0.backdropbottom)


    }


    class ItemViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer


    fun addAll(images: List<ApiImages.Image>?) {
        this.mImages = images as MutableList<ApiImages.Image>?
        notifyDataSetChanged()
    }


}
