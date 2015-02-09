package es.progmac.cuadrante.lib;

import android.util.Log;

public class MyLog{
	/**
	 * Para que los debug funcionen o no, ya que consumen recursos y no nos interesa en producción 
	 */
	private static boolean DEBUG_MODE = true;

	public MyLog() {
		// TODO Auto-generated constructor stub
	}
	
	public static void d(String tag, String print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE) Log.d(tag, print);
	}
	public static void d(String tag, int print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE)Log.d(tag, "" + print);
	}
	public static void d(String tag, long print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE)Log.d(tag, "" + print);
	}
	public static void d(String tag, double print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE)Log.d(tag, "" + print);
	}
	public static void d(String tag, float print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE)Log.d(tag, "" + print);
	}
	public static void d(String tag, boolean print){
		if(DEBUG_MODE && Cuadrante.DEBUG_MODE) Log.d(tag, "" + print);
	}

}
