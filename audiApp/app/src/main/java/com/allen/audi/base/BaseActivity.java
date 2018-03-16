package com.allen.audi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @className:
 * @classDescription:
 * @Author: allen
 * @createTime: 2018/3/8.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        ButterKnife.bind(this);
        init();
    }

    protected abstract int getLayout();

    protected abstract void init();
}
