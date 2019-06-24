package com.example.glidetest.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.glidetest.R
import com.example.retrofittest.Models.Movie
import android.widget.Toast
import java.util.logging.Handler as Handler1
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.bumptech.glide.request.RequestOptions
import com.example.glidetest.DetailActivity


class MoviesAdapter (
    private var mContext : Context?
) : RecyclerView.Adapter<MoviesAdapter.ItemViewHolder> ()  {



    protected var movieList : MutableList<Movie> = arrayListOf()


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MoviesAdapter.ItemViewHolder {


        var view: View? = LayoutInflater.from(p0.context).inflate(R.layout.movie_card, p0, false)
        return ItemViewHolder(view!!,mContext,movieList)


    }



    override fun getItemCount(): Int = (if (movieList == null) 0 else movieList?.size!!)


    override fun onBindViewHolder(p0: MoviesAdapter.ItemViewHolder, p1: Int) {

        p0.title?.setText(movieList?.get(p1)?.original_title)
        var vote: String? = movieList?.get(p1)?.vote_average.toString()
        p0.userrating?.setText(vote)



        Glide.with(this!!.mContext!!)
            .load(movieList?.get(p1)?.get_poster_path())
            .thumbnail(Glide.with(this!!.mContext!!).load(R.drawable.icon_load))
            .fitCenter()
//            .apply(RequestOptions().placeholder(R.drawable.load_icon))
            .into(p0.thumbnail!!)


    }






    public class ItemViewHolder (
        private var view: View,
        private var mContext : Context?,
        private var movieList: List<Movie>
    ): RecyclerView.ViewHolder(view) {
        var title: TextView? = view?.findViewById(R.id.title)
        var userrating: TextView? = view?.findViewById(R.id.rating)
        var thumbnail: ImageView? = view?.findViewById(R.id.thumbnail)

        val value = view?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {


                    val intent = Intent(mContext, DetailActivity::class.java)

//                    var clickedDataItem : Parcelable? = null
//                    clickedDataItem = movieList[pos] as Parcelable
//
//                    var bundle = Bundle()
//                    bundle.putParcelable("movies", clickedDataItem)
//                    intent.putExtra("bundle_movie", bundle )

                    intent.putExtra("movieID",movieList.get(pos).id)



                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    mContext?.startActivity(intent)


                }
            }
        })

    }


    fun addAll(movies : List<Movie>) {
        this.movieList.addAll(movies)
        notifyDataSetChanged()
    }
}



