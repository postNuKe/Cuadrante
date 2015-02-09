package es.progmac.cuadrante.lib;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

/**
 * Crea mi propio TimePickerDialog pero con un intervalo de 15 minutos cada vez que se selecciona
 * sumar o restar los minutos.
 * @author david
 *
 */
public class IntvlMinTimePickerDialog extends TimePickerDialog {
	//public static final int TIME_PICKER_INTERVAL = 15;
	private static int _interval = 15;
	private boolean mIgnoreEvent = false;
	private static Context _ctx;

	public IntvlMinTimePickerDialog(Context context,
			OnTimeSetListener callBack, int hourOfDay, int minute,
			boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		_ctx = context;
	}

	public IntvlMinTimePickerDialog(Context context, int theme,
			OnTimeSetListener callBack, int hourOfDay, int minute,
			boolean is24HourView) {
		super(context, theme, callBack, hourOfDay, minute, is24HourView);
		_ctx = context;
	}

	@Override
	public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
		super.onTimeChanged(timePicker, hourOfDay, minute);
		// this.setTitle("2. Select Time");
		if(_interval > 0 && Sp.getIntervalMinutesActive(_ctx)){
			if (!mIgnoreEvent){
				minute = getRoundedMinute(minute, _ctx);
				mIgnoreEvent=true;
				timePicker.setCurrentMinute(minute);
				mIgnoreEvent=false;
			}
		}
	}

	public static int getRoundedMinute(int minute, Context context){
		if(_interval > 0 && Sp.getIntervalMinutesActive(context)){
			if(minute % _interval != 0){
				int minuteFloor = minute - (minute % _interval);
				minute = minuteFloor + (minute == minuteFloor + 1 ? _interval : 0);
				if (minute == 60)  minute=0;
			}
		}

		return minute;
	}	

	public void setInterval(int interval){
		_interval = interval;
	}

	public int getInterval(){
		return _interval;
	}

}
