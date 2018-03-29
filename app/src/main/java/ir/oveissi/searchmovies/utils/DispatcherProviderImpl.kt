package ir.oveissi.searchmovies.utils


import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import javax.inject.Inject

/**
 * Created by Abbas on 30/04/16.
 */
class DispatcherProviderImpl @Inject
constructor() : DispatcherProvider {

    private var POOL = newFixedThreadPoolContext(2 * Runtime.getRuntime().availableProcessors(), "bg")

    override fun backgroundDispatcher(): CoroutineDispatcher {
        return POOL
    }

}
