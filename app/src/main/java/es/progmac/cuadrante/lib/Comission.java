package es.progmac.cuadrante.lib;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import es.progmac.cuadrante.info.ServicioInfo;

public class Comission {
	/** Si el servicio dura dos días */
	private boolean mServiceTwoDays = false;
	private DateTime mDTStart = new DateTime();
	private DateTime mDTEnd = new DateTime();
	private DateTime mDTStart2 = null;
	private DateTime mDTEnd2 = null;
	private DateTime mDT14 = new DateTime();
	private DateTime mDTEnd14 = new DateTime();
	private DateTime mDT16 = new DateTime();
	private DateTime mDT22 = new DateTime();
	/** Horas de duración del servicio */
	private int mDurationHours = 0;		
	private ServicioInfo mService = new ServicioInfo();
	
	public Comission(ServicioInfo service){
		mService = service;
		if(service.getStartSchedule().equals(Cuadrante.SCHEDULE_NULL)){
			return;
		}
		mDTStart = service.getDateTime()
				.withHourOfDay(CuadranteDates.getHour(service.getStartSchedule()))
				.withMinuteOfHour(CuadranteDates.getMinutes(service.getStartSchedule()));
		mDTEnd = service.getDateTime()
				.withHourOfDay(CuadranteDates.getHour(service.getEndSchedule()))
				.withMinuteOfHour(CuadranteDates.getMinutes(service.getEndSchedule()));
		if(!service.getStartSchedule2().equals(Cuadrante.SCHEDULE_NULL)){
			mDTStart2 = service.getDateTime()
					.withHourOfDay(CuadranteDates.getHour(service.getStartSchedule2()))
					.withMinuteOfHour(CuadranteDates.getMinutes(service.getStartSchedule2()));
			mDTEnd2 = service.getDateTime()
					.withHourOfDay(CuadranteDates.getHour(service.getEndSchedule2()))
					.withMinuteOfHour(CuadranteDates.getMinutes(service.getEndSchedule2()));
		}

		Duration duration = new Duration(mDTStart, mDTEnd);
	
		//si sale negativo es que la hora final es inferior a la hora
		//inicial (22:00 - 06:00) asi que sumamos un día más a la fecha fin 
		//para poder restarla bien y que salga las horas correctas.
		if(duration.getMillis() <= 0){
			mServiceTwoDays = true;
			mDTEnd = mDTEnd.plusDays(1);
			duration = new Duration(mDTStart, mDTEnd);
		}	
		mDurationHours = duration.toPeriod().getHours();		
		mDT14 = mDTStart.withHourOfDay(14).withMinuteOfHour(0);
		mDTEnd14 = mDTEnd.withHourOfDay(14).withMinuteOfHour(0);
		mDT16 = mDTStart.withHourOfDay(16).withMinuteOfHour(0);
		mDT22 = mDTStart.withHourOfDay(22).withMinuteOfHour(0);		
	}
	
	/**
	 * Averigua si el horario del servicio tiene derecho a manutención o no
	 * Real decreto 462/2002, de 24 de mayo, por indemnizaciones por razón de servicio
	 * @param service
	 * @return 0 no, 1 media, 2 entera
	 */
	public int hasManutencionOneDay(){		
		if(mServiceTwoDays){
			//antes de las 14:00
			if(mDTStart.isBefore(mDT14)){
				return 2;
			//antes de las 22:00 y despues de las 14:00 del día siguiente
			}else if(mDTStart.isBefore(mDT22) && mDTEnd.isAfter(mDTEnd14)){
				return 2;
			//antes de las 22:00 y antes de las 14:00 del día siguiente
			}else if(mDTStart.isBefore(mDT22) && mDTEnd.isBefore(mDTEnd14)){
				return 1;
			//después o igual de las 22:00 y después de las 14:00
			}else if((mDTStart.isAfter(mDT22) || mDTStart.isEqual(mDT22)) && mDTEnd.isAfter(mDTEnd14)){
				return 1;
			}
		}else if(mDurationHours >= 5){
			//servicio que empieza antes de las 14:00 y termina despues de las 16:00
			if(mDTStart.isBefore(mDT14) && mDTEnd.isAfter(mDT16)){
				return 1;
			}
		}			
		return 0;
	}
	
	/**
	 * Averigua si el día de salida de una comisión se tiene derecho o no a dieta
	 * @return 0 no, 1 media, 2 entera
	 */
	public int hasManutencionStartDay(){
		if(mDTStart.isBefore(mDT14)) return 2;
		else if((mDTStart.isAfter(mDT14) || mDTStart.isEqual(mDT14)) 
				&& mDTStart.isBefore(mDT22)) return 1;
		return 0;
	}
	
	/**
	 * Averigua si el día de regreso de una comisión se tiene derecho o no a dieta
	 * @return 0 no, 1 media, 2 entera
	 */
	public int hasManutencionEndDay(){
		if(mDTStart2 != null){
			if(mDTEnd2.isAfter(mDT14)) return 1;
		}else if(mDTEnd.isAfter(mDT14)) return 1;
		return 0;
	}
}
