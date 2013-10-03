package com.example.opentopotest2;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	
	Button button1 = null;
	TextView textView1 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button1 = (Button) this.findViewById(R.id.button1);
		button1.setOnClickListener(this);
		textView1 = (TextView) this.findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button1){
			button1.setText("Loading...");
			button1.setEnabled(false);
			textView1.setText("Loading...");
			runWeb();
		}	
	}
	
	WebView webView;
	private static final String magicString = "25az225MAGICee4587da";

	private void runWeb(){
		webView = new WebView(this);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.setWebChromeClient(new PageHandler());
	    webView.setWebViewClient(new WebViewClient() {
	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	Log.d("onPageFinished", "url:" + url);
	        	if(url.contentEquals("file:///android_asset/OpenTopoTest.html") == true){
		        	Log.d("onPageFinished", "Submitting form");
	        		//Initial homemade post page, change post vars here, then post form;
	        		String strFunction = "javascript:"
		        			+ "document.getElementById('email').value = 'newEmailAddress@email.com';"
		        			+ "document.getElementById('theForm').submit();";
		        	webView.loadUrl(strFunction);
	        	} else {
	        		//Open topo pages
		        	String strFunction = "javascript:"
		        			+ "var els = document.getElementsByTagName('a');"
		        			+ "for (var i = 0, l = els.length; i < l; i++) {"
							+ "    var el = els[i];"
							+ "    if (el.innerHTML.indexOf('dems.tar.gz') != -1) {"
							+ "        javascript:console.log('"+magicString+"'+ el.href);"
							+ "    }"
							+ "}";
		        	webView.loadUrl(strFunction);
	        	}
	        }
	    });
	    webView.loadUrl("file:///android_asset/OpenTopoTest.html");
	}
	
	private class PageHandler extends WebChromeClient {		
		public boolean onConsoleMessage(ConsoleMessage cmsg){
			if(cmsg.message().startsWith(magicString)){
				String categoryMsg = cmsg.message().substring(magicString.length());
				Log.d("Link:", categoryMsg);
				button1.setText("Load URL");
				button1.setEnabled(true);
				textView1.setText(categoryMsg);
				webView.stopLoading();
				webView.destroy();
				return true;
			}
			return false;
		}
	}

}
