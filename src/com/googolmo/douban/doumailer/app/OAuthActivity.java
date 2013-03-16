/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.doumailer.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.googolmo.douban.doumailer.BaseActivity;
import com.googolmo.douban.doumailer.R;
import com.googolmo.douban.doumailer.fragment.OAuthFragment;

/**
 * User: GoogolMo
 * Date: 13-3-16
 * Time: 上午10:08
 */
public class OAuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, Fragment.instantiate(this, OAuthFragment.class.getName(), getIntent().getExtras()));
        ft.commitAllowingStateLoss();
    }
}
