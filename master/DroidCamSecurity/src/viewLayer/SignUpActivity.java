package viewLayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import businessLayer.Authentication;
import businessLayer.util.JSONParser;

import com.example.droidcamsecurity.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class SignUpActivity extends Activity {
	
	// Progress Dialog
	private ProgressDialog pDialog;
	
	private EditText name;
	private EditText email;
	private EditText password;
	private EditText ipCam;
	private EditText camPwd;
	
	JSONParser jsonParser = new JSONParser();
	
	// JSON Node names
    private static final String TAG_SUCCESS = "success";

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
		name = (EditText) findViewById(R.id.eText_name);
		email = (EditText) findViewById(R.id.eText_email);
		password = (EditText) findViewById(R.id.eText_password);
		ipCam = (EditText) findViewById(R.id.eText_ipcam);
		camPwd = (EditText) findViewById(R.id.eText_ippwd);
		
		if (Authentication.signUpValidateForm(SignUpActivity.this) == 0){
			
			// Execute async task for signup
			new CreateNewAccount().execute();
		}
		
	}

	
	/**
	 * Background Async Task to Create new Account - SignUp
	 * */
	class CreateNewAccount extends AsyncTask<String, String, String> {
		
		public Boolean flag;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignUpActivity.this);
			pDialog.setMessage(getText(R.string.msg_progress));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		/**
		 * Creating User account
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("name", name.getText().toString()));
	        params.add(new BasicNameValuePair("email", email.getText().toString()));
	        params.add(new BasicNameValuePair("password", password.getText().toString()));
	        params.add(new BasicNameValuePair("cam_ip", ipCam.getText().toString()));
	        params.add(new BasicNameValuePair("cam_password", camPwd.getText().toString()));

	        // getting JSON Object from the create_account WebService
	        JSONObject json = jsonParser.makeHttpRequest(getText(R.string.url_create_user).toString(),
	                "POST", params);

	        // logging
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt(TAG_SUCCESS);
	            
	            if (success == 1) {
	            	flag = true;
	            } else {
	            	flag = false;
	            }
	            
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 *
 		**/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			
			Builder alert = new AlertDialog.Builder(SignUpActivity.this);
			alert.setTitle(getString(R.string.title_activity_sign_up));
			
			if(flag){
				alert.setMessage(getResources().getString(R.string.sign_up_success));
				alert.setPositiveButton("OK",null);
				alert.show();
                
				finish();
                
			} else{
				alert.setMessage(getString(R.string.sign_up_unsuccess));
				alert.setPositiveButton("OK",null);
				alert.show(); 
			}
				
		}
		
	}
}
