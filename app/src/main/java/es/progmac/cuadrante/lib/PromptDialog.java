package es.progmac.cuadrante.lib;

import es.progmac.cuadrante.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

/**
 * helper for Prompt-Dialog creation
 */
public abstract class PromptDialog extends AlertDialog.Builder implements
		OnClickListener {
	private final EditText input;
	private Context ctx;

	/**
	 * @param context
	 * @param title
	 *            resource id, si se le pasa 0, no saldrá el título
	 * @param message
	 *            resource id, si se pasa 0, no saldrá el mensaje
	 * @param String
	 *            inputText, texto que aparecerá en el EditeText
	 * @param int 
	 * 			  inputMaxLength, caracteres máximos de introducción de texto
	 * @param String
	 *            hintText, texto que aparecerá de fondo en el input que
	 *            desaparecera caundo seleccionas dicho input para escribir
	 * @param int 
	 * 			  lines, número de lineas del cuadro de entrada de texto
	 */
	public PromptDialog(Context context, int title, int message,
			String inputText, int inputMaxLength, String hintText, int lines) {
		super(context);
		input = new EditText(context);
		basePromptDialog(context, title, message, inputText, inputMaxLength, hintText, lines);
	}
	
	/**
	 * @param context
	 * @param title
	 *            resource id, si se le pasa 0, no saldrá el título
	 * @param message
	 *            resource id, si se pasa 0, no saldrá el mensaje
	 * @param String
	 *            inputText, texto que aparecerá en el EditeText
	 */
	public PromptDialog(Context context, int title, int message,
			String inputText) {
		super(context);
		input = new EditText(context);
		basePromptDialog(context, title, message, inputText, 0, null, 0);
	}
	
	/**
	 * @param context
	 * @param title
	 *            resource id, si se le pasa 0, no saldrá el título
	 * @param message
	 *            resource id, si se pasa 0, no saldrá el mensaje
	 * @param String
	 *            inputText, texto que aparecerá en el EditeText
	 */
	public PromptDialog(Context context, int title, String inputText) {
		super(context);
		input = new EditText(context);
		basePromptDialog(context, title, 0, inputText, 0, null, 0);
	}
	
	private void basePromptDialog(Context context, int title, int message, 
			String inputText, int inputMaxLength, String hintText, int lines) {
		ctx = context;
		if (title > 0)
			setTitle(title);
		if (message > 0)
			setMessage(message);
		
		InputFilter[] fArray;
		if (inputMaxLength > 0) {
			fArray = new InputFilter[]{
					new InputFilter.AllCaps(),
					new InputFilter.LengthFilter(inputMaxLength)
			};
		}else{
			fArray = new InputFilter[]{
					new InputFilter.AllCaps()
			};			
		}
		input.setFilters(fArray);
		
		if (inputText != null)
			input.setText(inputText);
		if(lines > 0){
			input.setLines(lines);
		}
		if (hintText != null)
			input.setHint(hintText);
		
		setView(input);
		// ok y cancel en res/values/strings.xml
		setPositiveButton(R.string.ok, this);
		setNegativeButton(R.string.cancel, this);
	}
	
	public void setHintText(String hintText){
		if (hintText != null){
			input.setHint(hintText);
		}
	}
	
	public void setHintText(int hintText){
		if (hintText > 0){
			input.setHint(ctx.getString(hintText));
		}
	}
	
	public void setLines(int lines){
		if(lines > 0){
			input.setLines(lines);
		}		
	}
	
	public void setInputType(int type){
		if(type > 0){
			input.setInputType(type);
		}
	}

	/**
	 * will be called when "cancel" pressed. closes the dialog. can be
	 * overridden.
	 * 
	 * @param dialog
	 */
	public void onCancelClicked(DialogInterface dialog) {
		dialog.dismiss();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (onOkClicked(input.getText().toString())) {
				dialog.dismiss();
			}
		} else {
			onCancelClicked(dialog);
		}
	}

	/**
	 * called when "ok" pressed.
	 * 
	 * @param input
	 * @return true, if the dialog should be closed. false, if not.
	 */
	abstract public boolean onOkClicked(String input);
}
