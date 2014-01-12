package businessLayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import businessLayer.util.JSONParser;
import android.os.StrictMode;
import android.util.Log;

public class Authentication {
 
    static JSONParser jsonParser = new JSONParser();
    String email;
    String pass;
    String ipCam;
    String camPwd;
    
    // URL to create new product
    private static String url_create_user = "http://www.catsart.net/WebservicesPHP/create_account.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    
	public static void registerAccount(String email, String password,
			String ipCam, String camPwd) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("cam_ip", ipCam));
        params.add(new BasicNameValuePair("cam_password", camPwd));

        // getting JSON Object
        // Note that create product URL accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                "POST", params);

        // check log cat fro response
        Log.d("Create Response", json.toString());

        // check for success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                /*// successfully created product
                Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                startActivity(i);

                // closing this screen
                finish();*/
            } else {
                // failed to create product
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return null;
    
	}

}
