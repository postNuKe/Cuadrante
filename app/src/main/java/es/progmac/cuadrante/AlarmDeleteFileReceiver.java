package es.progmac.cuadrante;

import java.io.File;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.Extra;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Clase que recibirá la llamada cuando sea neceario eliminar un fichero,
 * mirar SettingsActivity.java para ver como se usa esta clase
 * @author david
 *
 */
public class AlarmDeleteFileReceiver extends BroadcastReceiver {
	public AlarmDeleteFileReceiver(){

	}

	/**
	 * Inicializamos los datos para realizar la llamada
	 * @param context
	 * @param long fecha en milisegundos cuando se ejecutará el delete del fichero
	 * System.currentTimeMillis() + 1000 * 60, //1 minuto
	 * @param filename path hasta el fichero a eliminar en un determinado tiempo
	 */
	public AlarmDeleteFileReceiver(Context context, long timeMillis, String filename) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmDeleteFileReceiver.class);
		intent.putExtra(Extra.FILENAME, filename);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, timeMillis, pi);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
		Bundle extras = intent.getExtras();
		if(extras != null){
			String filename = extras.getString(Extra.FILENAME);
			if(!filename.equals("")){
				File file = new File(filename);
				if(file.exists()){
					boolean success = file.delete();
					if (success) {
						//Toast.makeText(context, "SE HA BORRADO", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}
}
