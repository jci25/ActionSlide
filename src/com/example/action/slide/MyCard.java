package com.example.action.slide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class MyCard extends Card {
	private String img, name, date, user, full;
	private int displayWidth;
	public ImageView imageView, extraImg, shareImg, saveImg, copyImg;
	public MyCard(String namein, String userin, String datein, String url, String fullUrl, int display){
		name = namein;
		user = userin;
		date=datein;
		img = url;
		displayWidth = display;
		full = fullUrl;
	}

	@Override
	public View getCardContent(Context context) {
		
		
		
		ImageLoader imageLoader = new ImageLoader(context);
		View view = LayoutInflater.from(context).inflate(R.layout.card, null);
		
		LinearLayout Layouts = (LinearLayout) view.findViewById(R.id.img_lay);
		//Layouts.getLayoutParams().height = (int) Math.floor(displayWidth * .75);
		
		((TextView) view.findViewById(R.id.name)).setText(name);
		((TextView) view.findViewById(R.id.date)).setText(date);
		
		imageView = (ImageView) view.findViewById(R.id.cnhImg );
		imageView.setContentDescription(img);
		shareImg = (ImageView) view.findViewById(R.id.shareImg);
		shareImg.setContentDescription(img);
		saveImg = (ImageView) view.findViewById(R.id.saveImg);
		saveImg.setContentDescription(img);
		copyImg = (ImageView) view.findViewById(R.id.copyImg);
		copyImg.setContentDescription(img);
		//imageView.setMinimumHeight((int) Math.floor(displayWidth * .75));
		//imageView.setMaxHeight((int) Math.floor(displayWidth * .75));
		imageLoader.DisplayImage(img, imageView, displayWidth);
		
		ImageView userView = (ImageView) view.findViewById(R.id.user );
		imageLoader.DisplayImage(user, userView, displayWidth);
		return view;
	}
	
	public String getImage(){
		
		return img;
	}
	
	
}
