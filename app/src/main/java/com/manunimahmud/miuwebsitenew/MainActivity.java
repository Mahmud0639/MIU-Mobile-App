package com.manunimahmud.miuwebsitenew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static WebView webView;
    public static SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    public static LinearLayout successLayout;

    private int darkorlightmode = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        successLayout = findViewById(R.id.successLayout);


        webView = findViewById(R.id.webView);
        swipeRefreshLayout = findViewById(R.id.reload);
        progressBar = findViewById(R.id.progressBar);

        checkConnection();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                checkConnection();

                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (swipeRefreshLayout.isRefreshing()) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               // view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);

                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    setTitle("Loading...");
                }
            }
        });
        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (webView.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isConnected()) {
                    webView.reload();
                } else if (mobile.isConnected()) {
                    webView.reload();
                } else {
                    checkConnection();
                    finishAffinity();
                }


                progressBar.setVisibility(View.GONE);
            }
        });
    }

 /*   @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finishAffinity();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.theme:
                if (darkorlightmode == 0) {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                        darkorlightmode = 1;
                    }
                } else {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                        darkorlightmode = 0;
                    }
                }
                break;
            case R.id.gotoEmsLogin:
                Intent intent = new Intent(MainActivity.this, MiuPortalActivity.class);
                startActivity(intent);

                break;
            case R.id.refresh:
                ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifi.isConnected()) {
                    webView.reload();
                } else if (mobile.isConnected()) {
                    webView.reload();
                } else {
                    checkConnection();
                    finishAffinity();
                }
                break;
            case R.id.rate:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                String subject = "Manarat International University";
                String text = "Through this app you will be able to know all the information of Manarat International University.\nClick on the link below to download this app from play store.\ncom.manunimahmud.miuwebsitenew";
                intent1.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent1.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent1, "Share with"));
                break;
            case R.id.developer:
                Intent intent2 = new Intent(MainActivity.this, DeveloperActivity.class);
                startActivity(intent2);
                break;
            case R.id.goHome:
                webView.loadUrl("https://manarat.ac.bd/");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected()) {
            webView.loadUrl("https://manarat.ac.bd/");
        } else if (mobile.isConnected()) {
            webView.loadUrl("https://manarat.ac.bd/");
        } else {
            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired
    private long mBackPressed;

    // When user click bakpress button this method is called
    @Override
    public void onBackPressed() {

        // When landing in home screen
        if (!webView.canGoBack()) {

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {

                Toast.makeText(getBaseContext(), "Press again to exit",
                        Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();

        } else{
            webView.goBack();
        }


    }
}