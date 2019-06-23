package com.example.glidetest

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.glidetest.Models.ApiImages
import com.example.glidetest.Models.ApiVideos
import com.example.glidetest.Models.Video
import com.example.glidetest.adapter.BackdropAdapter
import com.example.glidetest.adapter.VideoAdapter
import com.example.glidetest.api.Client
import com.example.glidetest.api.Service
import com.example.glidetest.utils.RecyclerViewOnClickListener
import com.example.retrofittest.Models.Movie
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.videolayout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response






class DetailActivity : AppCompatActivity() {

    var recycleView : RecyclerView? = null
    var adapter : BackdropAdapter? = null
    var adapter_videos : VideoAdapter? = null
    var recyclerViewVideos : RecyclerView? = null
    var imagesBackdrop : List<ApiImages.Image>? = null
    var movieID : Int = 320288
    var movie : Movie? = null
    var videos : List<Video>? = null
    var pd : ProgressDialog? = null
    var youTubeFragment : YouTubePlayerFragment? = null
    var youTubePlayerVideos: YouTubePlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        pd = ProgressDialog(this)
        pd!!.setMessage("Load ...")
        pd!!.setCancelable(false)
        pd!!.show()
        movieID = intent.extras.getInt("movieID")
        recycleView = recycle_view_backdrop

        adapter = BackdropAdapter(this@DetailActivity)

        recycleView?.itemAnimator = DefaultItemAnimator()
        recycleView?.layoutManager = LinearLayoutManager(this@DetailActivity,LinearLayoutManager.HORIZONTAL,false)
        recycleView?.adapter = adapter


        loadJSONDetail()
        loadJSONTrailer()
        loadJSONImage()
        adapter?.notifyDataSetChanged()
        adapter_videos?.notifyDataSetChanged()

    }

    private fun populateRecyclerViewVideos() {
        adapter_videos = VideoAdapter(this,videos)
        recyclerViewVideos?.adapter = adapter_videos
        recyclerViewVideos?.addOnItemTouchListener(RecyclerViewOnClickListener(this,object : RecyclerViewOnClickListener.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                if (youTubeFragment != null && youTubePlayerVideos != null) {
                    adapter_videos?.setSelectedPosition(position)


                    youTubePlayerVideos?.loadVideo(videos?.get(position)?.key)
                }
            }
        }))
    }

    private fun setUpRecyclerViewVideos() {
        recyclerViewVideos = recycle_view_video
        recyclerViewVideos?.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewVideos?.layoutManager = linearLayoutManager
    }

    private fun loadJSONImage() {
        try {
            val client : Client? = Client()
            var service : Service? = client!!.getClient()?.create(Service::class.java)
            var call : Call<ApiImages>? = service?.getApiImages(movieID)
            call?.run {
                enqueue(object : Callback<ApiImages> {
                    override fun onFailure(call: Call<ApiImages>, t: Throwable) {
                        Log.d("Error ",t.message)
                        Toast.makeText(this@DetailActivity, "Error Fetching Data!" , Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ApiImages>, response: Response<ApiImages>) {
                        imagesBackdrop = response.body()?.backdrops!!


//                        Glide.with(this@DetailActivity)
//                            .load(imagesBackdrop.get(1).get_image_path())
//                            .into(backdropbottom)
                        adapter?.addAll(imagesBackdrop)
                        if (imagesBackdrop?.size != 0 || imagesBackdrop != null) {
                            backdrop_txt.setText("Backdrop : ")
                        }
                        else
                        {
                            backdrop_txt.visibility = TextView.GONE
                        }
                    }

                } )
            }
        }
        catch (e : Exception)
        {
            Log.d("Error ",e.message)
            Toast.makeText(this@DetailActivity,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadJSONDetail()
    {
        try {
            val client : Client? = Client()
            var service : Service? = client!!.getClient()?.create(Service::class.java)
            var call : Call<Movie>? = service?.getApiMovieDetail(movieID)
            call?.run {
                enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.d("Error ",t.message)
                        Toast.makeText(this@DetailActivity, "Error Fetching Data!" , Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        movie = response.body()
//                        Toast.makeText(this@DetailActivity, "${movie?.name}", Toast.LENGTH_SHORT).show()
//                        Glide.with(this@DetailActivity)
//                            .load(movie?.get_backdrop_path())
//                            .into(backdrop)
                        Glide.with(this@DetailActivity)
                            .load(movie?.get_poster_path())
                            .thumbnail(Glide.with(this@DetailActivity).load(R.drawable.icon_load))
                            .fitCenter()
                            .into(thumbnail_image_header)

                        thumbnail_image_header.clipToOutline = true


                        name.setText(movie?.name)
                        userrating.setText("Rating : ${movie?.vote_average.toString()}")
                        if (movie?.runtime != null) {
                            time.setText("Time : ${movie?.runtime!! / 60 as Int}h${movie?.runtime!! % 60}")
                        }
                        else {
                            time.visibility = TextView.INVISIBLE
                        }
                        releasedate.setText("Release date : ${movie?.release_date}")
                        plotsynopsis.setText("Overview : ${movie?.overview}")


                    }

                } )
            }
        }
        catch (e : Exception)
        {
            Log.d("Error ",e.message)
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadJSONTrailer()
    {
        try {
            val client : Client? = Client()
            var service : Service? = client!!.getClient()?.create(Service::class.java)
            var call : Call<ApiVideos>? = service?.getApiMovieVideos(movieID)
            call?.run {
                enqueue(object : Callback<ApiVideos> {
                    override fun onFailure(call: Call<ApiVideos>, t: Throwable) {
                        Log.d("Error ",t.message)
                        Toast.makeText(this@DetailActivity, "Error Fetching Data Trailer!" , Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ApiVideos>, response: Response<ApiVideos>) {
                        videos = response.body()?.results

                        if (videos?.size != 0 ) {
                            youTubeFragment = fragmentManager.findFragmentById(R.id.videoyoutube) as YouTubePlayerFragment
                            youTubeFragment?.initialize(BuildConfig.API_KEY,
                                object : YouTubePlayer.OnInitializedListener {
                                    override fun onInitializationSuccess(
                                        provider: YouTubePlayer.Provider,
                                        youTubePlayer: YouTubePlayer, b: Boolean
                                    ) {

                                        if (!b) {
                                            youTubePlayerVideos = youTubePlayer
                                            var vote_average: Float = (if (movie?.vote_average!! == null) 0.0F else movie?.vote_average!!)

                                            youTubePlayerVideos?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                                            if (vote_average < 5) {
                                                youTubePlayerVideos?.cueVideo(videos?.get(0)?.key)
                                            } else {
                                                youTubePlayerVideos?.loadVideo(videos?.get(0)?.key)

                                            }

                                            setUpRecyclerViewVideos()
                                            populateRecyclerViewVideos()
                                        }
                                    }

                                    override fun onInitializationFailure(
                                        provider: YouTubePlayer.Provider,
                                        youTubeInitializationResult: YouTubeInitializationResult
                                    ) {
//
                                    }
                                })


//                            adapter_videos?.addAll(videos)


                        }
                        else {
//                            backdrop.visibility = ImageView.VISIBLE
//                            Glide.with(this@DetailActivity)
//                                .load(movie?.get_backdrop_path())
//                                .into(backdrop)
                            layout_video.visibility = RelativeLayout.GONE
                            Toast.makeText(this@DetailActivity,"null video",Toast.LENGTH_SHORT).show()
                        }
                        pd?.dismiss()

                    }

                } )
            }
        }
        catch (e : Exception)
        {
            Log.d("Error ",e.message)
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

}
