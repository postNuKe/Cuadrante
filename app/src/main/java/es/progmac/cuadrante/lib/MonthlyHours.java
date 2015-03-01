package es.progmac.cuadrante.lib;

import android.util.SparseArray;
import android.util.SparseIntArray;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import es.progmac.cuadrante.info.HoursInfo;
import es.progmac.cuadrante.info.ServicioInfo;

/**
 * Gestiona las horas y demás valores de un mes
 * Created by macias on 22/02/15.
 */
public class MonthlyHours {
    private Context mContext;
    private DatabaseHandler mDb;
    private Calendar mCalendar;
    private int mActualMonth;
    private String mWorkDayComputing;
    private HoursInfo mActualMonthInfo = new HoursInfo();
    private double mTotalReferenceHours = 0;
    private double mTotalHours = 0;

    public static final String HOURS = "hours";
    public static final String MINUTES = "minutes";

    private static final String TAG = "MonthlyHours";

    public static final String MONTHLY = "monthly";
    public static final String QUARTERLY = "quarterly";
    public static final String QUARTERLY2 = "quarterly2";


    /**
     *
     * @param ctx
     * @param db
     * @param calendar
     * @param actualMonth
     */
    public MonthlyHours(Context ctx, DatabaseHandler db, Calendar calendar, int actualMonth){
        _monthlyHours(ctx, db, calendar, actualMonth, MONTHLY);
    }

    /**
     *
     * @param ctx
     * @param db
     * @param calendar
     * @param actualMonth
     * @param workDayComputing tipo de computo mensual
     */
    public MonthlyHours(Context ctx, DatabaseHandler db, Calendar calendar, int actualMonth, String workDayComputing){
        _monthlyHours(ctx, db, calendar, actualMonth, workDayComputing);
    }

    public void _monthlyHours(Context ctx, DatabaseHandler db, Calendar calendar, int actualMonth, String workDayComputing){
        mContext = ctx;
        mDb = db;
        mCalendar = calendar;
        mActualMonth = actualMonth;
        mWorkDayComputing = workDayComputing;

        switch (workDayComputing){
            case MONTHLY:
                mActualMonthInfo = mDb.getMonthHours(calendar.get(Calendar.YEAR), actualMonth);
                if(mActualMonthInfo.getId() > 0){
                }else{
                    MyLog.d(TAG, "computo jornada:" + workDayComputing);
                    Calendar tmpMonth = (Calendar) calendar.clone();
                    tmpMonth.set(Calendar.MONTH, actualMonth - 1);
                    CalendarAdapter tmpAdapter = new CalendarAdapter(mContext, tmpMonth);
                    mActualMonthInfo = setHoursInfo(tmpMonth, tmpAdapter);
                }
                mTotalReferenceHours += mActualMonthInfo.getReference();
                mTotalHours += mActualMonthInfo.getHours();

                break;
            case QUARTERLY:
            case QUARTERLY2:
                setQuarter();
                break;
        }
    }

    /**
     * Establece las horas del trimestre o cuatrimestre
     */
    private void setQuarter(){
        SparseArray<HoursInfo> quarterMonthsHours = new SparseArray<HoursInfo>();
        List<Integer> months = new ArrayList<Integer>();
        switch (mWorkDayComputing){
            case QUARTERLY:
                quarterMonthsHours = mDb.getQuarterHours(mCalendar.get(Calendar.YEAR), mActualMonth);
                months = CuadranteDates.getQuarterMonths(mActualMonth);
                break;
            case QUARTERLY2:
                quarterMonthsHours = mDb.getQuarter2Hours(mCalendar.get(Calendar.YEAR), mActualMonth);
                months = CuadranteDates.getQuarter2Months(mActualMonth);
                break;
        }
        for (Integer m : months) {
            //MyLog.d(TAG, "----------------------------------");
            //MyLog.d(TAG, "m: " + m + " actualMonth:" + actualMonth);
            HoursInfo monthInfo = new HoursInfo();
            //MyLog.d(TAG, "mes:" + m);
            //si no existe en la bd grabado los datos del mes
            if (quarterMonthsHours.indexOfKey(m) < 0) {
                //MyLog.d(TAG, "el mes " + m + " no tiene horas guardadas");
                Calendar tmpMonth = (Calendar) mCalendar.clone();
                tmpMonth.set(Calendar.MONTH, m - 1);
                CalendarAdapter tmpAdapter = new CalendarAdapter(mContext, tmpMonth);
                //MyLog.d(TAG, tmpMonth.get(Calendar.MONTH) + " " + tmpMonth.get(Calendar.YEAR));
                monthInfo = setHoursInfo(tmpMonth, tmpAdapter);
            } else {
                monthInfo = quarterMonthsHours.get(m);
            }
            mTotalReferenceHours += monthInfo.getReference();
            mTotalHours += monthInfo.getHours();
            if (m == mActualMonth) {
                mActualMonthInfo = monthInfo;
            }
        }
    }

