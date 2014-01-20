package businessLayer;

import viewLayer.util.Validations;
import android.app.Activity;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.example.droidcamsecurity.R;

/**
 * 
 * @author Kate
 *
 * Authentication validations
 *
 */
public class Authentication extends Activity {
 
    public static int signUpValidateForm(Activity actSignUp
    		//EditText email, 
    		//EditText password, EditText confPwd, EditText ipCam, EditText camPwd
    		){
    	
    	TextView tvEmail = (TextView) actSignUp.findViewById(R.id.tView_email);
    	TextView tvPwd = (TextView) actSignUp.findViewById(R.id.tView_password);
    	TextView tvConfPwd = (TextView) actSignUp.findViewById(R.id.tView_confpwd);
    	TextView tvIpCam = (TextView) actSignUp.findViewById(R.id.tView_ipcam);
    	TextView tvIpPwd = (TextView) actSignUp.findViewById(R.id.tView_ippwd);
    	
    	EditText email = (EditText) actSignUp.findViewById(R.id.eText_email);
    	EditText password = (EditText) actSignUp.findViewById(R.id.eText_password);
    	EditText confPwd = (EditText) actSignUp.findViewById(R.id.eText_confpwd);
    	EditText ipCam = (EditText) actSignUp.findViewById(R.id.eText_ipcam);
    	EditText camPwd = (EditText) actSignUp.findViewById(R.id.eText_ippwd);
    	
    	tvEmail.setTextColor(Color.BLACK);
    	tvPwd.setTextColor(Color.BLACK);
    	tvConfPwd.setTextColor(Color.BLACK);
    	tvIpCam.setTextColor(Color.BLACK);
    	tvIpPwd.setTextColor(Color.BLACK);
    	
    	int erro = 0;
    	
		// Validate that the textBoxes aren't empty - BEGIN
		if (Validations.eTextIsEmpty(email)){
			tvEmail.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(password)){
			tvPwd.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(confPwd)){
			tvConfPwd.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(ipCam)){
			tvIpCam.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(camPwd)){
			tvIpPwd.setTextColor(Color.RED);
			erro = 1;
		}
		
		// Validate that the textBoxes aren't empty - END
		
		if (!password.getText().toString().equals(confPwd.getText().toString())){
			tvPwd.setTextColor(Color.RED);
			tvConfPwd.setTextColor(Color.RED);
			erro = 1;
		}
    	
    	return erro;
    }

}
