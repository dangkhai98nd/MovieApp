package com.example.glidetest.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.glidetest.DetailActivity
import com.example.glidetest.R
import com.example.retrofittest.Models.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_card.*
import kotlinx.android.synthetic.main.placeholderlayout.*


class MoviesAdapter(
    private var mContext: Context
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private var DURATION: Long = 500
    private var on_attach = true
    protected var movieList: MutableList<Movie> = arrayListOf()
    private var listener : ItemMovieClickListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
//        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_card, p0, false)
//        return ItemViewHolder(view)
        return when (p1) {
            0 -> ItemViewHolder(
                LayoutInflater.from(p0.context).inflate(
                    R.layout.movie_card,
                    p0,
                    false
                )
            )
            else -> ItemPalceViewHolder(
                LayoutInflater.from(p0.context).inflate(
                    R.layout.placeholderlayout,
                    p0,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (movieList.size - position < 3)
            return 1
        return 0
    }

    override fun getItemCount(): Int = (movieList.size + 2)

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        when (p0) {
            is ItemViewHolder -> {
                p0.bind()
            }
            is ItemPalceViewHolder -> {
                p0.bind()
            }
            else -> {
                Log.e("error", "error")
            }
        }
//        val movie = movieList[p1]
//        p0.title?.text = movie.original_title
////        p0.title.isSelected = true
//        val vote: String? =movie.vote_average.toString()
//        p0.rating?.text = vote
//        Glide.with(mContext)
//            .load(movie.get_poster_path())
//            .thumbnail(Glide.with(mContext).load(R.drawable.icon_load))
//            .fitCenter()
////            .apply(RequestOptions().placeholder(R.drawable.load_icon))
//            .into(p0.thumbnail)

//        setAnimation(p0.itemView,p1)
    }

    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!on_attach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) DURATION / 2 else i * DURATION / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {

        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                Log.d(TAG, "onScrollStateChanged: Called $newState")
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.onAttachedToRecyclerView(recyclerView)
    }

    inner class ItemPalceViewHolder(
        override val containerView: View
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
            shimmer_view_container.startShimmerAnimation()
        }
    }

    inner class ItemViewHolder(
        override val containerView: View
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind() {
            val movie = movieList[adapterPosition]
            title?.text = movie.original_title
            val vote: String? = movie.vote_average.toString()
            rating?.text = vote



            Glide.with(mContext ?: return)
                .load(movie.get_poster_path())
                .thumbnail(Glide.with(mContext!!).load(R.drawable.icon_load))
                .fitCenter()
//            .apply(RequestOptions().placeholder(R.drawable.load_icon))
                .into(thumbnail)
        }

        val value = containerView.setOnClickListener {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {

//                val intent = Intent(mContext, DetailActivity::class.java)
//
//                intent.putExtra("movieID", movieList[pos].id)
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                val options = ActivityOptions.makeSceneTransitionAnimation(
//                        this,
//                        Pair.create(
//                            thumbnail,
//                            "thumbnail"
//                        ),
//                        Pair.create(title, "title")
//                    )
//                mContext.startActivity(intent, options.toBundle())
                listener?.onAction(movieList[pos].id, thumbnail,title)

            }
        }

    }


    fun addAll(movies: List<Movie>) {
        this.movieList.addAll(movies)
        notifyDataSetChanged()
    }

    fun registerItemMovieClickListener (listener : ItemMovieClickListener) {
        this.listener = listener
    }

    interface ItemMovieClickListener {
        fun onAction(movieID : Int?, thumbnail : ImageView, title : TextView)
    }
}



