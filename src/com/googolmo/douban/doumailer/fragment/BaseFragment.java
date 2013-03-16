/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.doumailer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.googolmo.douban.doumailer.BaseApplication;
import com.googolmo.douban.doumailer.data.Provider;

/**
 * User: GoogolMo
 * Date: 13-3-16
 * Time: 上午10:21
 */
public class BaseFragment extends SherlockFragment{
    protected Provider mProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mProvider = ((BaseApplication)getActivity().getApplication()).getProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public Provider getProvider(){
        return mProvider;
    }
}
