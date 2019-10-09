package com.example.glidetest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
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
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    var adapter: BackdropAdapter? = null
    var adapter_videos: VideoAdapter? = null
    var recyclerViewVideos: RecyclerView? = null
    var imagesBackdrop: List<ApiImages.Image> = listOf()
    var imagesPoster: List<ApiImages.Image> = listOf()
    var movieID: Int? = null
    var movie: Movie? = null
    var videos: List<Video>? = null
    var pd: ProgressDialog? = null
    var youTubeFragment: YouTubePlayerFragment? = null
    var youTubePlayerVideos: YouTubePlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        pd = ProgressDialog(this)
        pd?.setMessage("Load ...")
        pd?.setCancelable(false)
        pd?.show()
        movieID = intent?.extras?.getInt("movieID")

        adapter = BackdropAdapter(this@DetailActivity)

        recycle_view_backdrop?.itemAnimator = DefaultItemAnimator()
        recycle_view_backdrop?.layoutManager =
            LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        recycle_view_backdrop?.adapter = adapter



        loadJSONTrailer()

        transparentStatusBar()

    }

    private fun transparentStatusBar() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
//        window.navigationBarColor = Color.TRANSPARENT
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun populateRecyclerViewVideos() {
        adapter_videos = VideoAdapter(this, videos)
        recyclerViewVideos?.adapter = adapter_videos
        recyclerViewVideos?.addOnItemTouchListener(
            RecyclerViewOnClickListener(this,
                object : RecyclerViewOnClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        if (youTubeFragment != null && youTubePlayerVideos != null) {
                            adapter_videos?.setSelectedPosition(position)

                            youTubePlayerVideos?.loadVideo(videos?.get(position)?.key)
                        }
                    }
                })
        )
    }

    private fun setUpRecyclerViewVideos() {
        recyclerViewVideos = recycle_view_video
        recyclerViewVideos?.setHasFixedSize(true)

        recyclerViewVideos?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadJSONImage() {
        try {
            val client: Client? = Client()
            val service: Service? = client!!.getClient()?.create(Service::class.java)
            val call: Call<ApiImages>? = service?.getApiImages(movieID ?: return)
            call?.run {
                enqueue(object : Callback<ApiImages> {
                    override fun onFailure(call: Call<ApiImages>, t: Throwable) {
                        Log.d("Error ", t.message)
                        Toast.makeText(
                            this@DetailActivity,
                            "Error Fetching Data!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    override fun onResponse(call: Call<ApiImages>, response: Response<ApiImages>) {
                        imagesBackdrop = response.body()?.backdrops ?: listOf()
                        imagesPoster = response.body()?.posters ?: listOf()
                        loadJSONDetail()

                        adapter?.addAll(imagesBackdrop)
                        if (imagesBackdrop.size != 0) {
                            backdrop_txt.text = "Backdrop : "
                        } else {
                            backdrop_txt.visibility = TextView.GONE
                        }
                    }

                })
            }
        } catch (e: Exception) {
            Log.d("Error ", e.message)
            Toast.makeText(this@DetailActivity, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadJSONDetail() {
        try {
            val client: Client? = Client()
            val service: Service? = client!!.getClient()?.create(Service::class.java)
            val call: Call<Movie>? = service?.getApiMovieDetail(movieID ?: return)
            call?.run {
                enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.d("Error ", t.message)
                        Toast.makeText(
                            this@DetailActivity,
                            "Error Fetching Data!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        movie = response.body()

                        Glide.with(this@DetailActivity)
                            .load(movie?.get_poster_path())
                            .thumbnail(Glide.with(this@DetailActivity).load(R.drawable.icon_load))
                            .fitCenter()
                            .into(thumbnail_image_header)
                        var image_background : String? = movie?.poster_path
                        if (imagesPoster.size > 1)
                            image_background = imagesPoster[1].get_image_path()
                        if (image_background != null) {
                            Glide.with(this@DetailActivity)
                                .load(image_background)
                                .apply(bitmapTransform(BlurTransformation(22)))
                                .into(backgroud_image)
                        }
                        thumbnail_image_header.clipToOutline = true


                        name.text = movie?.name
                        userrating.text = "Rating : ${movie?.vote_average.toString()}"
                        val runtime = movie?.runtime
                        if (runtime != null) {
                            time.text = "Time : ${runtime / 60}h${runtime % 60}"
                        } else {
                            time.visibility = TextView.GONE
                        }
                        releasedate.text = "Release date : ${movie?.release_date}"
                        plotsynopsis.text = "Overview : ${movie?.overview}"
                        pd?.dismiss()
                    }
                })
            }
        } catch (e: Exception) {
            Log.d("Error ", e.message)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadJSONTrailer() {
        try {
            val client: Client? = Client()
            val service: Service? = client!!.getClient()?.create(Service::class.java)
            val call: Call<ApiVideos>? = service?.getApiMovieVideos(movieID ?: return)
            call?.run {
                enqueue(object : Callback<ApiVideos> {
                    override fun onFailure(call: Call<ApiVideos>, t: Throwable) {
                        Log.d("Error ", t.message)
                        Toast.makeText(
                            this@DetailActivity,
                            "Error Fetching Data Trailer!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(call: Call<ApiVideos>, response: Response<ApiVideos>) {
                        videos = response.body()?.results
                        loadJSONImage()

                        if (videos != null) {
                            youTubeFragment =
                                fragmentManager.findFragmentById(R.id.videoyoutube) as YouTubePlayerFragment
                            youTubeFragment?.initialize(BuildConfig.API_KEY,
                                object : YouTubePlayer.OnInitializedListener {
                                    override fun onInitializationSuccess(
                                        provider: YouTubePlayer.Provider,
                                        youTubePlayer: YouTubePlayer, b: Boolean
                                    ) {
                                        youTubePlayerVideos = youTubePlayer
                                        val vote_average: Float =
                                            (movie?.vote_average ?: 0.0F)

                                        youTubePlayerVideos?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                                        if (vote_average < 5) {
                                            youTubePlayerVideos?.cueVideo(videos?.get(0)?.key)
                                        } else {
                                            youTubePlayerVideos?.loadVideo(videos?.get(0)?.key)

                                        }

                                        setUpRecyclerViewVideos()
                                        populateRecyclerViewVideos()
                                    }

                                    override fun onInitializationFailure(
                                        provider: YouTubePlayer.Provider,
                                        youTubeInitializationResult: YouTubeInitializationResult
                                    ) {

                                    }
                                })

                        } else {

                            layout_video.visibility = RelativeLayout.GONE

                        }

                    }
                })
            }
        } catch (e: Exception) {
            Log.d("Error ", e.message)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}

