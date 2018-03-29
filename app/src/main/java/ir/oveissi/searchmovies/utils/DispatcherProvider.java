package ir.oveissi.searchmovies.utils;


import kotlinx.coroutines.experimental.CoroutineDispatcher;

/**
 * Created by Abbas on 30/04/16.
 */
public interface DispatcherProvider {

    CoroutineDispatcher backgroundDispatcher();

}
