package com.commonsware.cwac.colormixer;

import java.util.ArrayList;
import java.util.List;

import com.commonsware.cwac.parcel.ParcelHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.text.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;


public class ColorMixerDialogCopy extends AlertDialog
implements DialogInterface.OnClickListener {
	static private final String COLOR="c";
	private ColorMixer mixer=null;
	private int initialColor;
	private int defaultColor;
	private ColorMixer.OnColorChangedListener onSet=null;
	private Context ctx;
	private ParcelHelper parcel;
	private ClipboardManager clipboard;

	public ColorMixerDialogCopy(Context ctxt,
			int defaultColor,
			int initialColor,
			ColorMixer.OnColorChangedListener onSet) {
		super(ctxt);
		ctx = ctxt;
 	   	clipboard = (ClipboardManager)
		        ctxt.getSystemService(Context.CLIPBOARD_SERVICE);

		this.initialColor=initialColor;
		this.defaultColor=defaultColor;
		this.onSet=onSet;

		ParcelHelper parcel=new ParcelHelper("cwac-colormixer", ctxt);
		this.parcel = parcel;
		
		mixer=new ColorMixer(ctxt);
		mixer.setColor(initialColor);

		setView(mixer);
		registerForContextMenu(mixer);
		
		setButton(ctxt.getText(parcel.getIdentifier("set", "string")),
				this);
		setButton2(ctxt.getText(parcel.getIdentifier("cancel", "string")),
				(DialogInterface.OnClickListener)null);
		setButton3(ctxt.getText(parcel.getIdentifier("options", "string")), this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		//Log.d("onClick", "which:" + which);
		//Log.d("onClick", "color:" + mixer.getColor());
		if(which == DialogInterface.BUTTON_NEUTRAL){
			List<CharSequence> list_options = new ArrayList<CharSequence>();
			list_options.add(ctx.getText(parcel.getIdentifier("palette", "string")));
			list_options.add(ctx.getText(parcel.getIdentifier("defaultcolor", "string")));
			list_options.add(ctx.getText(parcel.getIdentifier("copy", "string")));
			//only if i have copied the color to clipboard
			if(clipboard.hasText()){
				String text = clipboard.getText().toString();
				if(text.length() >= 2){
					if(text.substring(0,2).matches("[-][0-9]")){
						list_options.add(ctx.getText(parcel.getIdentifier("paste", "string")));
					}
				}
			}		            		   
			final CharSequence[] items = list_options.toArray(new CharSequence[list_options.size()]);
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		    builder.setTitle(ctx.getText(parcel.getIdentifier("selectingoptions", "string")))
		           .setItems(items, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   switch (which) {
		            	   case 0://palette
		            		   dialog = new RainbowPickerDialog(ctx, mixer, onSet);
		   					   ((Dialog) dialog).show();
		            		   break;
		            	   case 1://default color
		            		   mixer.setColor(defaultColor);
        				   	   onSet.onColorChange(mixer.getColor());
		            		   break;
		            	   case 2://copy
		            		   clipboard.setText(String.valueOf(mixer.getColor()));
		            		   Toast.makeText(ctx, 
		            				   ctx.getText(parcel.getIdentifier("copiedcolortoclipboard", "string")), 
		            				   Toast.LENGTH_LONG).show();
		            		   break;
		            	   case 3://paste
		            		   if(clipboard.hasText()){
		            			   String text = clipboard.getText().toString();
		            			   if(text.length() >= 2){
		            				   if(text.substring(0,2).matches("[-][0-9]")){
		            					   mixer.setColor(Integer.parseInt(text));
		            				   	   onSet.onColorChange(mixer.getColor());
			            				   Toast.makeText(ctx, 
			            						   ctx.getText(parcel.getIdentifier("colorpasted", "string")), 
			            						   Toast.LENGTH_LONG).show();	
		            				   }
		            			   }
		            		   }		            		   
		            		   break;
		            	   }
		               }
		    });
		    builder.create().show();		
		}else if (initialColor!=mixer.getColor()) {
			onSet.onColorChange(mixer.getColor());
		}
	}
	
	@Override
	public Bundle onSaveInstanceState() {
		Bundle state=super.onSaveInstanceState();

		state.putInt(COLOR, mixer.getColor());

		return(state);
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);

		mixer.setColor(state.getInt(COLOR));
	}
}
