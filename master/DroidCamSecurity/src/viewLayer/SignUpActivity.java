package viewLayer;

import viewLayer.util.Validations;

import businessLayer.Authentication;

import com.example.droidcamsecurity.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void resgisterAccount(View view){
		EditText email = (EditText) findViewById(R.id.eText_email);
		EditText password = (EditText) findViewById(R.id.eText_password);
		EditText confPwd = (EditText) findViewById(R.id.eText_confpwd);
		EditText ipCam = (EditText) findViewById(R.id.eText_ipcam);
		EditText camPwd = (EditText) findViewById(R.id.eText_ippwd);
		int error = 0;
		
		// Validate that the textBoxes aren't empty - BEGIN
		if (Validations.eTextIsEmpty(email)){
			TextView tv = (TextView) findViewById(R.id.tView_email);
			tv.setTextColor(Color.RED);
			error = 1;
		}
		
		if (Validations.eTextIsEmpty(password)){
			TextView tv = (TextView) findViewById(R.id.tView_password);
			tv.setTextColor(Color.RED);
			error=1;
		}
		
		if (Validations.eTextIsEmpty(confPwd)){
			TextView tv = (TextView) findViewById(R.id.tView_confpwd);
			tv.setTextColor(Color.RED);
			error = 1;
		}
		
		if (Validations.eTextIsEmpty(ipCam)){
			TextView tv = (TextView) findViewById(R.id.tView_ipcam);
			tv.setTextColor(Color.RED);
			error=1;
		}
		
		if (Validations.eTextIsEmpty(camPwd)){
			TextView tv = (TextView) findViewById(R.id.tView_ippwd);
			tv.setTextColor(Color.RED);
			error = 1;
		}
		
		// Validate that the textBoxes aren't empty - END
		
		if (!password.getText().toString().equals(confPwd.getText().toString())){
			TextView tv = (TextView) findViewById(R.id.tView_password);
			tv.setTextColor(Color.RED);
			tv = (TextView) findViewById(R.id.tView_confpwd);
			tv.setTextColor(Color.RED);
			error = 1;
		}
		
		if (error == 0){
			// register account
			Authentication.registerAccount(email.getText().toString(), 
					password.getText().toString(),
					ipCam.getText().toString(),
					camPwd.getText().toString());
		}
		
	}

}
