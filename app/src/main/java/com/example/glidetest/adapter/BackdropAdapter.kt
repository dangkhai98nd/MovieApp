package com.example.glidetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.glidetest.Models.ApiImages
import com.example.glidetest.R

class BackdropAdapter (
    private var mContext : Context?
) : RecyclerView.Adapter<BackdropAdapter.ItemViewHolder> ()  {


    var mImages : MutableList<ApiImages.Image>? = null



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BackdropAdapter.ItemViewHolder {


        var view: View? = LayoutInflater.from(p0.context).inflate(R.layout.backdrop, p0, false)
        return ItemViewHolder(view!!)


    }



    override fun getItemCount(): Int = (if (mImages == null) 0 else mImages?.size!!)


    override fun onBindViewHolder(p0: BackdropAdapter.ItemViewHolder, p1: Int) {


        Glide.with(this!!.mContext!!)
            .load(mImages?.get(p1)?.get_image_path())
            .into(p0.backdropbottom!!)


    }






    public class ItemViewHolder (
        private var view: View
    ): RecyclerView.ViewHolder(view) {
        var backdropbottom: ImageView? = view?.findViewById(R.id.backdropbottom)

    }


    fun addAll(images : List<ApiImages.Image>?) {
        this.mImages = images as MutableList<ApiImages.Image>?
        notifyDataSetChanged()
    }
}
