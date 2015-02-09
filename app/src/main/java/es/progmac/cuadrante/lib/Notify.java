package es.progmac.cuadrante.lib;

import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import es.progmac.cuadrante.MainActivity;
import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ServicioInfo;

public class Notify {
	/**
	 * id de notificación para el f2
	 */
	private static final int NOTIFY_F2_ID = 1;

	public Notify() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Verifica si faltan menos de X dias para fin del mes actual y si
	 * se llega a conseguir el F2, para asi notificar al usuario
	 * @param context contexto
	 * @param db DatabaseHandler
	 * @param f2Hours horas del f2
	 * @param numAlerts número de alertas
	 */
	public static void f2(Context context, DatabaseHandler db, double f2Hours, int numAlerts) {
		if(Sp.getF2NotifyActive(context)){
			DateTime dtToday = new DateTime();
			Calendar calendar = Calendar.getInstance();
			int restDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - dtToday.getDayOfMonth();
			//MyLog.d("notifyAlert", "restDays:" + restDays);
			String todayDate = CuadranteDates.formatDate(dtToday.getYear(), dtToday.getMonthOfYear(), dtToday.getDayOfMonth());
			
			if(restDays <= Sp.getF2NotifyRestDays(context) && f2Hours < Cuadrante.F2_HOURS && numAlerts < Cuadrante.F2_ALERTS 
					&& !Sp.getF2NotifyDate(context).equals(todayDate)){//verificamos si ya se ha avisado hoy de que no llega al F2
				List<ServicioInfo> services = 
						db.getServicesInMonth(dtToday.getYear(), dtToday.getMonthOfYear());
				//solo vamos a permitir que alarme cuando el mes tenga más de 5
				//servicios realizados porque si no, cuando por ejemplo instalas
				//la applicación al final de un mes ya te salta la notificación
				if(services.size() > 5){		
					Sp.setF2NotifyDate(context, todayDate);
					//Obtenemos una referencia al servicio de notificaciones
					String ns = Context.NOTIFICATION_SERVICE;
					NotificationManager notManager = (NotificationManager) context.getSystemService(ns);
					
					//Configuramos la notificación
					int icon = android.R.drawable.stat_sys_warning;
					long hour = System.currentTimeMillis();
					 
					Notification notif = 
							new Notification(icon, context.getString(R.string.alert_status), hour);
					
					//Configuramos el Intent
					CharSequence description = 
							String.format(context.getString(R.string.alert_description), restDays);
					 
					Intent notIntent = new Intent(context,
					    MainActivity.class);
					 
					//PendingIntent contIntent = PendingIntent.getActivity(context, 0, notIntent, 0);
					PendingIntent contIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
					 
					notif.setLatestEventInfo(
					    context, context.getString(R.string.alert_title), description, contIntent);
					
					//AutoCancel: cuando se pulsa la notificaión ésta desaparece
					notif.flags |= Notification.FLAG_AUTO_CANCEL;
					 
					//Añadir sonido, vibración y luces
					if(Sp.getF2NotifyFlagSound(context))
						notif.defaults |= Notification.DEFAULT_SOUND;
					//notif.defaults |= Notification.DEFAULT_VIBRATE;
					//notif.defaults |= Notification.DEFAULT_LIGHTS;
					
					//Enviar notificación
					notManager.notify(NOTIFY_F2_ID, notif);
				}
			}
		}
	}		
}