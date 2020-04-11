package com.example.glidetest


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.glidetest.adapter.EndlessRecyclerViewScrollListener
import com.example.glidetest.adapter.MoviesAdapter
import com.example.glidetest.api.Client
import com.example.glidetest.api.Service
import com.example.retrofittest.Models.ApiMovies
import com.example.retrofittest.Models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    var recycleView: androidx.recyclerview.widget.RecyclerView? = null
    var adapter: MoviesAdapter? = null
    var movies: MutableList<Movie>? = null
    var apiMovies: ApiMovies? = null
    var pd: ProgressDialog? = null
    var swipeContainer: SwipeRefreshLayout? = null
    var scrollListener: EndlessRecyclerViewScrollListener? = null
    var page: Int = 1
    var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()
        swipeContainer = main_content
        swipeContainer?.setColorSchemeResources(R.color.red)
        swipeContainer?.setOnRefreshListener {

            initViews()
//            ádfsadf
        }
        transparentStatusBar()

    }

    private fun transparentStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.navigationBarColor = Color.TRANSPARENT
        window.statusBarColor = Color.TRANSPARENT
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

    private fun initViews() {
        page = 1
        movies = null
        apiMovies = null
        pd = ProgressDialog(this)
        pd?.setMessage("Load movie...")
        pd?.setCancelable(false)
        pd?.show()
        recycleView = recycle_view

        adapter = MoviesAdapter(this).also {
            it.registerItemMovieClickListener(object : MoviesAdapter.ItemMovieClickListener {
                override fun onAction(movieID: Int?, thumbnail: ImageView, title: TextView) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        this@MainActivity,
                        Pair.create<View, String>(thumbnail, "thumbnail")
                    )
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("movieID",movieID)
                    startActivity(intent, options.toBundle())
                }
            })
        }


        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            recycleView?.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(this, 2)

        } else {
            recycleView?.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(this, 4)

        }
        recycleView?.itemAnimator =
            androidx.recyclerview.widget.DefaultItemAnimator()
//        recycleView?.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayout.HORIZONTAL,false)
//        recycleView?.onFlingListener(object : RecyclerView.OnFlingListener(){
//            override fun onFling(p0: Int, p1: Int): Boolean {
//                if (Math.abs(p1) > 0.7) {
//                    var y  = 1 * Math.signum(1 as Double).toInt()
//                    recycleView!!.fling(p0, y)
//                    return true
//                }
//
//                return false
//            }
//        })

//        recycleView?.fling(2,2)


//        RecyclerView.SmoothScroller smoothScroller = new FlexiSmoothScroller(getContext())
//            .setMillisecondsPerInchSearchingTarget(100f));
//        smoothScroller.setTargetPosition(targetPos);
//        recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);


        recycleView?.adapter = adapter

        val layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? =
            recycle_view.layoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager!!) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: androidx.recyclerview.widget.RecyclerView
            ) {
                if (!(isLoading)) {
                    if (apiMovies?.page == apiMovies?.total_pages)
                        return
                    this@MainActivity.page++
                    pbLoading.visibility = ProgressBar.VISIBLE
                    isLoading = true
                    loadJSON()
                    scrollListener?.resetState()
                }
            }

        }
        recycle_view.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)

        loadJSON()
        adapter?.notifyDataSetChanged()


    }

    @SuppressLint("CheckResult")
    private fun loadJSON() {
        try {
            val client: Client? = Client()
            val service: Service? = client?.client()?.create(Service::class.java)

            service!!.getApiMoviesObservable(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    apiMovies = it
                    val moviemore: List<Movie> = apiMovies?.results ?: emptyList()

                    adapter?.addAll(moviemore)

                    if (swipeContainer?.isRefreshing == true) {
                        swipeContainer?.isRefreshing = false
                    }
                    pd?.dismiss()

                    if (isLoading) {
                        pbLoading?.visibility = ProgressBar.GONE
                        isLoading = false
                    }
                }, {
                    it.printStackTrace()
                })
//            val call: Call<ApiMovies>? = service?.getApiMovies(page)
//            call?.run {
//                enqueue(object : Callback<ApiMovies> {
//                    override fun onFailure(call: Call<ApiMovies>, t: Throwable) {
//                        Log.d("Error ", t.message)
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Error Fetching Data!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onResponse(call: Call<ApiMovies>, response: Response<ApiMovies>) {
//                        apiMovies = response.body()
//
//                        val moviemore: List<Movie> = apiMovies?.results ?: emptyList()
//
//                        adapter?.addAll(moviemore)
//
//                        if (swipeContainer!!.isRefreshing) {
//                            swipeContainer!!.isRefreshing = false
//                        }
////                        pd?.dismiss()
//
//                        if (isLoading) {
//                            pbLoading.visibility = ProgressBar.GONE
//                            isLoading = false
//                        }
//                    }
//
//                })
//            }
        } catch (e: Exception) {
            Log.d("Error ", e.message)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}






