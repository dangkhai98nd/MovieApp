package com.example.glidetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.glidetest.BuildConfig
import com.example.glidetest.Models.Video
import com.example.glidetest.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class VideoAdapter (
    private var mContext : Context?
) : RecyclerView.Adapter<VideoAdapter.ItemViewHolder> ()  {


    var mVideos : MutableList<Video>? = null



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoAdapter.ItemViewHolder {


        var view: View? = LayoutInflater.from(p0.context).inflate(R.layout.backdrop, p0, false)
        return ItemViewHolder(view!!)


    }



    override fun getItemCount(): Int = (if (mVideos == null) 0 else mVideos?.size!!)


    override fun onBindViewHolder(p0: VideoAdapter.ItemViewHolder, p1: Int) {


        p0.youTubePlayerView?.initialize(BuildConfig.API_KEY, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p2: YouTubePlayer.Provider?, p3: YouTubePlayer?, p4: Boolean) {
                p3?.cueVideo("${mVideos?.get(p1)?.key}")
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

            }
        })


    }






    public class ItemViewHolder (
        private var view: View
    ): RecyclerView.ViewHolder(view) {
        var youTubePlayerView : YouTubePlayerView? = view.findViewById(R.id.videoyoutube) as YouTubePlayerView


    }


    fun addAll(videos : List<Video>?) {
        this.mVideos = videos as MutableList<Video>?
        notifyDataSetChanged()
    }
}