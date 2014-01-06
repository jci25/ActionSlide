package com.example.action.slide;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WebViewer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_web_viewer);
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("Image");
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
		WebView webView = (WebView) findViewById(R.id.webView1);
		//webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.loadUrl(url);
		final Activity here = this;
		webView.setWebChromeClient(new WebChromeClient() {
	        public void onProgressChanged(WebView view, int progress)  
	        {
	         here.setProgress(progress * 100); //Make the bar disappear after URL is loaded
	 
	         // Return the app name after finish loading
	            if(progress == 100);
	               //MyActivity.setTitle(R.string.app_name);
	          }
	        });
	}

	
}
