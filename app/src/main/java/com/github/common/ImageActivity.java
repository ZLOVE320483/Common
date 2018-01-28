package com.github.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.common.api.ApiConstants;
import com.github.common.api.model.Movie;
import com.github.common.api.service.APIService;
import com.github.framework.ui.AnimatedImageView;
import com.github.framework.util.FrescoHelper;
import com.github.network.ApiRequestManager;
import com.google.gson.annotations.Since;

import java.util.List;
import java.util.function.Predicate;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zlove on 2018/1/17.
 */

public class ImageActivity extends AppCompatActivity {

    private static final String imgUrl = "https://pic3.zhimg.com/v2-5af460972557190bd4306ad66f360d4a.jpg";

    private AnimatedImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (AnimatedImageView) findViewById(R.id.image);
        FrescoHelper.bindImage(imageView, imgUrl);

        ApiRequestManager.getInstance(ApiConstants.BASE_URL)
                .getService(APIService.class)
                .getMovies(0, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Movie movie) {
                        Log.d("ZLOVE", "getTitle---" + movie.getTitle());
                        Log.d("ZLOVE", "getStart---" + movie.getStart());
                        Log.d("ZLOVE", "getCount---" + movie.getCount());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
