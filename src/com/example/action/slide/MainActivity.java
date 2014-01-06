package com.example.action.slide;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.example.action.slide.R.color;
import com.fima.cardsui.views.CardUI;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract.Colors;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends SlidingActivity {
	ActionBar actionbar;
	private comicTask mComicTask = null;
	private CardUI mCardView;
	private ArrayList<String[]> itemsList = new ArrayList<String[]>();
	private String[] items;
	private int location =0;
	private int displayWidth;
	private int choice = 0;
	String[] values;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.activity_behind);
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		values = new String[] { "Newest", "Oldest", "Random" };

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data'

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		R.layout.list_item_jci, R.id.item_jci, values);
		// Assign adapter to ListView
		listView.setAdapter(adapter); 
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, values[arg2], Toast.LENGTH_SHORT).show();
				getSlidingMenu().toggle(true);
				if(values[arg2].equals("Newest") && choice != 0){
					actionbar.setTitle("Newest");
					choice = 0;
					location = 0;
					mCardView.clearCards();
					itemsList.clear();
					mComicTask = new comicTask();
					mComicTask.execute("http://www.explosm.net/comics");
				}else if(values[arg2].equals("Random") && choice != 1){
					actionbar.setTitle("Random");
					choice = 1;
					location = 0;
					mCardView.clearCards();
					itemsList.clear();
					mComicTask = new comicTask();
					mComicTask.execute("http://www.explosm.net/comics/random/");
				}else if(values[arg2].equals("Oldest") && choice != 2){
					actionbar.setTitle("Oldest");
					choice = 2;
					location = 0;
					mCardView.clearCards();
					itemsList.clear();
					mComicTask = new comicTask();
					mComicTask.execute("http://www.explosm.net/comics/15/");
				}else{
					mCardView.scrollToCard(0);
				}
			}

          });
		
		setContentView(R.layout.activity_main);
	  
		getSlidingMenu().setAboveOffset(150);
		getSlidingMenu().setBehindOffset(150);
		getSlidingMenu().setFadeDegree(.3f);
		getSlidingMenu().setShadowWidth(100);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Newest");
		
		DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
		displayWidth = metrics.widthPixels;
		
		mComicTask = new comicTask();
		mComicTask.execute("http://www.explosm.net/comics");
		
		
	}
	public class comicTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... input) {
			//TODO: get info and parse web
			System.out.println("in task");
			
				String next = buffer(input[0]);
				
				
			
			return next;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
		
		@Override
		protected void onPostExecute(String result) {
			//load card
			if(location != 0){
				mCardView.clearCards();
			}
			for(int i = 0; i < itemsList.size(); i++){
				String[] item = itemsList.get(i);
				System.out.println(itemsList.get(2)[0]);
				mCardView = (CardUI) findViewById(R.id.cardsview);
				mCardView.setSwipeable(false);
				final MyCard androidViewsCard = new MyCard(item[0], item[1], item[2], item[3], item[4], displayWidth);
				
			/*	androidViewsCard.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getBaseContext(), WebViewer.class);
						intent.putExtra("Image", androidViewsCard.getImage());
						startActivity(intent);

					}
				});*/
				mCardView.addCard(androidViewsCard);
			}
			
			
			mCardView.refresh();
			loadMore(result);
			mCardView.scrollToCard(location);
		}
	}
	
	public String buffer(String imgUrl){
		System.out.println("do buffer");
			URL nextUrl;
			for(int i = 0; i < 5; i++){
				System.out.println(imgUrl);
				String sources = "";
				try {
					nextUrl = new URL(imgUrl);
						BufferedReader in = new BufferedReader(
								new InputStreamReader(
										nextUrl.openStream()));

						String inputLine;

						while ((inputLine = in.readLine()) != null){
							sources = sources + " " + inputLine;
							//System.out.println(inputLine);
						}
						System.out.println(sources);
						in.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					e.printStackTrace();
				}
				if(choice == 0){
					//read newest
					imgUrl = readSource(sources, imgUrl);
				}else if(choice == 1){
					//read random
					imgUrl = randSource(sources, imgUrl);
				}else if(choice == 2){
					//read oldest
					imgUrl = oldSource(sources, imgUrl);
				}
			}
			
			return imgUrl;
		
	}
	
	public void loadMore(final String next) {
		// TODO Auto-generated method stub
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);
		final LoadCard androidViewsCard = new LoadCard();
		
		androidViewsCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				location = mCardView.getLastCardStackPosition() - 1;
				TextView text = (TextView) mCardView.findViewById(R.id.LoadTitle);
				text.setVisibility(View.GONE);
				ProgressBar loading = (ProgressBar) mCardView.findViewById(R.id.loadProgress);
				loading.setVisibility(View.VISIBLE);
				mComicTask = new comicTask();
  				mComicTask.execute(next);

			}
		});
		mCardView.addCard(androidViewsCard);
	}

	/*
	 * used for parsing html
	 * @html = html source code of page
	 * @return = array of specific information
	 * 
	 */
	public String readSource(String html, String fullUrl){
		//System.out.println("start" + html);
		String[] content = html.split("id=\"maincontent\">");
		//System.out.println("main content: " + content[0]);
		content = content[1].split("</script>");
		//System.out.println("script end");
		content = content[1].split("<script language=\"javascript\">");
		String userName, date, next, userImage;
		try{
			String[] user_content = content[0].split("<nobr>");
			userImage = user_content[1].substring(user_content[1].indexOf("\"")+1, user_content[1].substring(user_content[1].indexOf("\"")+1).indexOf("\"")+user_content[1].indexOf("\"")+1);
			System.out.println(userImage);
			user_content = user_content[1].split("by");
			user_content = user_content[1].split("</a");
			userName = user_content[0].substring(user_content[0].lastIndexOf(">")+1, user_content[0].length());
			user_content = content[0].split("_blank");
			date = user_content[user_content.length-1];
			date = date.substring(date.indexOf("<br />")+6, date.indexOf("</nobr>"));
			System.out.println(date);
			user_content = content[0].split("rel=\"prev\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net" + next;
		}catch(Exception e){
			String[] user_content = content[0].split("rel=\"prev\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net" + next;
			return next;
		}
		
		//System.out.println("javascript");
		//System.out.println(content[content.length-2]);
		content = content[content.length-2].split("/IMG");
		//System.out.println("url" + content[0]);
		String image = content[0].substring(content[0].lastIndexOf("IMG]")+4, content[0].length()-1);
		//System.out.println(image);
		items = new String[5];
		items[0] = userName;
		items[1] = userImage;
		items[2] = date;
		items[3] = image;
		items[4] = fullUrl;
		//System.out.println(items[0]);
		itemsList.add(items);
		//System.out.println(itemsList.get(itemsList.size()-1)[0]);
		
		return next;
		
		
	}
	
	/*
	 * used for parsing html
	 * @html = html source code of page
	 * @return = array of specific information
	 * 
	 */
	public String randSource(String html, String fullUrl){
		//System.out.println("start" + html);
		String[] content = html.split("id=\"maincontent\">");
		//System.out.println("main content: " + content[0]);
		content = content[1].split("</script>");
		//System.out.println("script end");
		content = content[1].split("<script language=\"javascript\">");
		String userName, date, next, userImage;
		try{
			String[] user_content = content[0].split("<nobr>");
			userImage = user_content[1].substring(user_content[1].indexOf("\"")+1, user_content[1].substring(user_content[1].indexOf("\"")+1).indexOf("\"")+user_content[1].indexOf("\"")+1);
			System.out.println(userImage);
			user_content = user_content[1].split("by");
			user_content = user_content[1].split("</a");
			userName = user_content[0].substring(user_content[0].lastIndexOf(">")+1, user_content[0].length());
			user_content = content[0].split("_blank");
			date = user_content[user_content.length-1];
			date = date.substring(date.indexOf("<br />")+6, date.indexOf("</nobr>"));
			System.out.println(date);
			user_content = content[0].split("rel=\"prev\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net/comics/random/";
		}catch(Exception e){
			String[] user_content = content[0].split("rel=\"prev\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net/comics/random/";
			return next;
		}
		
		//System.out.println("javascript");
		//System.out.println(content[content.length-2]);
		content = content[content.length-2].split("/IMG");
		//System.out.println("url" + content[0]);
		String image = content[0].substring(content[0].lastIndexOf("IMG]")+4, content[0].length()-1);
		//System.out.println(image);
		items = new String[5];
		items[0] = userName;
		items[1] = userImage;
		items[2] = date;
		items[3] = image;
		items[4] = fullUrl;
		//System.out.println(items[0]);
		itemsList.add(items);
		//System.out.println(itemsList.get(itemsList.size()-1)[0]);
		
		return next;
		
		
	}
	
	/*
	 * used for parsing html
	 * @html = html source code of page
	 * @return = array of specific information
	 * 
	 */
	public String oldSource(String html, String fullUrl){
		//System.out.println("start" + html);
		String[] content = html.split("id=\"maincontent\">");
		//System.out.println("main content: " + content[0]);
		content = content[1].split("</script>");
		//System.out.println("script end");
		content = content[1].split("<script language=\"javascript\">");
		String userName, date, next, userImage;
		try{
			String[] user_content = content[0].split("<nobr>");
			userImage = user_content[1].substring(user_content[1].indexOf("\"")+1, user_content[1].substring(user_content[1].indexOf("\"")+1).indexOf("\"")+user_content[1].indexOf("\"")+1);
			System.out.println(userImage);
			user_content = user_content[1].split("by");
			user_content = user_content[1].split("</a");
			userName = user_content[0].substring(user_content[0].lastIndexOf(">")+1, user_content[0].length());
			user_content = content[0].split("_blank");
			date = user_content[user_content.length-1];
			date = date.substring(date.indexOf("<br />")+6, date.indexOf("</nobr>"));
			System.out.println(date);
			user_content = content[0].split("rel=\"next\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net" + next;
		}catch(Exception e){
			String[] user_content = content[0].split("rel=\"next\"");
			next = user_content[1].substring(user_content[1].indexOf("href=\"") + 6, user_content[1].substring(user_content[1].indexOf("href=\"") + 6).indexOf("\"") + user_content[1].indexOf("href=\"")+6);
			next = "http://www.explosm.net" + next;
			return next;
		}
		
		//System.out.println("javascript");
		//System.out.println(content[content.length-2]);
		content = content[content.length-2].split("/IMG");
		//System.out.println("url" + content[0]);
		String image = content[0].substring(content[0].lastIndexOf("IMG]")+4, content[0].length()-1);
		//System.out.println(image);
		items = new String[5];
		items[0] = userName;
		items[1] = userImage;
		items[2] = date;
		items[3] = image;
		items[4] = fullUrl;
		//System.out.println(items[0]);
		itemsList.add(items);
		//System.out.println(itemsList.get(itemsList.size()-1)[0]);
		
		return next;
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_main, (com.actionbarsherlock.view.Menu) menu);
		return true;
	}
	
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
		  case android.R.id.home:
	            // TODO handle clicking the app icon/logo
			  getSlidingMenu().toggle(true);
			  
	          return true;
          case R.id.menu_settings:
        	  //Toast.makeText(this, "menu", Toast.LENGTH_SHORT).show();
        	  return true;
          case R.id.menu_0:
        	  //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
        	  mCardView.scrollToCard(0);
        	  return true;
          case R.id.menu_1:
        	  //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        	  mCardView.scrollToCard(mCardView.getLastCardStackPosition());
        	  return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void extraClickHandler(View view)
    {
		//TO-DO: make dialog box to share link, copy link, or save image
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		    clipboard.setText(view.getContentDescription());
		} else {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
		    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",view.getContentDescription());
		    clipboard.setPrimaryClip(clip);
		}
		Toast.makeText(getBaseContext(), "URL copied to clipboard", Toast.LENGTH_SHORT).show();
    }
	
	public void imageClickHandler(View view){
		Intent intent = new Intent(getBaseContext(), WebViewer.class);
		System.out.println(view.getContentDescription());
		intent.putExtra("Image", view.getContentDescription());
		startActivity(intent);
	}

	public void downloadClickHandler(View view){
		System.out.println(view.getContentDescription().toString());
		new DownloadImageTask().execute(view.getContentDescription());
	}
	
	private class DownloadImageTask extends AsyncTask<Object, Object, String> {
		@Override
	     protected String doInBackground(Object... views) {
			File file = null;
	    	 System.out.println("background");
	    	 String view = views[0].toString();
	    	 System.out.println(view);
	    	 Object filepath = null;
	 		try{   
	 			  URL url = new URL(view);
	 			  Log.i("url:",""+url.toString());
	 			 System.out.println("url:"+" "+url.toString());
	 			  HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	 			  urlConnection.setRequestMethod("GET");
	 			  urlConnection.setDoOutput(true);                   
	 			  urlConnection.connect(); 
	 			  
	 			  String SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
	 			  Log.i("SD card:",""+SDCardRoot.toString());
	 			 System.out.println("SD card "+SDCardRoot.toString());
	 			  String filename=view.substring(view.lastIndexOf("/"));   
	 			  filename = "/Download" + filename;
	 			  Log.i("Local filename:",""+filename);
	 			 System.out.println("Local filename: "+filename);
	 			  file = new File(SDCardRoot,filename);
	 			  Log.i("file:",""+file.toString()); 
	 			 System.out.println("file: "+file.toString());
	 			  if (file.exists ()) file.delete (); 
	 			  FileOutputStream fileOutput = new FileOutputStream(file);
	 			  InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
	 			  int totalSize = urlConnection.getContentLength();
	 			  int downloadedSize = 0;   
	 			  byte[] buffer = new byte[totalSize];
	 			  int bufferLength = 0;
	 			  while ( (bufferLength = inputStream.read(buffer)) > 0 ) 
	 			  {                 
	 				  Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
	 			    fileOutput.write(buffer, 0, bufferLength);                  
	 			    downloadedSize += bufferLength;                 
	 			    Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
	 			  }             
	 			  fileOutput.close();
	 			  if(downloadedSize==totalSize) filepath=file.getPath();    
	 			} 
	 			catch (MalformedURLException e) 
	 			{
	 			  e.printStackTrace();
	 			} 
	 			catch (IOException e)
	 			{
	 			  filepath=null;
	 			  e.printStackTrace();
	 			}
	 			Log.i("filepath:"," "+filepath) ;
	 			return file.toString();
	     }

	     protected void onPostExecute(String imageDL) {
	    	 System.out.println(imageDL);
	    	 try {
				MediaStore.Images.Media.insertImage(getContentResolver(), imageDL, "Title", "Description");
				Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			    File f = new File(imageDL);
			    Uri contentUri = Uri.fromFile(f);
			    mediaScanIntent.setData(contentUri);
			    MainActivity.this.sendBroadcast(mediaScanIntent);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 Toast.makeText(MainActivity.this.getApplicationContext(), "Download complete", Toast.LENGTH_SHORT).show();
	     }



	 }
	
	public void shareClickHandler(View view){
		Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, view.getContentDescription().toString());
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this comic!");
        startActivity(Intent.createChooser(intent, "Share"));
	}
	
}
