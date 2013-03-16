package com.googolmo.douban.doumailer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.googolmo.douban.doumailer.app.OAuthActivity;

/**
 * User: googolmo
 * Date: 13-2-8
 * Time: 上午10:36
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        BaseApplication app = ((BaseApplication)getApplication());
        if (app.getProvider().getLoginedUserToken() == null) {
            Intent intent = new Intent(this, OAuthActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
