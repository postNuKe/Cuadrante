package es.progmac.cuadrante;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Weeks;

import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.cuadrante.lib.CalendarAdapter;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.DatabaseHandler;
import es.progmac.cuadrante.lib.MyLog;
import es.progmac.cuadrante.lib.Pas;
import es.progmac.cuadrante.lib.Sp;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.widget.GridView;
import android.widget.RemoteViews;

public class AppWidgetWeekProvider extends AppWidgetProvider {
	
	public static final String TAG = "AppWidgetWeekProvider";
	public static final int DAYS_SHOW = 7;
	private DatabaseHandler mDb;
	private Context mContext;
	
	public AppWidgetWeekProvider() {

	}
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		mContext = context;
		boolean pasActive = false;
		boolean showData = true;
		ArrayList<String> pasList = new ArrayList<String>();
        final int N = appWidgetIds.length;
        DateTime dtPas = new DateTime();
        Pas pasObj = new Pas(mContext);
        
        if(Sp.getLoginActivate(context) && !Sp.getWidgetActivate(context)){
        	showData = false;
        	//return;
        }

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_week);
            views.setOnClickPendingIntent(R.id.frame1, pendingIntent);
            
            DateTime today = new DateTime();
            DateTime firstDay = new DateTime();
            //Miramos que primer día se debe de mostrar
            if(Sp.getWidgetWeekFistDay(mContext).equals("yesterday")){
                firstDay = today.minusDays(1);            	
            }else if(Sp.getWidgetWeekFistDay(mContext).equals("today")){
            	firstDay = today; 
            }else if(Sp.getWidgetWeekFistDay(mContext).equals("tomorrow")){
            	firstDay = today.plusDays(1); 
            }
            
            DateTime lastDay = firstDay.plusDays(DAYS_SHOW - 1);
            DateTime day = firstDay;        
            
            mDb = new DatabaseHandler(context);
            List<ServicioInfo> services = 
            		mDb.getServicesFromInterval(
            				CuadranteDates.formatDate(firstDay), 
            				CuadranteDates.formatDate(lastDay));
            
            List<ComisionInfo> comisions = 
            		mDb.getComisionFromInterval(
            				CuadranteDates.formatDate(firstDay), 
            				CuadranteDates.formatDate(lastDay));
            				
                        
            for(int j=1, idDate = 0, idDayWeek = 0, idDateServicio = 0, idCol = 0; j<= DAYS_SHOW; j++){
	            boolean dayWithService = false;
	            boolean isToday = false;//era por si quería poner el dia de hoy en otro color como en el calendario
	            idCol = context.getResources().getIdentifier(
            			"col".concat(String.valueOf(j)), "id", context.getPackageName());	
            	idDate = context.getResources().getIdentifier(
            			"date".concat(String.valueOf(j)), "id", context.getPackageName());
	            idDayWeek = context.getResources().getIdentifier(
            			"day_week".concat(String.valueOf(j)), "id", context.getPackageName());
	            idDateServicio = context.getResources().getIdentifier(
            			"date_servicio".concat(String.valueOf(j)), "id", context.getPackageName());	
	            
	            views.setInt(idCol, "setBackgroundResource", R.drawable.item_background);
        		views.setInt(idDate,"setTextColor", Color.WHITE);
        		views.setInt(idDayWeek,"setTextColor", Color.WHITE);
        		views.setInt(idDateServicio,"setTextColor", Color.WHITE);
	            views.setTextViewText(idDate, "");
	            views.setTextViewText(idDayWeek, "");
	            views.setTextViewText(idDateServicio, "");
	            
	            
	            if(showData){
		            views.setTextViewText(idDate, "" + day.getDayOfMonth());           
		            views.setTextViewText(
		            		idDayWeek, 
		            		CuadranteDates.getDayOfWeekAsShort(context, day.getDayOfWeek()));//day.dayOfWeek().getAsShortText());  
		            /*
		            if(day.equals(today)){
		            	views.setInt(idCol,"setBackgroundResource", R.drawable.item_background_focused);
		            	isToday = true;
		            }
		            */
		            for(ServicioInfo service : services){
		            	if(service.getDate().equals(CuadranteDates.formatDate(day))){
		            		//si es domingo y está activado lo de los domingos en rojo
		            		if(Sp.getSundayColorActive(mContext) &&
		            				service.getDateTime().dayOfWeek().get() == DateTimeConstants.SUNDAY){
		            			views.setInt(idCol,"setBackgroundColor", Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
		            			views.setInt(idDate,"setTextColor", Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
		            			views.setInt(idDayWeek,"setTextColor",  Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
		            			views.setInt(idDateServicio,"setTextColor",  Cuadrante.SERVICE_DEFAULT_TEXT_COLOR_HOLIDAY);
		            		}else{
			            		if(!isToday)
			            			views.setInt(idCol,"setBackgroundColor", Integer.parseInt(service.getBgColor()));
			            		views.setInt(idDate,"setTextColor", Integer.parseInt(service.getTextColor()));
			            		views.setInt(idDayWeek,"setTextColor", Integer.parseInt(service.getTextColor()));
			            		views.setInt(idDateServicio,"setTextColor", Integer.parseInt(service.getTextColor()));
		            		}
		            		views.setTextViewText(idDateServicio, service.getName());
		            		dayWithService = true;
		            		break;
		            	}
		            }
		            
		            //si el dia no tiene servicio mostramos la comision si tiene
		            //es una locura el cambiar los colores y demás, hay que jugar con bitmap y polladas
		            //http://stackoverflow.com/questions/4318572/how-to-use-a-custom-typeface-in-a-widget
		            if(!dayWithService){
		            	if(pasObj.active()){
	                		views.setInt(idDateServicio, "setTextColor", Color.parseColor("#8a8a8a"));
	                		views.setTextViewText(idDateServicio, pasObj.getDay(day));		            		
		            	}
		            	//views.setTextViewText(idDateServicio, "");
		            	for(ComisionInfo comision : comisions){
		    				if(comision.getInterval().contains(day)){
		                		views.setInt(idDateServicio, "setTextColor", Color.parseColor("#8a8a8a"));
		                		views.setTextViewText(idDateServicio, 
		                				Cuadrante.getLengthServiceNameSubstring(
		                						context, Cuadrante.CALENDAR_SERVICE_COMISION).toString());
		    					break;
		    				}            		
		            	}
		            	
		            }
	            }  
	            day = day.plusDays(1);
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}