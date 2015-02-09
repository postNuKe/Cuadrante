package com.commonsware.cwac.colormixer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class RainbowPickerDialog extends Dialog {
	private int sColor = 0;
	private ColorMixer sMixer=null;
	private ColorMixer.OnColorChangedListener sOnSet=null;
	
	public RainbowPickerDialog(Context context,ColorMixer mixer, ColorMixer.OnColorChangedListener onSet) {
		super(context);
		this.setTitle(R.string.cwac_colormixer_palette);
		sMixer = mixer;
		sOnSet = onSet;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);

		GridView gridViewColors = (GridView) findViewById(R.id.gridViewColors);
		gridViewColors.setAdapter(new RainbowPickerAdapter(getContext()));

		// close the dialog on item click
		gridViewColors.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setSelectedColor(view.getId());
				sMixer.setColor(getSelectedColor());
				sOnSet.onColorChange(getSelectedColor());
				dismiss();
			}
		});
	}		
	private void setSelectedColor(int color){
		sColor = color;
	}
	public int getSelectedColor(){
		return sColor;
	}
}
