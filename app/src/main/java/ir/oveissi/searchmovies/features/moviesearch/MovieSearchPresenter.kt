/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.oveissi.searchmovies.features.moviesearch

import android.util.Log
import ir.oveissi.searchmovies.interactors.MovieInteractor
import ir.oveissi.searchmovies.interactors.remote.GeneralApiException
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.JobCancellationException
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.HttpException
import javax.inject.Inject

class MovieSearchPresenter @Inject
constructor(private val mMovieInteractor: MovieInteractor) : MovieSearchContract.Presenter {
    private lateinit var viewLayer: MovieSearchContract.View
    private val compositeDisposable = mutableListOf<Job>()

    override fun onLoadMoviesByTitle(title: String, page: Int) {

        val job = launch(UI) {
            try {
                val movies = mMovieInteractor.getMoviesByTitle(title, page)
                viewLayer.hideLoadingForMovies()
                viewLayer.showMoreMovies(movies)
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> Log.d(TAG, "onError StatusCode: " + e.code())
                    is GeneralApiException -> Log.d(TAG, "onError message: " + e.message())
                    else -> Log.d(TAG, "onError")
                }
                if (e !is JobCancellationException)
                    viewLayer.showToast("خطا رخ داد.")
            }
        }
        compositeDisposable.add(job)
    }

    override fun onSearchButtonClick(terms: String) {

        viewLayer.showLoadingForMovies()
        viewLayer.clearMovies()
        onLoadMoviesByTitle(terms, 1)
    }


    override fun subscribe() {
    }

    override fun unsubscribe() {
        //Real implementation(retrofit coroutines adapter) not supports cancel for now.
        compositeDisposable.forEach { it.cancel() }
    }

    override fun onViewAttached(view: MovieSearchContract.View) {
        viewLayer = view
    }

    companion object {

        private const val TAG = "MovieSearchPresenter"
    }
}
