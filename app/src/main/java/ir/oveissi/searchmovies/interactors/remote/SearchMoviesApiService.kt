package ir.oveissi.searchmovies.interactors.remote

import ir.oveissi.searchmovies.pojo.Movie
import ir.oveissi.searchmovies.pojo.TmpMovies

/**
 * Created by abbas on 7/18/16.
 */
interface SearchMoviesApiService {

    suspend fun getMoviesByTitle(query: String, page: Int?): TmpMovies

    suspend fun getMovieById(id: String): Movie
}
