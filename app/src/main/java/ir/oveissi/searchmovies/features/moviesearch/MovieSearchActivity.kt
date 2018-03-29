package ir.oveissi.searchmovies.features.moviesearch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.miguelcatalan.materialsearchview.MaterialSearchView
import ir.oveissi.searchmovies.R
import ir.oveissi.searchmovies.SearchMovieApplication
import ir.oveissi.searchmovies.features.moviedetail.MovieDetailActivity
import ir.oveissi.searchmovies.pojo.Movie
import ir.oveissi.searchmovies.utils.customviews.EndlessLinearLayoutRecyclerview
import ir.oveissi.searchmovies.utils.customviews.LoadingLayout
import java.util.*
import javax.inject.Inject


class MovieSearchActivity : AppCompatActivity(), MovieSearchContract.View, MovieSearchAdapter.ItemClickListener {

    @Inject
    @JvmField
    var mPresenter: MovieSearchPresenter? = null
    var title = ""
    var current_page = 1
    @BindView(R.id.rvMovies)
    @JvmField
    internal var rvMovies: EndlessLinearLayoutRecyclerview? = null
    @BindView(R.id.search_view)
    @JvmField
    internal var searchView: MaterialSearchView? = null
    @BindView(R.id.loadinglayout)
    @JvmField
    internal var loadinglayout: LoadingLayout? = null
    @BindView(R.id.myToolbar)
    @JvmField
    internal var toolbar: Toolbar? = null
    private var mListAdapter: MovieSearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchMovieApplication.getComponent().plus(MovieSearchPresenterModule()).inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        loadinglayout!!.state = LoadingLayout.STATE_SHOW_DATA
        loadinglayout!!.setListener { mPresenter!!.onSearchButtonClick(title) }


        mListAdapter = MovieSearchAdapter(this@MovieSearchActivity, ArrayList())
        mListAdapter!!.setItemClickListener(this)

        rvMovies!!.adapter = mListAdapter
        rvMovies!!.layoutManager = LinearLayoutManager(this)
        rvMovies!!.setOnLoadMoreListener {
            mPresenter!!.onLoadMoviesByTitle(title, current_page)
            current_page++
        }

        searchView!!.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                submitQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO: simulate debounce
                if (!newText.isNullOrBlank()) {
                    submitQuery(newText!!)
                }
                return false
            }
        })


        mPresenter!!.onViewAttached(this)
        mPresenter!!.subscribe()

        mPresenter!!.onLoadMoviesByTitle(title, 1)
        current_page++
    }

    private fun submitQuery(inputSearch: String) {
        title = inputSearch
        current_page = 1
        mPresenter!!.onSearchButtonClick(inputSearch)
        current_page++
    }

    override fun onStop() {
        super.onStop()
        mPresenter!!.unsubscribe()
    }


    override fun clearMovies() {
        mListAdapter!!.clear()
    }

    override fun showToast(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show()
    }


    override fun showLoadingForMovies() {
        loadinglayout!!.state = LoadingLayout.STATE_LOADING

    }

    override fun hideLoadingForMovies() {
        if (loadinglayout!!.state != LoadingLayout.STATE_SHOW_DATA)
            loadinglayout!!.state = LoadingLayout.STATE_SHOW_DATA
    }

    override fun showMoreMovies(movies: List<Movie>) {
        rvMovies!!.setLoading(false)
        for (p in movies) {
            mListAdapter!!.addItem(p)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_search)
        searchView!!.setMenuItem(item)

        return true
    }


    override fun ItemClicked(position: Int, item: Movie, imPoster: ImageView) {
        val i = Intent(this@MovieSearchActivity, MovieDetailActivity::class.java)
        i.putExtra("movie_id", item.id.toString())
        i.putExtra("image_path", item.poster)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MovieSearchActivity, imPoster, "imPoster")
            startActivity(i, option.toBundle())
        } else {
            startActivity(i)
        }
    }
}


