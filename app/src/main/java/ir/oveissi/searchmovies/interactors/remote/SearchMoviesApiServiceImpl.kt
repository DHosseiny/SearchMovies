package ir.oveissi.searchmovies.interactors.remote

import com.google.gson.Gson
import io.reactivex.Observable
import ir.oveissi.searchmovies.pojo.Movie
import ir.oveissi.searchmovies.pojo.TmpMovies
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.channels.SendChannel
import kotlinx.coroutines.experimental.rx2.rxObservable
import retrofit2.HttpException
import java.io.IOException


/**
 * Created by abbas on 7/18/16.
 */
class SearchMoviesApiServiceImpl(private val api: ApiInterface) : SearchMoviesApiService {

    override fun getMoviesByTitle(query: String, page: Int?): Observable<TmpMovies> {
        return rxObservable {
            val tmpMoviesDeferred = api.getMoviesByTitle(query, page)
            sendWithParseError(tmpMoviesDeferred)
        }
    }

    override fun getMovieById(id: String): Observable<Movie> {
        return rxObservable {
            val movieDeferred = api.getMovieById(id)
            sendWithParseError(movieDeferred)
        }
    }

    private suspend fun <E> SendChannel<E>.sendWithParseError(deferred: Deferred<E>) {

        try {
            val body = deferred.await()
            send(body)//invokes observable onNext
            //observable onComplete invokes as job done
        } catch (e: Exception) {

            val throwable = deferred.getCompletionExceptionOrNull()

            if (throwable is HttpException) {

                val gson = Gson()
                try {
                    val generalApiException = gson.fromJson(throwable.response().errorBody()!!.string(), GeneralApiException::class.java)
                    close(generalApiException)//invokes observable onError
                    return
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            // if not the kind we're interested in, then just report the same error to onError
            close(throwable)//invokes observable onError
        }
    }

}


