package ir.oveissi.searchmovies.interactors.remote

import com.google.gson.Gson
import ir.oveissi.searchmovies.pojo.Movie
import ir.oveissi.searchmovies.pojo.TmpMovies
import kotlinx.coroutines.experimental.Deferred
import retrofit2.HttpException
import java.io.IOException


/**
 * Created by abbas on 7/18/16.
 */
class SearchMoviesApiServiceImpl(private val api: ApiInterface) : SearchMoviesApiService {

    override suspend fun getMoviesByTitle(query: String, page: Int?): TmpMovies {
        return api.getMoviesByTitle(query, page).awaitWithParseError()
    }

    override suspend fun getMovieById(id: String): Movie {
        return api.getMovieById(id).awaitWithParseError()
    }

    private suspend fun <R> Deferred<R>.awaitWithParseError(): R {

        try {
            return await()
        } catch (throwable: Exception) {

            if (throwable is HttpException) {

                val gson = Gson()
                try {
                    val generalApiException = gson.fromJson(throwable.response().errorBody()!!.string(), GeneralApiException::class.java)
                    throw generalApiException//invokes observable onError
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            // if not the kind we're interested in, then just report the same error to onError
            throw throwable//invokes observable onError
        }
    }

}


