package viewLayer.util;

import android.view.View;
import android.widget.EditText;


public class Validations {
	
	public static boolean eTextIsEmpty(View view){
		EditText etext = (EditText) view;
		
		if (etext.length() > 0)
			return false;
		else
			return true;
	}

}
