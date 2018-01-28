package com.github.common.api.service;

import com.github.common.api.model.Movie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zlove on 2018/1/24.
 */

public interface APIService {

    @GET("/v2/movie/top250")
    Observable<Movie> getMovies(@Query("start") int start, @Query("count") int count);
}
