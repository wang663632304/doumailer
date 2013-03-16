/*
 * Copyright (c) 2013. By GoogolMo
 */

package com.googolmo.douban.doumailer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.googolmo.douban.dou4j.http.AccessToken;
import com.googolmo.douban.dou4j.model.DoubanException;
import com.googolmo.douban.dou4j.util.DLog;
import com.googolmo.douban.doumailer.Constants;
import com.googolmo.douban.doumailer.MainActivity;
import com.googolmo.douban.doumailer.R;
import com.googolmo.douban.doumailer.data.Provider;

/**
 * User: GoogolMo
 * Date: 13-3-16
 * Time: 上午10:08
 */
public class OAuthFragment extends BaseFragment {
    private static final String TAG = OAuthFragment.class.getName();

    private WebView mWebView;
    private String mCode;
    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.view_oauth, container, false);
        mWebView = (WebView)v.findViewById(R.id.webview);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                DLog.d(TAG, "shouldOverrideUrlLoading:" + url);
                if (url.startsWith(Constants.CALLBACKURL)) {
                    Uri uri = Uri.parse(url);
                    mCode = uri.getQueryParameter("code");
                    DLog.d(TAG, "code:" + mCode);
                    view.stopLoading();
                    TokenTask task = new TokenTask(getProvider(), mCode);
                    task.execute();
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                DLog.d(TAG, "onPageStarted:" + url);
                if (url.startsWith(Constants.CALLBACKURL)) {
                    Uri uri = Uri.parse(url);
                    mCode = uri.getQueryParameter("code");
                    DLog.d(TAG, "code:" + mCode);
                    view.stopLoading();

                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        WebSettings ws = mWebView.getSettings();
        ws.setAppCacheEnabled(false);
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);

        String url = getProvider().getApi().getBaseApi().getAuthorizationUrl();
        mWebView.loadUrl(url);
    }

    private void clearDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

   private class TokenTask extends AsyncTask<Void, Void, AccessToken> {
       private Provider mProvider;
       private String mCode;

       private TokenTask(Provider provider, String code) {
           this.mProvider = provider;
           this.mCode = code;
       }

       @Override
       protected AccessToken doInBackground(Void... voids) {
           try {
               AccessToken t = mProvider.getApi().getBaseApi().oAuth(this.mCode);
               mProvider.addUser(t);
               mProvider.setCurrentUid(t.getUid());
               mProvider.setAccessToken(t);
               return t;
           } catch (DoubanException e) {
               e.printStackTrace();
           }
           return null;
       }

       @Override
       protected void onPreExecute() {
           clearDialog();
           mDialog = new ProgressDialog(getActivity());
           ((ProgressDialog)mDialog).setMessage(getActivity().getString(R.string.authing));
           mDialog.setCancelable(false);
           mDialog.setCanceledOnTouchOutside(false);
           mDialog.show();
           super.onPreExecute();
       }

       @Override
       protected void onPostExecute(AccessToken accessToken) {
           super.onPostExecute(accessToken);
           clearDialog();
           if (accessToken != null) {
               DLog.d(TAG, accessToken.toString());
               Intent intent = new Intent(getActivity(), MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
//               Intent data = new Intent();
//               data.putExtra(Constants.KEY_ACCESSTOKEN, accessToken);
//               getActivity().setResult(Activity.RESULT_OK, data);
//               getActivity().finish();
           }

       }
   }

}
