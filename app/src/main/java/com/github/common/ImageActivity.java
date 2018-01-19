package com.github.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.framework.ui.AnimatedImageView;
import com.github.framework.util.FrescoHelper;

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
    }
}
