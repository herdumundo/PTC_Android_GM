package com.example.maehara_ptc;

import Utilidades.voids;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import Utilidades.contenedor_usuario;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebView_Lotes extends AppCompatActivity {
    WebView wv;
    @Override
    public void onBackPressed() {
        voids.volver_atras(this,this,menu_principal.class,"DESEA IR AL MENU PRINCIPAL?",1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view__lotes);
        wv = (WebView) findViewById(R.id.wv_lotes);
if(voids.verif_wv==1)
    {
        wv.loadUrl("http://192.168.6.162:8086/Lotes/controles/logincontrol.jsp?usuario="+contenedor_usuario.usuario+"&pass="+contenedor_usuario.pass+"");
        wv.setWebViewClient(new MyClient());
        wv.setWebChromeClient(new GoogleClient());
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.clearCache(true);
        wv.clearHistory();
    }
else{
        wv.loadUrl("http://192.168.6.162:8086/Lotes/menu.jsp");
        wv.setWebViewClient(new MyClient());
        wv.setWebChromeClient(new GoogleClient());
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
     /* wv.clearCache(true);
        wv.clearHistory();*/
    }
        }

    class MyClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String Url)
        {
            view.loadUrl(Url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view,String url)
        {
            super.onPageFinished(view,url);

        }
    }
    class GoogleClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view,int newProgress)
        {
            super.onProgressChanged(view,newProgress);

        }
    }
  }