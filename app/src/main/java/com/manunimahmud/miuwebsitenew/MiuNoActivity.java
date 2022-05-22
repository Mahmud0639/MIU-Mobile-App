package com.manunimahmud.miuwebsitenew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MiuNoActivity extends AppCompatActivity {
    private TextView reloadit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miu_no);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        reloadit = findViewById(R.id.reloadit);
        reloadit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isConnected()){
                    Intent intent = new Intent(MiuNoActivity.this,MiuPortalActivity.class);
                    startActivity(intent);
                    MiuPortalActivity.webView.reload();
                    finishAffinity();
                }else if (mobile.isConnected()){
                    Intent intent = new Intent(MiuNoActivity.this,MiuPortalActivity.class);
                    startActivity(intent);
                    MiuPortalActivity.webView.reload();
                    finishAffinity();
                }else {
                    Snackbar.make(view,"Check your internet connection to further proceed",Snackbar.LENGTH_LONG).show();
                }


            }
        });
    }
}