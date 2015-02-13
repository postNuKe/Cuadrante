/*
 * Copyright 2011 Lauri Nevala.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.progmac.cuadrante.lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.Weeks;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.info.ComisionInfo;
import es.progmac.cuadrante.info.ServicioInfo;
import es.progmac.android.others.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
	//private static final String TAG = "CalendarAdapter";
	static final int FIRST_DAY_OF_WEEK = 1; // Sunday = 0, Monday = 1
	private static final String TAG = "CalendarAdapter";
	public DatabaseHandler db;

	private Context mContext;

	private java.util.Calendar month;
	private Calendar selectedDate;
	private ArrayList<String> items;
	// references to our items
	public String[] days;
	public ServicioInfo[] daysWithServices;
	public ComisionInfo[] daysWithComisions;
	/**
	 * Si se activa o no el pas en este mes, se modifica desde la función
	 * MainActivity.calendarUpdater
	 */
	private boolean pas_active = false;
	public String pas = "";
	public int pas_year = 0;
	public int pas_week = 0;
	public DateTime dt_pas;
	/**
	 * Si el calendario tienen que verse los horarios en vez de los servicios
	 */
	private boolean view_schedule = false;
	
	/**
	 * Se guardan las semanas de servicios correlativas dependiendo de
	 * cual haya sido la elegida inicialmente en configuración
	 */
	public ArrayList<String> pas_list = new ArrayList<String>();
	
	public Pas pasObj = new Pas();

	public CalendarAdapter(Context c, Calendar monthCalendar) {
		month = monthCalendar;
		selectedDate = (Calendar) monthCalendar.clone();
		mContext = c;
		month.set(Calendar.DAY_OF_MONTH, 1);
		
		db = new DatabaseHandler(mContext);
		
		pasObj = new Pas(mContext);

		this.items = new ArrayList<String>();
		refreshDays();
	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	public int getCount() {
		return days.length;
	}

	public Object getItem(int position) {
		return days[position];
	}

	public long getItemId(int position) {
		return position;
	}
	
	public void setPASActive(boolean active){
		pas_active = active;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
		boolean schedule_same_line = false;
		/** si el item actual equivale al dia actual del calendario para ponerlo como activo */
		boolean isViewDayFocused = false;

		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);
		}
		Resources res = mContext.getResources();
		//MyLog.d("getView", "width:" + v.getWidth() + " getMeasuredWidth:" + v.getMeasuredWidth());
		//si la anchura de cada celda es mayor, podemos mostrar los horarios en la misma fila
		//MyLog.d(TAG, v.getMeasuredWidth());
		if(v.getMeasuredWidth() > 160){
			schedule_same_line = true;
		}

		if(this.view_schedule){
			int min_height;
			min_height = (schedule_same_line) ? 
					R.integer.MIN_HEIGHT_CALENDAR_ITEM_SCHEDULE_SAME_LINE : 
						R.integer.MIN_HEIGHT_CALENDAR_ITEM_SCHEDULE;				
			v.setMinimumHeight(Utils.getDptoPx(mContext, res.getInteger(min_height)));
		}else{
			v.setMinimumHeight(Utils.getDptoPx(mContext, res.getInteger(R.integer.MIN_HEIGHT_CALENDAR_ITEM)));
		}		
		dayView = (TextView) v.findViewById(R.id.date);
		dayView.setTextAppearance(v.getContext(), R.style.calendar_item_date);		
		TextView dayServiceView = (TextView) v.findViewById(R.id.date_servicio);
		dayServiceView.setTextAppearance(v.getContext(), R.style.calendar_item_date_servicio_default);		
		dayServiceView.setText("");
		dayServiceView.setTextColor(-1);
		dayView.setTextColor(-1);
		v.setVisibility(View.VISIBLE);
        TextView typeDayNoCountNaturalView =
                (TextView) v.findViewById(R.id.type_day_no_count_natural);
        typeDayNoCountNaturalView.setVisibility(View.GONE);
        //guarda la fecha completa del dia YYYY-mm-dd
        TextView dateLong = (TextView) v.findViewById(R.id.date_long);
        dateLong.setVisibility(View.GONE);
        dateLong.setText(days[position]);
		//imagen importante estrella
		ImageView important_image = (ImageView) v.findViewById(R.id.important_star);
		important_image.setVisibility(View.GONE);
		//imagen manutencion
		ImageView manutencion_entera = (ImageView) v.findViewById(R.id.manutencion_entera);
		manutencion_entera.setVisibility(View.GONE);
		//imagen media manutencion
		ImageView manutencion_media = (ImageView) v.findViewById(R.id.manutencion_media);
		manutencion_media.setVisibility(View.GONE);

		// disable empty days from the beginning
		if (days[position].equals("")) {
			v.setVisibility(View.INVISIBLE);
		} else {
			//MyLog.d("getview", "dia: " + selectedDate.get(Calendar.DAY_OF_MONTH));
			// mark current day as focused
            /*
			if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
					&& month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
					&& days[position].equals(""
							+ selectedDate.get(Calendar.DAY_OF_MONTH))) {
							*/
            if(days[position].equals(
                    CuadranteDates.formatDate(
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH) + 1,
                        selectedDate.get(Calendar.DAY_OF_MONTH)))){
				isViewDayFocused = true;
				if(Sp.getTodayColorActive(mContext)){
					v.setBackgroundColor(Sp.getTodayBgColor(mContext));
					dayServiceView.setTextColor(Sp.getTodayTextColor(mContext));
					dayView.setTextColor(Sp.getTodayTextColor(mContext));		
					typeDayNoCountNaturalView.setTextColor(Sp.getTodayTextColor(mContext));
				}else{
					v.setBackgroundResource(R.drawable.item_background_focused);					
					dayServiceView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
					dayView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
					typeDayNoCountNaturalView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
				}
			} else {
				v.setBackgroundResource(R.drawable.list_item_background);
			}
		}
		//si el dia es domingo
		boolean isSunday = false;
		if (Utils.esMultiplo(position + 1, 7) && !isViewDayFocused){
			isSunday = true;
			//v.setBackgroundColor(Cuadrante.SERVICE_DEFAULT_BG_COLOR_HOLIDAY);
			v.setBackgroundColor(Sp.getSundayBgColor(mContext));
			dayServiceView.setTextColor(Sp.getSundayTextColor(mContext));
			dayView.setTextColor(Sp.getSundayTextColor(mContext));
			typeDayNoCountNaturalView.setTextColor(Sp.getSundayTextColor(mContext));
		}
        CuadranteDates cDay = new CuadranteDates(days[position]);
		dayView.setText(Integer.toString(cDay.getDay()));
		
		// imprimimos la abreviatura del servicio de este dia
		if (daysWithServices[position].getId() > 0) {
			dayServiceView.setText(
					Cuadrante.getLengthServiceNameSubstring(
							mContext, daysWithServices[position].getName()));
			//ver el horario
			if(this.view_schedule){
				String separator = "";
				if(schedule_same_line){
					separator = " / ";
				}else{
					separator = "\n";
				}
				dayServiceView.setText("");
				if(!daysWithServices[position].getStartSchedule().equals(Cuadrante.SCHEDULE_NULL)
						&& !daysWithServices[position].getEndSchedule().equals(Cuadrante.SCHEDULE_NULL)){
					dayServiceView.setText(
							daysWithServices[position].getStartSchedule()
							.concat(separator)
							.concat(daysWithServices[position].getEndSchedule()));
				}
				if(!daysWithServices[position].getStartSchedule2().equals(Cuadrante.SCHEDULE_NULL)
						&& !daysWithServices[position].getEndSchedule2().equals(Cuadrante.SCHEDULE_NULL)){
					dayServiceView.setText(dayServiceView.getText().toString()
							.concat("\n")
							.concat(daysWithServices[position].getStartSchedule2())
							.concat(separator)
							.concat(daysWithServices[position].getEndSchedule2()));
				}
				dayServiceView.setTextAppearance(v.getContext(), R.style.calendar_item_date_servicio_schedule);
			}else{
				dayServiceView.setTextAppearance(v.getContext(), R.style.calendar_item_date_servicio);
			}
			dayView.setTextAppearance(v.getContext(), R.style.calendar_item_date);
			if(!isViewDayFocused){//si el dia no es el de hoy
				v.setBackgroundColor(Integer.parseInt(daysWithServices[position].getBgColor()));
				dayServiceView.setTextColor(Integer.parseInt(daysWithServices[position].getTextColor()));
				dayView.setTextColor(Integer.parseInt(daysWithServices[position].getTextColor()));
				typeDayNoCountNaturalView.setTextColor(Integer.parseInt(daysWithServices[position].getTextColor()));
			}else{
				if(Sp.getTodayColorActive(mContext)){
					dayServiceView.setTextColor(Sp.getTodayTextColor(mContext));
					dayView.setTextColor(Sp.getTodayTextColor(mContext));	
					typeDayNoCountNaturalView.setTextColor(Sp.getTodayTextColor(mContext));
				}else{				
					dayServiceView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
					dayView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
					typeDayNoCountNaturalView.setTextColor(Cuadrante.TODAY_DEFAULT_TEXT_COLOR);
				}
			}
			//si es domingo y esta activado lo del color rojo para los domingos
			if(isSunday && Sp.getSundayColorActive(mContext)){
				v.setBackgroundColor(Sp.getSundayBgColor(mContext));
				dayServiceView.setTextColor(Sp.getSundayTextColor(mContext));
				dayView.setTextColor(Sp.getSundayTextColor(mContext));
				typeDayNoCountNaturalView.setTextColor(Sp.getSundayTextColor(mContext));
			}
			//tipo de dia -1
			if(daysWithServices[position].getTypeDay() == 3){
				typeDayNoCountNaturalView.setVisibility(View.VISIBLE);
			}else//día importante
			if(daysWithServices[position].getIsImportant() == 1){
				important_image.setVisibility(View.VISIBLE);
			}else//derecho a manutención
			if(Sp.getCommissionsManutencionActive(mContext) 
					&& daysWithComisions[position].getId() == 0){//que no sea una comisión
				Comission comission = new Comission(daysWithServices[position]);
				switch (comission.hasManutencionOneDay()) {
				case 1:
					important_image.setVisibility(View.GONE);
					manutencion_media.setVisibility(View.VISIBLE);						
					break;
				case 2:
					important_image.setVisibility(View.GONE);
					manutencion_entera.setVisibility(View.VISIBLE);
					break;
				}
			}
			//MyLog.d("getView", daysWithServices[position].getDate() + " textColor:" + Integer.parseInt(daysWithServices[position].getTextColor()));

		}else if(daysWithComisions[position].getId() > 0){
			dayServiceView.setText(Cuadrante.getLengthServiceNameSubstring(mContext, Cuadrante.CALENDAR_SERVICE_COMISION));
			dayServiceView.setTextAppearance(v.getContext(), R.style.textCalendarComision);
		//Pas
		//si el pas está activo, sacado de MainActivity.calendarUpdater			
		//}else if(pas_active && !getItem(position).equals("") && pas_list.size() > 0){
		}else if(pasObj.active() && pasObj.isCreateList() && !getItem(position).equals("")){			
			//MyLog.d(TAG, "pas active:" + pasObj.active());
			//MyLog.d(TAG, "pas list:" + pasObj.isCreateList());
			DateTime dtView = new DateTime(
				month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1, 
				Integer.parseInt((String) getItem(position)), 0, 0);
			String pasText = pasObj.getDay(dtView);
			if(pasText != null){
				dayServiceView.setText(pasText);
				dayServiceView.setTextAppearance(v.getContext(), R.style.calendar_item_pas);				
			}
		}
		return v;
	}

	public void refreshDays() {
		// clear items
		items.clear();

		pasObj = new Pas(mContext);
        DateTime dtFirstDay, dtLastDay, dt;
        dtFirstDay = dt = new DateTime(month).withDayOfMonth(1).withMillisOfDay(0);
        dtLastDay = new DateTime(month).dayOfMonth().withMaximumValue().withMillisOfDay(0);

            if(dtFirstDay.getDayOfWeek() > 4){//jueves, pasamos a la siguiente semana
                dtFirstDay = dtFirstDay.plusWeeks(1).withDayOfWeek(1);
            }else{//cogemos toda la semana desde el lunes, siendo del mes pasado
                dtFirstDay = dtFirstDay.withDayOfWeek(1);
            }
            MyLog.d(TAG, "dtFirstDay:" + dtFirstDay.toString());
            if(dtLastDay.getDayOfWeek() >= 4){//cogemos el domingo de esta semana
                dtLastDay = dtLastDay.withDayOfWeek(7);
            }else{
                dtLastDay = dtLastDay.minusWeeks(1).withDayOfWeek(7);
            }
            MyLog.d(TAG, "dtLastDay:" + dtLastDay.toString());
            CuadranteDates dateFirst = new CuadranteDates(dtFirstDay);
            CuadranteDates dateLast = new CuadranteDates(dtLastDay);
            // obtenemos todos los servicios entre las fechas
            List<ServicioInfo> servicios = db.getServicesFromInterval(
                    dateFirst.getDate(), dateLast.getDate());
            //obtenemos todas las comisiones entre las fechas
            List<ComisionInfo> comisions = db.getComisionFromInterval(
                    dateFirst.getDate(), dateLast.getDate());
            int numDays =
                    CuadranteDates.getDaysBetweenDates(dateFirst.getDate(), dateLast.getDate()) + 1;
            MyLog.d(TAG, "numDays:" + numDays);
            days = new String[numDays];
            daysWithServices = new ServicioInfo[numDays];
            daysWithComisions = new ComisionInfo[numDays];
            // populate days
            DateTime dateDay = dtFirstDay;
            for (int i = 0; i < days.length; i++) {
                daysWithServices[i] = new ServicioInfo();
                for (ServicioInfo servicio : servicios) {
                    if (servicio.getDate().equals(CuadranteDates.formatDate(dateDay))) {
                        daysWithServices[i] = servicio;
                        break;
                    }
                }
                daysWithComisions[i] = new ComisionInfo();
                for (ComisionInfo comision : comisions) {
                    if (comision.getInterval().contains(dateDay)) {
                        daysWithComisions[i] = comision;
                        break;
                    }
                }
                days[i] = CuadranteDates.formatDate(dateDay);//"" + dateDay.dayOfMonth().get();
                dateDay = dateDay.plusDays(1);
            }

        /*
            int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
            MyLog.d(TAG, "lastDay: " + lastDay);
            int firstDay = (int) month.get(Calendar.DAY_OF_WEEK);
            MyLog.d(TAG, "firstDay: " + firstDay);
            // obtenemos todos los servicios del mes
            List<ServicioInfo> servicios = db.getServicesInMonth(
                    month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1);
            //obtenemos todas las comisiones del mes
            List<ComisionInfo> comisions = db.getComisionInMonth(
                    month.get(Calendar.YEAR), month.get(Calendar.MONTH) + 1);

            int numDays;
            // figure size of the array
            if (firstDay == 1) {
                numDays = lastDay + (FIRST_DAY_OF_WEEK * 6);
            } else {
                numDays = lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1);
            }
            days = new String[numDays];
            daysWithServices = new ServicioInfo[numDays];
            daysWithComisions = new ComisionInfo[numDays];
            int j = FIRST_DAY_OF_WEEK;

            // populate empty days before first real day
            if (firstDay > 1) {
                for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                    days[j] = "";
                    daysWithServices[j] = new ServicioInfo();
                    daysWithComisions[j] = new ComisionInfo();
                    // MyLog.d("firstDay > 1 j", "" + j);
                }
            } else {
                for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                    days[j] = "";
                    daysWithServices[j] = new ServicioInfo();
                    daysWithComisions[j] = new ComisionInfo();
                }
                j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
            }

            // populate days
            int dayNumber = 1;
            DateTime dateDay = new DateTime(month.get(Calendar.YEAR),
                    month.get(Calendar.MONTH) + 1, dayNumber, 0, 0);
            for (int i = j - 1; i < days.length; i++) {
                daysWithServices[i] = new ServicioInfo();
                for (ServicioInfo servicio : servicios) {
                    if (servicio.getDay() == dayNumber) {
                        daysWithServices[i] = servicio;
                        break;
                    }
                }
                daysWithComisions[i] = new ComisionInfo();
                for (ComisionInfo comision : comisions) {
                    if (comision.getInterval().contains(dateDay)) {
                        daysWithComisions[i] = comision;
                        break;
                    }
                }
                dateDay = dateDay.plusDays(1);
                days[i] = "" + dayNumber;
                dayNumber++;
            }
        }
        */
	}
	
	/**
	 * Determina si el calendario hay que verse en horarios o servicios
	 * @param view
	 */
	public void setViewSchedule(boolean view){
		this.view_schedule = view;
	}
	
	public boolean getViewSchedule(){
		return this.view_schedule;
	}


}