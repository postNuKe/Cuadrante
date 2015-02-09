package es.progmac.cuadrante;

import org.joda.time.DateTime;

import es.progmac.cuadrante.R;
import es.progmac.cuadrante.lib.Cuadrante;
import es.progmac.cuadrante.lib.CuadranteDates;
import es.progmac.cuadrante.lib.Extra;
import es.progmac.cuadrante.lib.Sp;

import es.progmac.android.others.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	// Values for email and password at the time of the login attempt.
	private String mPassword;
	private String mCurrentPassword;
	private String mNewPassword;
	private String mNewPasswordRepeat;
	private int mExpireSession;

	// UI references.
	private CheckBox mActiveLoginView;
	private CheckBox mActiveWidgetView;
	private Spinner mExpireSessionView;
	private EditText mPasswordView;
	private EditText mCurrentPasswordView;
	private EditText mNewPasswordView;
	private EditText mNewPasswordRepeatView;
	
	private String mMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			mMode = getIntent().getStringExtra(Extra.MODE);
			if(mMode.equals("edit")) editLogin();
		}else{
			login();
		}
		
	}
	
	private void login(){
		setContentView(R.layout.activity_login);
		mPasswordView = (EditText) findViewById(R.id.password);
		
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}
	
	private void attemptLogin() {
		// Reset errors.
		mPasswordView.setError(null);
		
		// Store values at the time of the login attempt.
		mPassword = mPasswordView.getText().toString();
		
		
		boolean cancel = false;
		View focusView = null;


		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else{
			try {
				if(!Sp.getLoginPassword(this).equals(Utils.encrypt(mPassword, Cuadrante.SECRET_KEY))){
					mPasswordView.setError(getString(R.string.error_incorrect_password));
					focusView = mPasswordView;
					cancel = true;				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				focusView = mPasswordView;
				cancel = true;				
			}
		}
		
		//existe algun error en el form
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			Sp.setLoginExpireSessionDate(this, CuadranteDates.formatDateTime(new DateTime()));
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);	
			this.finish();
		}		
	}
	
	private void editLogin() {
		setContentView(R.layout.activity_login_preferences);
		// Set up the login form.
		mActiveLoginView = (CheckBox) findViewById(R.id.activate_login);
		mActiveWidgetView = (CheckBox) findViewById(R.id.activate_widget);
		mExpireSessionView = (Spinner) findViewById(R.id.expire_session);
		mExpireSessionView.setSelection(Sp.getLoginExpireSessionDuration(this));
		mCurrentPasswordView = (EditText) findViewById(R.id.current_password);
		if(!Sp.getLoginActivate(this)){
			mCurrentPasswordView.setVisibility(View.GONE);
		}else{
			mActiveLoginView.setChecked(true);
		}
		if(Sp.getWidgetActivate(this)){
			mActiveWidgetView.setChecked(true);
		}
		mNewPasswordView = (EditText) findViewById(R.id.new_password);
		mNewPasswordRepeatView = (EditText) findViewById(R.id.new_password_repeat);
		
		findViewById(R.id.save_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptEditLogin();
					}
				});
		
	}

	protected void attemptEditLogin() {
		// Reset errors.
		mCurrentPasswordView.setError(null);
		mNewPasswordView.setError(null);
		mNewPasswordRepeatView.setError(null);
		
		// Store values at the time of the login attempt.
		mCurrentPassword = mCurrentPasswordView.getText().toString();
		mNewPassword = mNewPasswordView.getText().toString();
		mNewPasswordRepeat = mNewPasswordRepeatView.getText().toString();
		mExpireSession = mExpireSessionView.getSelectedItemPosition();
		
		
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if(Sp.getLoginActivate(this)){
			if (TextUtils.isEmpty(mCurrentPassword)) {
				mCurrentPasswordView.setError(getString(R.string.error_field_required));
				focusView = mCurrentPasswordView;
				cancel = true;
			} else if (mCurrentPassword.length() < 4) {
				mCurrentPasswordView.setError(getString(R.string.error_invalid_password));
				focusView = mCurrentPasswordView;
				cancel = true;
			} else{
				try {
					if(!Sp.getLoginPassword(this).equals(Utils.decrypt(mCurrentPassword, Cuadrante.SECRET_KEY))){
						mCurrentPasswordView.setError(getString(R.string.error_incorrect_password));
						focusView = mCurrentPasswordView;
						cancel = true;				
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//Intent intent = new Intent();
					//setResult(Cuadrante.RESULT_ERROR, intent);
					//this.finish();					
				}
			}
		}
		
		if (TextUtils.isEmpty(mNewPassword)) {
			mNewPasswordView.setError(getString(R.string.error_field_required));
			focusView = mNewPasswordView;
			cancel = true;
		} else if (mNewPassword.length() < 4) {
			mNewPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mNewPasswordView;
			cancel = true;
		}else if(!mNewPassword.equals(mNewPasswordRepeat)){
			mNewPasswordView.setError(getString(R.string.error_incorrect_new_password));
			focusView = mNewPasswordView;
			cancel = true;			
		}
		
		if (TextUtils.isEmpty(mNewPasswordRepeat)) {
			mNewPasswordRepeatView.setError(getString(R.string.error_field_required));
			focusView = mNewPasswordRepeatView;
			cancel = true;
		} else if (mNewPasswordRepeat.length() < 4) {
			mNewPasswordRepeatView.setError(getString(R.string.error_invalid_password));
			focusView = mNewPasswordRepeatView;
			cancel = true;
		}	
		
		//existe algun error en el form
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			//esta activado el checkbox del login
			if(mActiveLoginView.isChecked()){
				try {
					Sp.setLoginPassword(this, Utils.encrypt(mNewPassword, Cuadrante.SECRET_KEY));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//Intent intent = new Intent();
					//setResult(Cuadrante.RESULT_ERROR, intent);
					//this.finish();					
				}
				Sp.setLoginActivate(this, true);
				Sp.setLoginExpireSessionDuration(this, mExpireSession);
				Sp.setLoginExpireSessionDate(this, CuadranteDates.formatDateTime(new DateTime()));
				Sp.setWidgetActivate(this, mActiveWidgetView.isChecked());
			}else{
				Cuadrante.setLoginDataDefaultValue(this);
			}
			Cuadrante.refreshWidget(this);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			this.finish();
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
}
