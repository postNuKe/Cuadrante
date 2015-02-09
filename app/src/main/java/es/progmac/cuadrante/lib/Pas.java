package es.progmac.cuadrante.lib;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Weeks;

import es.progmac.cuadrante.R;

import android.content.Context;

public class Pas {
	public String TAG ="Pas";
	
	private Context mContext;
	/** Inicial del PAS elegido por el user */
	private String mPas = "";
	/** Fecha del PAS elegido en config */
	private DateTime mPasDt = new DateTime();
	/** Listado con las diferentes semanas del PAS */
	private ArrayList<String> mPasList = new ArrayList<String>();
	
	/**
	 * Creación del PAS
	 */
	public Pas(){
		
	}
	
	public Pas(Context context, String pas){
		_Pas(context, pas);
	}
	
	public Pas(Context context){
		_Pas(context, Sp.getPas(context));
	}
	
	private void _Pas(Context context, String pas){
		mContext = context;
		mPas = pas;
		if(!active()) return;
		mPasDt = new DateTime(Sp.getPasYear(mContext), 1, 1, 0, 0)
			.withWeekOfWeekyear(Sp.getPasWeek(mContext))
			.withDayOfWeek(DateTimeConstants.MONDAY);	
		//MyLog.d(TAG, "mPas:" + mPas);
		//MyLog.d(TAG, "mPasDt:" + mPasDt.toString());
		if(mPas.equals("p")){
			mPasList.add("P");
			mPasList.add("A");
			mPasList.add("S");
		}else if(mPas.equals("a")){
			mPasList.add("A");
			mPasList.add("S");
			mPasList.add("P");		
		}else if(mPas.equals("s")){
			mPasList.add("S");
			mPasList.add("P");		
			mPasList.add("A");						
		}			
	}
	
	/**
	 * Devuelve según el día, de qué pas está ese día (P, A, S)
	 * @param dt
	 * @return
	 */
	public String getDay(DateTime dt){
		if(!active()) return null;
		Days days = Days.daysBetween(mPasDt, dt);
		if(days.getDays() >= 0 && mPasList.size() > 0){//dias para adelante del primer pas
			int rest = Weeks.weeksBetween(mPasDt, dt).getWeeks() % 3;
			return mPasList.get(rest);			
		}
		return null;
	}
	
	public boolean active(){
		return Sp.getPasActive(mContext);
	}
	
	/**
	 * Si está creado el array de semanas del PAS
	 * @return
	 */
	public boolean isCreateList(){
		if(mPasList.size() > 0) return true;
		else return false;
	}
}
