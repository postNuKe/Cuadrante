package es.progmac.cuadrante.lib;

import es.progmac.cuadrante.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public abstract class DialogSpinner extends AlertDialog.Builder  implements
OnClickListener{
	public Context context;
	private Spinner spinnercategory;
	
	public DialogSpinner(Context context, String[] items, int titleResourceId, int textResourceId) {
		super(context);
		this.context = context;

		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.dialog_spinner, null); 
		TextView txt = (TextView) dialogView.findViewById(R.id.message);
		txt.setText(textResourceId);
		setView(dialogView); 
		setTitle(titleResourceId);

		spinnercategory = (Spinner) dialogView. findViewById(R.id.viewSpin); 
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(
						context, 
						android.R.layout.simple_spinner_item,
						items); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		spinnercategory.setAdapter(adapter);  		
		setPositiveButton(R.string.ok, this);
		//MyLog.d("DialogSpinner", "constructor");
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (onOkClicked(spinnercategory.getSelectedItemPosition())) {
				dialog.dismiss();
			}
		} else {
			onCancelClicked(dialog);
		}
	}

	/**
	 * called when "ok" pressed.
	 * 
	 * @param position
	 * @return true, if the dialog should be closed. false, if not.
	 */
	abstract public boolean onOkClicked(int position);
	
	/**
	 * will be called when "cancel" pressed. closes the dialog. can be
	 * overridden.
	 * 
	 * @param dialog
	 */
	public void onCancelClicked(DialogInterface dialog) {
		dialog.dismiss();
	}	

}
