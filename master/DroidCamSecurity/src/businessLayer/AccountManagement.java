package businessLayer;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.example.droidcamsecurity.R;

import viewLayer.SettingsActivity;
import viewLayer.util.Validations;

public class AccountManagement {

	
	public static int updateAccountDataValidateForm(
			SettingsActivity settingsActivity) {
		
		TextView tvActualPwd = (TextView) settingsActivity.findViewById(R.id.tvActualPwd);
    	TextView tvNewPwd = (TextView) settingsActivity.findViewById(R.id.tvNewPwd);
    	TextView tvNewConfPwd = (TextView) settingsActivity.findViewById(R.id.tvNewConfPwd);
    	
    	EditText etActualPwd = (EditText) settingsActivity.findViewById(R.id.etActualPwd);
    	EditText etNewPwd = (EditText) settingsActivity.findViewById(R.id.etNewPwd);
    	EditText etNewConfPwd = (EditText) settingsActivity.findViewById(R.id.etNewConfPwd);
    	
    	tvActualPwd.setTextColor(Color.BLACK);
    	tvNewPwd.setTextColor(Color.BLACK);
    	tvNewConfPwd.setTextColor(Color.BLACK);
    	
    	int erro = 0;
    	
    	// Validate that the textBoxes aren't empty - BEGIN
		if (Validations.eTextIsEmpty(etActualPwd)){
			tvActualPwd.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(etNewPwd)){
			tvNewPwd.setTextColor(Color.RED);
			erro = 1;
		}
		
		if (Validations.eTextIsEmpty(etNewConfPwd)){
			tvNewConfPwd.setTextColor(Color.RED);
			erro = 1;
		}
		// Validate that the textBoxes aren't empty - END
		
		if (!etNewPwd.getText().toString().equals(etNewConfPwd.getText().toString())){
			tvNewPwd.setTextColor(Color.RED);
			tvNewConfPwd.setTextColor(Color.RED);
			erro = 1;
		}
    	
		
		return erro;
	}
}
