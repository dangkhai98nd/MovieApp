package com.example.glidetest.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.glidetest.BuildConfig
import com.example.glidetest.Models.Video
import com.example.glidetest.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.videolayout.*

class VideoAdapter(
    private var mContext: Context?,
    private var mVideos: List<Video>?
) : androidx.recyclerview.widget.RecyclerView.Adapter<VideoAdapter.ItemViewHolder>() {


    private var selectedPosition: Int = 0


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {


        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.videolayout, p0, false)
        return ItemViewHolder(view)


    }


    override fun getItemCount(): Int = mVideos?.size ?: 0


    override fun onBindViewHolder(p0: ItemViewHolder,  p1: Int) {


        if (selectedPosition == p1) {
            p0.youtube_row_card_view?.setCardBackgroundColor(ContextCompat.getColor(this.mContext ?: return, R.color.red))
        } else {
            p0.youtube_row_card_view?.setCardBackgroundColor(
                ContextCompat.getColor(
                    this.mContext ?: return,
                    R.color.background_color_1
                )
            )
        }


        p0.video_thumbnail_image_view?.initialize(BuildConfig.API_KEY, object : YouTubeThumbnailView.OnInitializedListener {
            override fun onInitializationSuccess(p2: YouTubeThumbnailView?, p3: YouTubeThumbnailLoader?) {
                p3?.setVideo(mVideos?.get(p1)?.key)
                p3?.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                    override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p1: String?) {
                        p3.release()

                    }

                    override fun onThumbnailError(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader.ErrorReason?) {

                    }
                })
            }

            override fun onInitializationFailure(p2: YouTubeThumbnailView?, p3: YouTubeInitializationResult?) {

            }
        })


    }

    fun setSelectedPosition(position: Int) {

        selectedPosition = position
        notifyDataSetChanged()
    }


    class ItemViewHolder(
        override val containerView: View

    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer

}