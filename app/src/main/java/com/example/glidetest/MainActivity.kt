package com.example.glidetest


import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.glidetest.adapter.EndlessRecyclerViewScrollListener
import com.example.glidetest.adapter.MoviesAdapter
import com.example.glidetest.api.Client
import com.example.glidetest.api.Service
import com.example.retrofittest.Models.ApiMovies
import com.example.retrofittest.Models.Movie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.movie_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var recycleView: RecyclerView? = null
    var adapter: MoviesAdapter? = null
    var movies: MutableList<Movie>? = null
    var apiMovies: ApiMovies? = null
    var pd: ProgressDialog? = null
    var swipeContainer: SwipeRefreshLayout? = null
    var scrollListener: EndlessRecyclerViewScrollListener? = null
    var page: Int = 1
    var isLoading : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()
        swipeContainer = main_content
        swipeContainer?.setColorSchemeResources(R.color.red)
        swipeContainer?.setOnRefreshListener {

            initViews()

        }


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

        adapter = MoviesAdapter(this)


        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            recycleView?.layoutManager = GridLayoutManager(this, 2)

        } else {
            recycleView?.layoutManager = GridLayoutManager(this, 4)

        }
        recycleView?.itemAnimator = DefaultItemAnimator()
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

        val layoutManager: RecyclerView.LayoutManager? = recycle_view.layoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager!!) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (!(isLoading)) {
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

    private fun loadJSON() {
        try {
            val client: Client? = Client()
            val service: Service? = client?.getClient()?.create(Service::class.java)
            val call: Call<ApiMovies>? = service?.getApiMovies(page)
            call?.run {
                enqueue(object : Callback<ApiMovies> {
                    override fun onFailure(call: Call<ApiMovies>, t: Throwable) {
                        Log.d("Error ", t.message)
                        Toast.makeText(this@MainActivity, "Error Fetching Data!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ApiMovies>, response: Response<ApiMovies>) {
                        apiMovies = response.body()

                        val moviemore: List<Movie> = apiMovies?.results ?: emptyList()

                        adapter?.addAll(moviemore)

                        if (swipeContainer!!.isRefreshing) {
                            swipeContainer!!.isRefreshing = false
                        }
                        pd?.dismiss()

                        if (isLoading) {
                            pbLoading.visibility = ProgressBar.GONE
                            isLoading = false
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






