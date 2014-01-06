package com.example.action.slide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;

public class LoadCard extends Card {
	public LoadCard(){
	}

	@Override
	public View getCardContent(Context context) {
		
		View view = LayoutInflater.from(context).inflate(R.layout.load_card, null);
		
		((TextView) view.findViewById(R.id.LoadTitle)).setText("Load more");
		return view;
	}
	
	
}
