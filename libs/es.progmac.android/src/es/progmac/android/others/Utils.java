package es.progmac.android.others;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import es.progmac.android.others.Base64;
public class Utils {
	/**
	 * Encripta una cadena de texto
	 * @param toEncrypt
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String toEncrypt, byte[] key) throws Exception {
		//Create your Secret Key Spec, which defines the key transformations
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");


		//Get the cipher
		Cipher cipher = Cipher.getInstance("AES");

		//Initialize the cipher
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		//Encrypt the string into bytes
		byte[ ] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());

		//Convert the encrypted bytes back into a string
		String encrypted = Base64.encodeBytes(encryptedBytes);

		return encrypted;
	}	
	
	/**
	 * Desencripta una cadena de texto
	 * @param encryptedText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptedText, byte[] key) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] toDecrypt = Base64.decode(encryptedText);

		byte[] encrypted = cipher.doFinal(toDecrypt);

		return new String(encrypted);
	}	
	
	/**
	 * Convierte una medida en Dp a Px
	 * @param ctx
	 * @param dp
	 * @return
	 */
	public static int getDptoPx(Context ctx, int dp){
		// Get the screen's density scale
		final float scale = ctx.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return  (int) (dp * scale + 0.5f);
	}
	
	/**
	 * Convert dp to px
	 */
	public static int dp2px (Context context, int dp) {
		return (int)TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}	
	
	/**
	 * Mira si n2 es multiplo de n1
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static boolean esMultiplo(int n1, int n2) {
		if (n1 % n2 == 0)
			return true;
		else
			return false;
	}	
	
	/**
	 * Concatena tantos enteros como se quiera
	 * @param int digits
	 * @return int
	 */
	public static int concatenateDigits(int... digits) {
		StringBuilder sb = new StringBuilder(digits.length);
		for (int digit : digits) {
			sb.append(digit);
		}
		return Integer.parseInt(sb.toString());
	}		
	
	/**
	 * This method let us know when the device has connectivity and when not
	 * @return	true if online, false otherwise
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// If getActiveNetworkInfo() is null, there is no connection
		if (cm.getActiveNetworkInfo() == null) {
			return false;
		}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}
	
	/**
	 * Hide the keyboard if possible
	 * @param context
	 * @param activity
	 */
	public static void hideKeyboard(Context context) {
		// Hide the keyboard
		InputMethodManager inputManager = (InputMethodManager)            
		context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		if (((Activity) context).getCurrentFocus() != null) {
			inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),      
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	
	/**
	 * Show the keyboard if possible
	 * @param context
	 * @param activity
	 */
	public static void showKeyboard(Context context) {
		// show the keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

	}
	
	/**
	 * Use a cascade animation for the given listview
	 * @param list
	 */
	public static void setCascadeAnimation (ListView list) {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f
		);
		animation.setDuration(100);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);        
		list.setLayoutAnimation(controller);
	}
	
}