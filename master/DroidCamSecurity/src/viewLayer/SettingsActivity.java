package viewLayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import viewLayer.SignInActivity.LogInValidation;
import businessLayer.AccountManagement;
import businessLayer.Authentication;
import businessLayer.util.JSONParser;

import com.example.droidcamsecurity.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	// Progress Dialog
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	// JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	EditText eActualPwd;
	EditText eNewPwd;
	EditText eNewConfPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void updateAccount(View view){
		eActualPwd = (EditText) findViewById(R.id.etActualPwd);
		eNewPwd = (EditText) findViewById(R.id.etNewPwd);
		eNewConfPwd = (EditText) findViewById(R.id.etNewConfPwd);
		
		if (AccountManagement.updateAccountDataValidateForm(SettingsActivity.this) == 0){
			
			// Execute async task to update data
			new UpdatingData().execute();
		}
		
	}
	
	/**
	 * Background Async Task to Update account data
	 * */
	class UpdatingData extends AsyncTask<String, String, String> {
		
		public Boolean flag = true;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SettingsActivity.this);
			pDialog.setMessage(getText(R.string.msg_progress).toString());
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("email", getIntent().getExtras().getString("email")));
	        params.add(new BasicNameValuePair("password", eActualPwd.getText().toString()));
	        params.add(new BasicNameValuePair("new_password", eNewPwd.getText().toString()));

	        // getting JSON Object from the login WeService
	        JSONObject json = jsonParser.makeHttpRequest(getText(R.string.url_update_account).toString(),
	                "POST", params);

	        // logging
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt(TAG_SUCCESS);
	            
	            if (success == 1) {
	                // successfully update
	                finish();
	            	
	            } else {
	                // Login failed
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
			
			Builder alert = new AlertDialog.Builder(SettingsActivity.this);
			alert.setTitle(getString(R.string.title_activity_settings));
			
			// If there's an error, show the error popup
			if(!flag){
				alert.setMessage(getString(R.string.update_account_unsuccess));
				alert.setPositiveButton("OK",null);
				alert.show(); 
			}
		}
		
	}
}
