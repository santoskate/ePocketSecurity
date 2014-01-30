package com.example.droidcamsecurity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import businessLayer.util.JSONParser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
		return true;
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
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
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
		
		// JSON Node names
	    private static final String TAG_SUCCESS = "success";
	    private static final String TAG_MESSAGE = "message";

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
				accountImageView = (ImageView) rootView
						.findViewById(R.id.streamingImage);
					
				// Create an object for subclass of AsyncTask to get de image
		        GetImageTask task = new GetImageTask();
		        
		        // Execute the task
		        task.execute(new String[] { getText(R.string.url).toString()+"p/image.jpg" });
				
			} else if (i == 2){ 	// content for the second fragment
				
			} else if (i == 3){		// content for the third fragment
				TextView tx = (TextView) rootView.findViewById(R.id.section_label);
				tx.setText(R.string.content_about);
			}
			
			
			return rootView;
		}
		
		private class GetImageTask extends AsyncTask<String, Void, Bitmap> {
			private String url;
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
	            	map = loadImageFromUrl(url);
	            }
	            	
	            return map;
	        }
	 
	        // Sets the Bitmap returned by doInBackground
	        @Override
	        protected void onPostExecute(Bitmap result) {
	        	accountImageView.setImageBitmap(result);
	        	
	        	GetImageTask task = new GetImageTask();
	            
	            task.execute(url);
	        }
	        
	        public Bitmap loadImageFromUrl(String url) {
	            URL m;
	            InputStream i = null;
	            BufferedInputStream bis = null;
	            ByteArrayOutputStream out =null;
	            try {
	                m = new URL(url);
	                i = (InputStream) m.getContent();
	                bis = new BufferedInputStream(i,1024 * 8);
	                out = new ByteArrayOutputStream();
	                int len=0;
	                byte[] buffer = new byte[1024];
	                while((len = bis.read(buffer)) != -1){
	                    out.write(buffer, 0, len);
	                }
	                out.close();
	                bis.close();
	            } catch (MalformedURLException e1) {
	                e1.printStackTrace();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            byte[] data = out.toByteArray();
	            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	            //Drawable d = Drawable.createFromStream(i, "src");
	            return bitmap;
	        }
	    }
	}

}
