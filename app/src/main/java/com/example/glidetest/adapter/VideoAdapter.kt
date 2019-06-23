package com.example.glidetest.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.glidetest.BuildConfig
import com.example.glidetest.Models.Video
import com.example.glidetest.R
import com.google.android.youtube.player.*

class VideoAdapter (
    private var mContext : Context?,
    private var mVideos : List<Video>?
) : RecyclerView.Adapter<VideoAdapter.ItemViewHolder> ()  {


    var selectedPosition : Int = 0



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoAdapter.ItemViewHolder {


        var view: View? = LayoutInflater.from(p0.context).inflate(R.layout.videolayout, p0, false)
        return ItemViewHolder(view!!)


    }



    override fun getItemCount(): Int = (if (mVideos == null) 0 else mVideos?.size!!)


    override fun onBindViewHolder(p0: VideoAdapter.ItemViewHolder, p1: Int) {


//        p0.youTubePlayerView?.initialize(BuildConfig.API_KEY, object : YouTubePlayer.OnInitializedListener{
//            override fun onInitializationSuccess(p2: YouTubePlayer.Provider?, p3: YouTubePlayer?, p4: Boolean) {
//                p3?.cueVideo("${mVideos?.get(p1)?.key}")
//            }
//
//            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
//
//            }
//        })
        if (selectedPosition == p1)
        {
            p0.youTubeCardView?.setCardBackgroundColor(ContextCompat.getColor(this!!.mContext!!,R.color.primary_dark_material_dark))
        }
        else {
            p0.youTubeCardView?.setCardBackgroundColor(ContextCompat.getColor(this!!.mContext!!,R.color.white))
        }


        p0.youTubeThumbnailView?.initialize(BuildConfig.API_KEY,object :YouTubeThumbnailView.OnInitializedListener{
            override fun onInitializationSuccess(p2: YouTubeThumbnailView?, p3: YouTubeThumbnailLoader?) {
                p3?.setVideo(mVideos?.get(p1)?.key)
                p3?.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener{
                    override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p1: String?) {
                        p3?.release()

                    }

                    override fun onThumbnailError(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader.ErrorReason?) {

                    }
                })
            }

            override fun onInitializationFailure(p2: YouTubeThumbnailView?, p3: YouTubeInitializationResult?) {

            }
        })



    }

    fun setSelectedPosition(position: Int?) {

        selectedPosition = position!!
        notifyDataSetChanged()
    }


    class ItemViewHolder (
        private var view: View
    ): RecyclerView.ViewHolder(view) {
//        var youTubePlayerView : YouTubePlayerView? = view.findViewById(R.id.videoyoutube) as YouTubePlayerView
        var youTubeThumbnailView : YouTubeThumbnailView? = view.findViewById(R.id.video_thumbnail_image_view) as YouTubeThumbnailView
        var youTubeCardView : CardView? = view.findViewById(R.id.youtube_row_card_view) as CardView
    }


}