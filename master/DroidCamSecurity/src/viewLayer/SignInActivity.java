package viewLayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import businessLayer.Authentication;
import businessLayer.util.JSONParser;

import com.example.droidcamsecurity.ItemListActivity;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class SignInActivity extends Activity {
	
	// Progress Dialog
	private ProgressDialog pDialog;
		
	private EditText email;
	private EditText password;
	
	JSONParser jsonParser = new JSONParser();
	
	// JSON Node names
    private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
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
		getMenuInflater().inflate(R.menu.sign_in, menu);
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
	
	public void logIn(View view) {
		email = (EditText) findViewById(R.id.eTextEmail);
		password = (EditText) findViewById(R.id.eTextPwd);
		
		if (Authentication.signInValidateForm(SignInActivity.this) == 0){
			new LogInValidation().execute();
		}

	}
	
	/**
	 * Background Async Task to Create new product
	 * */
	class LogInValidation extends AsyncTask<String, String, String> {
		
		public Boolean flag = true;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignInActivity.this);
			pDialog.setMessage(getText(R.string.msg_progress).toString());
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		protected String doInBackground(String... args) {
			// Building Parameters
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("email", email.getText().toString()));
	        params.add(new BasicNameValuePair("password", password.getText().toString()));

	        // getting JSON Object from the login WeService
	        JSONObject json = jsonParser.makeHttpRequest(getText(R.string.url_login).toString(),
	                "POST", params);

	        // logging
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt(TAG_SUCCESS);
	            
	            if (success == 1) {
	                // successfully login
	                Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
	                startActivity(i);
	            	
	            	// To finish Parent and go on
	            	setResult(2);
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
			
			Builder alert = new AlertDialog.Builder(SignInActivity.this);
			alert.setTitle(getString(R.string.title_activity_sign_in));
			
			if(!flag){
				alert.setMessage(getString(R.string.sign_in_unsuccess));
				alert.setPositiveButton("OK",null);
				alert.show(); 
			}
		}
		
	}

}
