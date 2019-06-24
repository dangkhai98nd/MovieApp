package com.example.glidetest.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.glidetest.DetailActivity
import com.example.glidetest.R
import com.example.retrofittest.Models.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_card.*
import java.util.logging.Handler as Handler1


class MoviesAdapter(
    private var mContext: Context?
) : RecyclerView.Adapter<MoviesAdapter.ItemViewHolder>() {


    protected var movieList: MutableList<Movie> = arrayListOf()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {


        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_card, p0, false)
        return ItemViewHolder(view)


    }


    override fun getItemCount(): Int = (movieList.size)


    override fun onBindViewHolder(p0: ItemViewHolder, p1: Int) {

        p0.bind()


    }


    inner class ItemViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind() {
            val movie = movieList[adapterPosition]
            title?.text = movie.original_title
            val vote: String? =movie.vote_average.toString()
            rating?.text = vote



            Glide.with(mContext ?: return)
                .load(movie.get_poster_path())
                .thumbnail(Glide.with(mContext!!).load(R.drawable.icon_load))
                .fitCenter()
//            .apply(RequestOptions().placeholder(R.drawable.load_icon))
                .into(thumbnail!!)
        }

        val value = containerView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {

                    val intent = Intent(mContext, DetailActivity::class.java)

                    intent.putExtra("movieID", movieList[pos].id)

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext?.startActivity(intent)


                }
            }
        })

    }


    fun addAll(movies: List<Movie>) {
        this.movieList.addAll(movies)
        notifyDataSetChanged()
    }
}



