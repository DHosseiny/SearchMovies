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

package ir.oveissi.searchmovies.interactors


import ir.oveissi.searchmovies.interactors.remote.SearchMoviesApiService
import ir.oveissi.searchmovies.pojo.Movie
import ir.oveissi.searchmovies.utils.DispatcherProvider
import kotlinx.coroutines.experimental.async
import javax.inject.Inject


class MovieInteractorImpl @Inject
constructor(private val searchMoviesApiService: SearchMoviesApiService, private val dispatcherProvider: DispatcherProvider) : MovieInteractor {

    override suspend fun getMoviesByTitle(title: String, page: Int?): List<Movie> {

        return async(dispatcherProvider.backgroundDispatcher()) {
            searchMoviesApiService.getMoviesByTitle(title, page).data
        }.await()
    }

    override suspend fun getMovieByID(id: String): Movie {

        return async(context = dispatcherProvider.backgroundDispatcher()) {
            searchMoviesApiService.getMovieById(id)
        }.await()
    }

}
