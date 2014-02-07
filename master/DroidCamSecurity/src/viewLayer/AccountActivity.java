package viewLayer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.droidcamsecurity.R;

import businessLayer.util.JSONParser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	static String e;
	static String ip;
	static String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		Bundle b = getIntent().getExtras();
    	e = b.getString("email");
    	ip = b.getString("camIp");
    	name = b.getString("name");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            Intent settings = new Intent(AccountActivity.this, SettingsActivity.class);
	            settings.putExtra("email", e);
	            startActivity(settings);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a AccountSectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new AccountSectionFragment();
			Bundle args = new Bundle();
			args.putInt(AccountSectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putString("about", getText(R.string.content_about).toString());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A Account fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class AccountSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		ImageView accountImageView;
		JSONParser jsonParser = new JSONParser();

		public AccountSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_account_dummy,
					container, false);
			Bundle b = getArguments();
			int i = b.getInt(AccountSectionFragment.ARG_SECTION_NUMBER);
			
			if (i == 1){ 		// content for the first fragment
				TextView tx = (TextView) rootView.findViewById(R.id.section_label);
				tx.setText("Welcome " + name);
				
				accountImageView = (ImageView) rootView
						.findViewById(R.id.streamingImage);
					
				// Create an object for subclass of AsyncTask to get de image
		        GetImageTask task = new GetImageTask();
		        
		        // Execute the task
		        task.execute(new String[] { getText(R.string.url).toString()+ e + "/image.jpg" });
				
			} else if (i == 2){ 	// content for the second fragment
				TextView tx = (TextView) rootView.findViewById(R.id.section_label);
				tx.setText(R.string.content_about);
			}
			
			
			return rootView;
		}
		
		private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
			private String url;
			private static final int MAX_RETRIES = 3;
	        @Override
	        protected Bitmap doInBackground(String... urls) {
	        	url = urls[0];
	        	
	        	// Building Parameters
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("email", e));
		        params.add(new BasicNameValuePair("cam_ip", ip));

		        // getting JSON Object from the login WeService
		        JSONObject json = jsonParser.makeHttpRequest(getText(R.string.url_get_image).toString(),
		                "POST", params);
		        
		        // logging
		        Log.d("Create Response", json.toString());
		        
	            Bitmap map = null;
	            while (map == null){
	            	try {
						if (json.getInt("success") == 0)
							break;
						
						map = loadImageFromUrl(url);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	            	
	            return map;
	        }
	 
	        // Sets the Bitmap returned by doInBackground
	        @Override
	        protected void onPostExecute(Bitmap result) {
	        	if(isAdded()){
	        		if (result != null){
	        			accountImageView.setImageBitmap(result);
	        			accountImageView.invalidate();
	        		}
		        	GetImageTask task = new GetImageTask();
		            
		            task.execute(url);
		        }
	        }
	        
	        public Bitmap loadImageFromUrl(String url) {
	            URL m;
	            InputStream i = null;
	            BufferedInputStream bis = null;
	            ByteArrayOutputStream out =null;
	            try {
	            	System.out.println("1");
	                m = new URL(url);
	                System.out.println("2 - " + m.toString());
	                i = (InputStream) m.getContent();
	                System.out.println("3");
	                bis = new BufferedInputStream(i,1024 * 8);
	                System.out.println("4");
	                out = new ByteArrayOutputStream();
	                System.out.println("5");
	                int len=0;
	                System.out.println("6");
	                byte[] buffer = new byte[1024];
	                System.out.println("7");
	                while((len = bis.read(buffer)) != -1){
	                    out.write(buffer, 0, len);
	                }
	                System.out.println("8");
	                out.close();
	                System.out.println("9");
	                bis.close();
	                System.out.println("11");
	            } catch (MalformedURLException e1) {
	                e1.printStackTrace();
	            } catch (EOFException e2){
	            	e2.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            } 
	            System.out.println("12");
	            byte[] data = out.toByteArray();
	            System.out.println("13");
	            BitmapFactory.Options opts = new BitmapFactory.Options();
	            System.out.println("13.1");
	            opts.inSampleSize = 4;
	            System.out.println("13.2");
	            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	            System.out.println("14");
	            
	            return bitmap;
	        }
	    }
	}

}