    private HoursInfo setHoursInfo(Calendar actualMonth, CalendarAdapter calendarAdapter) {
        /**
         * Horas totales trabajadas, hours, minutes
         */
        HashMap<String, Integer> total_time = new HashMap<String, Integer>();
        total_time.put(HOURS, 0);
        total_time.put(MINUTES, 0);

        //MyLog.d(TAG, "actualMonth:" + actualMonth.get(Calendar.MONTH));
        //miramos el servicio del ultimo dia del mes anterior por si tiene
        //horas pertenecientes al mes actual (22:00 - 06:00 pejem)
        int previousMonth = actualMonth.get(Calendar.MONTH);
        //previousMonth--;
        int previousYear = actualMonth.get(Calendar.YEAR);
        if (actualMonth.get(Calendar.MONTH) == 0) {
            previousMonth = 12;
            previousYear--;
        }
        //MyLog.d(TAG, "previousMonth:" + previousMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.set(previousYear, previousMonth - 1, 1);
        //lastServicePreviousMonth
        ServicioInfo lSPM = mDb.getServicio(
                CuadranteDates.formatDate(
                        previousYear,
                        previousMonth,
                        calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        if (lSPM.getId() > 0) {
            MyLog.d("refreshResume", "date previous service month:" + lSPM.getDate());
            Period day_period_previous = getTimeFromService(lSPM, 1, true);
            Period day_period_previous2 = getTimeFromService(lSPM, 2, true);
            Period day_period_previous3 = getTimeFromService(lSPM, 3, true);
            Period day_period_previous4 = getTimeFromService(lSPM, 4, true);
            int hours = day_period_previous.getHours() + day_period_previous2.getHours() +
                    day_period_previous3.getHours() + day_period_previous4.getHours();
            int minutes = day_period_previous.getMinutes() +
                    day_period_previous2.getMinutes() + day_period_previous3.getMinutes() +
                    day_period_previous4.getMinutes();

            total_time.put(HOURS, hours);
            total_time.put(MINUTES, minutes);
            //MyLog.d("refreshResume", "time previous service month:" + hours + " " + minutes);
        }

        ServicioInfo[] dServices = calendarAdapter.daysWithServices;

        /**
         * Cantidad de veces que hay de cada tipo de dia
         * <type_day, qty>
         */
        SparseIntArray type_day_qty = new SparseIntArray();
        //cuenta el número de dias deducibles en una semana, si hay 5 o más la semana suma 37.5
        //o 40, si no sumaria 7*7.5 = 52.5 o 60 si fuera la jornada de 40
        int numDeductibleDayWeek = 0;
        int numWeek = 0;

        int[] arrayWeeks = new int[CuadranteDates.getNumWeeksMonth(actualMonth)];
        int j = 0;
        // miramos todos los dias del mes si tienen servicio
        for (int i = 0; i < dServices.length; i++) {
            MyLog.d(TAG, "total: " + dServices.length + " i:" + i);
            if (dServices[i].getId() > 0) {// el dia tiene servicio
                int hoursWorkingDay = 0, minutesWorkingDay = 0, hours = 0, minutes = 0;
                Map<String, Integer> workDay = new HashMap<String, Integer>();
                MyLog.d(TAG, "servicio:" + dServices[i].getDateTime() + " week:" + dServices[i].getDateTime().getWeekOfWeekyear());

                //si cambiamos de semana mirar si la anterior semana ha habido dias deducibles y
                //si es asi sumar las horas, si superan 5 dias solo se suma la jornada laboral
                //maxima
                if(numWeek != dServices[i].getDateTime().getWeekOfWeekyear() ||
                        i == dServices.length - 1){//ultimo servicio grabado en el mes
                    //Si es la misma semana significa que estamos a ultimo servicio del mes por
                    //lo que mirar si este servicio es deducible
                    if(numWeek == dServices[i].getDateTime().getWeekOfWeekyear() &&
                            dServices[i].getTypeDay() == 1){
                        numDeductibleDayWeek++;
                    }
                    if(numDeductibleDayWeek > 0) {
                        //dependiendo si la jornada es 37.5 un dia deducible vale 7.5
                        //si son 40 vale 8
                        workDay = Cuadrante.getTimeWorkDate(mContext);
                        hours = numDeductibleDayWeek * workDay.get(Cuadrante.HOURS);
                        minutes = numDeductibleDayWeek * workDay.get(Cuadrante.MINUTES);
                        if (numDeductibleDayWeek >= 5) {
                            hours = workDay.get(Cuadrante.HOURS) * 5;
                            minutes = workDay.get(Cuadrante.MINUTES) * 5;
                        }
                        if (total_time.containsKey(HOURS)) {
                            total_time.put(HOURS, total_time.get(HOURS) + hours);
                            total_time.put(MINUTES, total_time.get(MINUTES) + minutes);
                        } else {
                            total_time.put(HOURS, hours);
                            total_time.put(MINUTES, minutes);
                        }
                        numDeductibleDayWeek = 0;
                    }
                    numWeek = dServices[i].getDateTime().getWeekOfWeekyear();
                }

                Period day_period = getTimeFromService(dServices[i], 1, false);
                Period day_period2 = getTimeFromService(dServices[i], 2, false);
                Period day_period3 = getTimeFromService(dServices[i], 3, false);
                Period day_period4 = getTimeFromService(dServices[i], 4, false);
                hours = day_period.getHours() + day_period2.getHours() +
                        day_period3.getHours() + day_period4.getHours();
                minutes = day_period.getMinutes() +
                        day_period2.getMinutes() + day_period3.getMinutes() +
                        day_period4.getMinutes();

                if(dServices[i].getGuardiaCombinada() == 1){
                    //obtenemos la diferencia de minutes que hay entre las presenciales y las 24h
                    //del dia
                    int minLocalizado = (24*60 - ((hours * 60) + minutes)) / 4;
                    hours += minLocalizado / 60;
                    minutes += minLocalizado % 60;
                    int minTotal = hours * 60 + minutes;
                    //si las horas totales del dia son inferiores a 7.5 u 8 poner que valga eso
                    switch (Sp.getSpWorkdayWeekHours(mContext)){
                        case "37.5":
                            if(minTotal < 450) {
                                hours = 7;
                                minutes = 30;
                            }
                            break;
                        case "40":
                            if(minTotal < 480) {
                                hours = 8;
                                minutes = 0;
                            }
                            break;
                    }
                }else if (dServices[i].getTypeDay() == 1) {//dia deducible
                    numDeductibleDayWeek++;
                }
                if (total_time.containsKey(HOURS)) {
                    total_time.put(HOURS, total_time.get(HOURS) + hours);
                    total_time.put(MINUTES, total_time.get(MINUTES) + minutes);
                } else {
                    total_time.put(HOURS, hours);
                    total_time.put(MINUTES, minutes);
                }
            }else if(i == dServices.length - 1){//ultimo dia del mes
                //mirar si falta por grabar dias deducibles
                //dependiendo si la jornada es 37.5 un dia deducible vale 7.5
                //si son 40 vale 8
                Map<String, Integer> workDay = Cuadrante.getTimeWorkDate(mContext);
                int hours = numDeductibleDayWeek * workDay.get(Cuadrante.HOURS);
                int minutes = numDeductibleDayWeek * workDay.get(Cuadrante.MINUTES);
                if (numDeductibleDayWeek >= 5) {
                    hours = workDay.get(Cuadrante.HOURS) * 5;
                    minutes = workDay.get(Cuadrante.MINUTES) * 5;
                }
                total_time.put(HOURS, total_time.get(HOURS) + hours);
                total_time.put(MINUTES, total_time.get(MINUTES) + minutes);
            }
        }

        //miramos si los minutos se pueden pasar a horas
        if (total_time.containsKey(MINUTES)) {
            if (total_time.get(MINUTES) >= 60) {
                //sumamos la división entre los minutos y 60
                total_time.put(HOURS, total_time.get(HOURS) + total_time.get(MINUTES) / 60);
                //el resto de minutos los ponemos como minutos
                total_time.put(MINUTES, total_time.get(MINUTES) % 60);
            }
        }
        int minutes = (total_time.get(MINUTES) * 100) / 60;
        double totalTimeService = Double.parseDouble(total_time.get(HOURS) + "." + minutes);
        double totalTime = totalTimeService;


        //obtenemos las horas de referencia mensuales
        int reference_hours = Cuadrante.getReferenceHours2(mContext, actualMonth);
        MyLog.d(TAG, "referencia:" + reference_hours);

        //HE= HPS – HJT (STP + DSP/7 – SND – DND/5)

        HoursInfo hoursInfo = new HoursInfo(
                actualMonth.get(Calendar.YEAR),
                actualMonth.get(Calendar.MONTH) + 1,
                totalTime,
                reference_hours,
                0,
                0,
                0);

        //MyLog.d(TAG, "mes: " + hoursInfo.getMonth());
        //MyLog.d(TAG, "horas: " + Double.parseDouble(total_time.get(HOURS) + "." + minutes));
        //MyLog.d(TAG, "f2: " + hoursToF2);
        //MyLog.d(TAG, "year:" + hoursInfo.getYear() + " month:" + hoursInfo.getMonth());
        //MyLog.d(TAG, "hours:" + hoursInfo.getHours() + " reference:" + hoursInfo.getReference() + " f2:" + hoursInfo.getF2());
        //MyLog.d(TAG, "guarcias:" + hoursInfo.getGuardias());
        mDb.insertHours(hoursInfo);

        return hoursInfo;
    }

    /**
     * Obtiene el periodo de tiempo de un servicio
     * @param serv
     * @param modeSchedule 1, startSchedule, 2 startSchedule2, etc...
     * @return
     */
    private Period getTimeFromService(ServicioInfo serv, int modeSchedule, boolean isServicePreviousMonth){
        switch(modeSchedule){
            case 1:
                if (!serv.getStartSchedule().equals(Cuadrante.SCHEDULE_NULL) ||
                        !serv.getEndSchedule().equals(Cuadrante.SCHEDULE_NULL) &&
                                serv.getTypeDay() == 0) {
                    //MyLog.d(TAG, "1:" + serv.getStartSchedule() + " " + serv.getEndSchedule() + " tipo dia:" + serv.getTypeDay());
                    return Cuadrante.getTimeFromService(
                            serv.getYear(),
                            serv.getMonth(),
                            serv.getDay(),
                            serv.getStartSchedule(),
                            serv.getEndSchedule(),
                            isServicePreviousMonth);
                }
                break;
            case 2:
                if (!serv.getStartSchedule2().equals(Cuadrante.SCHEDULE_NULL) ||
                        !serv.getEndSchedule2().equals(Cuadrante.SCHEDULE_NULL) &&
                                serv.getTypeDay() == 0) {
                    return Cuadrante.getTimeFromService(
                            serv.getYear(),
                            serv.getMonth(),
                            serv.getDay(),
                            serv.getStartSchedule2(),
                            serv.getEndSchedule2(),
                            isServicePreviousMonth);
                }
                break;
            case 3:
                if (!serv.getStartSchedule3().equals(Cuadrante.SCHEDULE_NULL) ||
                        !serv.getEndSchedule3().equals(Cuadrante.SCHEDULE_NULL) &&
                                serv.getTypeDay() == 0) {
                    return Cuadrante.getTimeFromService(
                            serv.getYear(),
                            serv.getMonth(),
                            serv.getDay(),
                            serv.getStartSchedule3(),
                            serv.getEndSchedule3(),
                            isServicePreviousMonth);
                }
                break;
            case 4:
                if (!serv.getStartSchedule4().equals(Cuadrante.SCHEDULE_NULL) ||
                        !serv.getEndSchedule4().equals(Cuadrante.SCHEDULE_NULL) &&
                                serv.getTypeDay() == 0) {
                    return Cuadrante.getTimeFromService(
                            serv.getYear(),
                            serv.getMonth(),
                            serv.getDay(),
                            serv.getStartSchedule4(),
                            serv.getEndSchedule4(),
                            isServicePreviousMonth);
                }
                break;
        }
        return new Period();
    }

    public double getReferenceHours(){
        return Cuadrante.round(Cuadrante.doubleTwoDigits(mTotalReferenceHours));
    }
    public double getHours(){
        return mTotalHours;
    }

    public HoursInfo getHoursInfo(){ return mActualMonthInfo;}
}