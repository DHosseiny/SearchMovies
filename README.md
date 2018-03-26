# Migrating Search Movies to kotlin coroutines [![Build Status](https://travis-ci.org/DHosseiny/SearchMovies.svg?branch=master)](https://travis-ci.org/DHosseiny/SearchMovies)

This project demonstrates how to migrage from RxJava2 to kotlin coroutines(see this [compare](https://github.com/DHosseiny/SearchMovies/compare/5e911ff...b52e6a4)).

This project is an example of MVP architecture pattern, which uses tools and libraries like [Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines.html), [Retrofit](http://square.github.io/retrofit/)(with [kotlin coroutines adapter](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter)), [RxJava 2](http://reactivex.io/)(with [kotlinx-coroutines-rx2](https://github.com/Kotlin/kotlinx.coroutines/tree/master/reactive/kotlinx-coroutines-rx2) utilities), [Dagger 2](https://google.github.io/dagger/) and [Picasso](http://square.github.io/picasso/). It recieves information from a public web service named [Movies API](http://www.moviesapi.ir/). It is an ongoing project and it's gonna modified by time.

//TODO: [master](https://github.com/DHosseiny/SearchMovies/tree/master) branch is there for full migration and [coroutineRx](https://github.com/DHosseiny/SearchMovies/tree/coroutineRX) branch is for base implemention with coroutines and other parts of code will continue work with RxJava2.

[Demo Apk](https://github.com/abbas-oveissi/SearchMovies/releases/download/v3.0.0/SearchMovies-normal-debug-3.0.0.apk)
## Demo

![Alt Text](http://oveissi.ir/githubAssets/searchmovie3.gif)
