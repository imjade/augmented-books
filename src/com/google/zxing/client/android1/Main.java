package com.google.zxing.client.android1;

import haseman.project.Geonames.GeonameClient;
import haseman.project.Geonames.GeonameObject;

import haseman.project.where4.CustomCameraView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.geonames.Toponym;

import ARKit.ARLayout;
import ARKit.TransparentPanel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	/** Called when the activity is first created. */
	CustomCameraView cv;
	public static volatile Context ctx;
	public static Location curLocation = null;
	public static FrameLayout rl;
	public static AbsoluteLayout al;
	static ARLayout ar;
	
	List<Thread> threadList = new ArrayList<Thread>();
	public List<Toponym> indoorPlaces = new ArrayList<Toponym>();
	Vector<GeonameObject> vc;
	boolean connected = false;
	
	String username;
	String password;
	String query;
	
	String startDewey;
	String nextDewey;
	String endDewey;
	
	Bundle bundle;
	
	public static ScrollView scrollView;
	public static LinearLayout linearLayout;
	
	Button buttonClicked;
	public static TextView textView;
	TransparentPanel transPanel;
	EditText editText;
	Button addCommentButton;
	String comments;

	public static Button buttonPlace1Button;
	public static Button buttonPlace2Button;
	public static Button buttonPlace3Button;
	public static Button buttonPlace4Button;
	public static Button buttonPlace5Button;
	/*public static Button buttonPlace6Button;
	public static Button buttonPlace7Button;
	public static Button buttonPlace8Button;
	public static Button buttonPlace9Button;
	public static Button buttonPlace10Button;*/
	
	public static LinearLayout linearLayoutPlace1;
	public static LinearLayout linearLayoutPlace2;
	public static LinearLayout linearLayoutPlace3;
	public static LinearLayout linearLayoutPlace4;
	public static LinearLayout linearLayoutPlace5;
	/*public static LinearLayout linearLayoutPlace6;
	public static LinearLayout linearLayoutPlace7;
	public static LinearLayout linearLayoutPlace8;
	public static LinearLayout linearLayoutPlace9;
	public static LinearLayout linearLayoutPlace10;*/
	
	Button generalInfo;
	Button viewComments;
	Button moreInformation;
	Button back;

	public static boolean next = false;
	
	public static List<LinearLayout> linearLayoutPlaces = new ArrayList<LinearLayout>();
	public static List<Button> buttonPlaces = new ArrayList<Button>();
	public static List<ImageButton> imageButtonPlaces = new ArrayList<ImageButton>();
	
	double [][] longitudeLatitude = new double [][] {{29.98668, 31.43871}, {29.98669, 31.43872}};
	
	//books would be saved their names, dewey and number of copies
	String [][] books = new String [][] {{"Networks1", "000.01ABC", "3"},
			{"Networks2", "000.01DEF", "2",},
			{"Networks3", "000.02GHI", "8"}, 
			{"Networks4", "000.12JKL", "5"},
			{"Networks5", "000.154MNO", "2"},
			{"Networks6", "000.23PQR", "3"},
			{"Networks7", "000.34STU", "2",},
			{"Networks8", "000.534VWX", "8"},
			{"Networks9", "000.534YZA", "5"},
			{"Networks10", "000.6BCD", "2"},
			{"Networks11", "000.701EFG", "3"},
			{"Einfuhrung in die Kryptographie", "003.54BUC", "3"},
			{"Computer Science an overview", "004BRO", "5",},
			{"Computer Systems", "004BRY", "4"},
			{"The Computer Engineering Handbook", "004OKL", "1"},
			{"An Invitation to Computer Science", "004SCH", "8"}};
	
	//removed action listener as GPS wouldn't be used
	/*private LocationListener gpsListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.e("Main", "***** Got first *****");
			curLocation = location;
			// ar.clearARViews();

			Thread thread = new Thread() {
				@Override
				public void run() {
					threadList.add(this);
					Log.e("Main", "thread added is: " + this.getId() + " "
							+ this.getName());
					Log.e("Main", "***** Got tanyyyy *****");
					GeonameClient fc = new GeonameClient();
					Vector<GeonameObject> vc = fc.getNearPlaces(curLocation,
							featureCodes);
					Log.e("Main", "***** CurLocation LA:"
							+ curLocation.getLatitude() + " LO:"
							+ curLocation.getLongitude() + " *****");
					drawPlaces(vc);
				}
			};
			runOnUiThread(thread);
			*
			 * runOnUiThread(new Runnable() { public void run() { Log.e("Main",
			 * "***** Got tanyyyy *****"); GeonameClient fc = new
			 * GeonameClient(); // Vector<GeonameObject> vc =
			 * fc.getNearPlaces(curLocation); Log.e("Main",
			 * "***** CurLocation LA:" + curLocation.getLatitude() + " LO:" +
			 * curLocation.getLongitude() + " *****"); drawPlaces(vc); } });
			 * new Thread() {
			 * @Override public void run() { Log.e("Main",
			 * "***** Got tanyyyy *****"); GeonameClient fc = new
			 * GeonameClient(); // Vector<GeonameObject> vc =
			 * fc.getNearPlaces(curLocation); Log.e("Main",
			 * "***** CurLocation LA:" + curLocation.getLatitude() + " LO:" +
			 * curLocation.getLongitude() + " *****"); drawPlaces(vc); }
			 * }.start();
			 *
			// drawPlaces(vc);
			// Log.e("MAIN","********* THREAD ID "+thread.getId());
			// thread.start();
			// LocationManager locMan = (LocationManager) ctx
			// .getSystemService(Context.LOCATION_SERVICE);
			// locMan.removeUpdates(this);
			// locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// (long) 1000, 1, this);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};*/

	public static void drawPlaces(Vector<GeonameObject> vc) {
		ar.clearARViews();
		if (vc != null && vc.size() > 0) {
			Enumeration e = vc.elements();
			while (e.hasMoreElements()) {
				GeonameObject fq = (GeonameObject) e.nextElement();
				Log.e("Main", "Got place:" + fq.name);
				if (fq.location != null)
					Log.i("Main", "Lat:" + fq.location.getLatitude() + ":"
							+ fq.location.getLongitude());
				Log.e("Main", "Azimuth: " + fq.azimuth);
				ar.addARView(fq);
			}
			ar.postInvalidate();
		}
	}

	private void addLoadingLayouts() {
		GeonameObject fs = new GeonameObject(this.getApplicationContext());
		fs.azimuth = 0;
		fs.inclination = 0;
		fs.name = "Loading1";
		ar.addARView(fs);
		
		fs = new GeonameObject(this.getApplicationContext());
		fs.azimuth = 45;
		fs.inclination = 0;
		fs.name = "Loading2";
		ar.addARView(fs);
		
		fs = new GeonameObject(this.getApplicationContext());
		fs.azimuth = 90;
		fs.inclination = 0;
		fs.name = "Loading3";
		ar.addARView(fs);
		
		fs = new GeonameObject(this.getApplicationContext());
		fs.azimuth = 135;
		fs.inclination = 0;
		fs.name = "Loading4";
		ar.addARView(fs);
		
		fs = new GeonameObject(this.getApplicationContext());
		fs.azimuth = 150;
		fs.inclination = 0;
		fs.name = "Loading5";
		ar.addARView(fs);
		
		ar.postInvalidate();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// try {
		super.onCreate(savedInstanceState);
		//firstlanch();
		ctx = this.getApplicationContext();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ar = new ARLayout(getApplicationContext());

		cv = new CustomCameraView(this.getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// cl = new CompassListener(this.getApplicationContext());
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		int width = d.getWidth();
		int height = d.getHeight();
		ar.screenHeight = height;
		ar.screenWidth = width;
		al = new AbsoluteLayout(getApplicationContext());
		rl = new FrameLayout(getApplicationContext());
		rl.addView(cv, width, height);
		rl.addView(al);
		addLoadingLayouts();
		
		bundle = getIntent().getExtras();
		username = bundle.getString("username");
		password = bundle.getString("password");
		
		Log.e("Main", "User, username\"" +username+"\", password \""+password+"\"");
		
		curLocation = new Location("currentLocation");
		
		placeLayout();
		extraInfoLayout();
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				handleScannedBarcode();
			}
		};
		runOnUiThread(thread);
		
		ar.debug = true;
		rl.addView(ar, width, height);
		
		setContentView(rl);
	}
	
	public void placeLayout(){
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				buttonClicked = buttonPlaces.get(v.getId());
				Log.e("Main", "******Clicked " + buttonClicked.getText());
				
				transPanel.setVisibility(View.GONE);
				
				generalInfo.setVisibility(View.VISIBLE);
				viewComments.setVisibility(View.VISIBLE);
				moreInformation.setVisibility(View.VISIBLE);
				back.setVisibility(View.VISIBLE);
			}
		};
		
		//AbsoluteLayout.LayoutParams paramsButton = new AbsoluteLayout.LayoutParams(15, 15, 0, 0);
		
		buttonPlace1Button = new Button(getApplicationContext());
		buttonPlace2Button = new Button(getApplicationContext());
		buttonPlace3Button = new Button(getApplicationContext());
		buttonPlace4Button = new Button(getApplicationContext());
		buttonPlace5Button = new Button(getApplicationContext());
		/*buttonPlace6Button = new Button(getApplicationContext());
		buttonPlace7Button = new Button(getApplicationContext());
		buttonPlace8Button = new Button(getApplicationContext());
		buttonPlace9Button = new Button(getApplicationContext());
		buttonPlace10Button = new Button(getApplicationContext());*/
		
		/*buttonPlace1Button.setText("Loading1");
		buttonPlace2Button.setText("Loading2");
		buttonPlace3Button.setText("Loading3");
		buttonPlace4Button.setText("Loading4");
		buttonPlace5Button.setText("Loading5");
		buttonPlace6Button.setText("Loading6");
		buttonPlace7Button.setText("Loading7");
		buttonPlace8Button.setText("Loading8");
		buttonPlace9Button.setText("Loading9");
		buttonPlace10Button.setText("Loading10");*/
				
		ImageButton imageButtonPlace1Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace2Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace3Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace4Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace5Button = new ImageButton(getApplicationContext());
		/*ImageButton imageButtonPlace6Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace7Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace8Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace9Button = new ImageButton(getApplicationContext());
		ImageButton imageButtonPlace10Button = new ImageButton(getApplicationContext());*/
		
		linearLayoutPlace1 = new LinearLayout(getApplicationContext());
		linearLayoutPlace2 = new LinearLayout(getApplicationContext());
		linearLayoutPlace3 = new LinearLayout(getApplicationContext());
		linearLayoutPlace4 = new LinearLayout(getApplicationContext());
		linearLayoutPlace5 = new LinearLayout(getApplicationContext());
		/*linearLayoutPlace6 = new LinearLayout(getApplicationContext());
		linearLayoutPlace7 = new LinearLayout(getApplicationContext());
		linearLayoutPlace8 = new LinearLayout(getApplicationContext());
		linearLayoutPlace9 = new LinearLayout(getApplicationContext());
		linearLayoutPlace10 = new LinearLayout(getApplicationContext());*/
		
		while (buttonPlaces.size() == 0) {
			Log.e("Main", "***** adding buttons *****");
			buttonPlaces.add(buttonPlace1Button);
			buttonPlaces.add(buttonPlace2Button);
			buttonPlaces.add(buttonPlace3Button);
			buttonPlaces.add(buttonPlace4Button);
			buttonPlaces.add(buttonPlace5Button);
			/*buttonPlaces.add(buttonPlace6Button);
			buttonPlaces.add(buttonPlace7Button);
			buttonPlaces.add(buttonPlace8Button);
			buttonPlaces.add(buttonPlace9Button);
			buttonPlaces.add(buttonPlace10Button);*/
			
			imageButtonPlaces.add(imageButtonPlace1Button);
			imageButtonPlaces.add(imageButtonPlace2Button);
			imageButtonPlaces.add(imageButtonPlace3Button);
			imageButtonPlaces.add(imageButtonPlace4Button);
			imageButtonPlaces.add(imageButtonPlace5Button);
			/*imageButtonPlaces.add(imageButtonPlace6Button);
			imageButtonPlaces.add(imageButtonPlace7Button);
			imageButtonPlaces.add(imageButtonPlace8Button);
			imageButtonPlaces.add(imageButtonPlace9Button);
			imageButtonPlaces.add(imageButtonPlace10Button);*/
			
			linearLayoutPlaces.add(linearLayoutPlace1);
			linearLayoutPlaces.add(linearLayoutPlace2);
			linearLayoutPlaces.add(linearLayoutPlace3);
			linearLayoutPlaces.add(linearLayoutPlace4);
			linearLayoutPlaces.add(linearLayoutPlace5);
			/*linearLayoutPlaces.add(linearLayoutPlace6);
			linearLayoutPlaces.add(linearLayoutPlace7);
			linearLayoutPlaces.add(linearLayoutPlace8);
			linearLayoutPlaces.add(linearLayoutPlace9);
			linearLayoutPlaces.add(linearLayoutPlace10);*/
		}
		
		for (int i = 0; i < buttonPlaces.size(); i++) {
			buttonPlaces.get(i).setId(i);
			buttonPlaces.get(i).setBackgroundColor(Color.TRANSPARENT);
			buttonPlaces.get(i).setTextColor(Color.WHITE);
			buttonPlaces.get(i).setTextSize(15); 
			buttonPlaces.get(i).setTypeface(null, Typeface.BOLD);
			buttonPlaces.get(i).setOnClickListener(listener);
			
			imageButtonPlaces.get(i).setImageResource(android.R.drawable.ic_dialog_info);
			imageButtonPlaces.get(i).setBackgroundColor(Color.TRANSPARENT);
			imageButtonPlaces.get(i).setId(i);
			imageButtonPlaces.get(i).setOnClickListener(listener);
			
			linearLayoutPlaces.get(i).setOrientation(LinearLayout.VERTICAL);
			linearLayoutPlaces.get(i).setVisibility(View.GONE);
			linearLayoutPlaces.get(i).addView(imageButtonPlaces.get(i));
			linearLayoutPlaces.get(i).addView(buttonPlaces.get(i));
		}
				
		al.addView(linearLayoutPlace1);
		al.addView(linearLayoutPlace2);
		al.addView(linearLayoutPlace3);
		al.addView(linearLayoutPlace4);
		al.addView(linearLayoutPlace5);
		/*al.addView(linearLayoutPlace6);
		al.addView(linearLayoutPlace7);
		al.addView(linearLayoutPlace8);
		al.addView(linearLayoutPlace9);
		al.addView(linearLayoutPlace10);*/
	}
	
	public void extraInfoLayout(){
		comments = "Dr. Fatma Meawad:\nThis book is a good reference for Java developers\n";
		comments += "\nProf Slim:\nI strongly recommend it\n";
		
		editText = new EditText(getApplicationContext());
		editText.setVisibility(View.GONE);

		addCommentButton = new Button(getApplicationContext());
		addCommentButton.setVisibility(View.GONE);
		addCommentButton.setText("Add Comment");
		addCommentButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				comments+="\n"+username+":\n"+editText.getText()+"\n";
				textView.setText(comments);
				editText.setText("");
			}
		});

		textView = new TextView(getApplicationContext());
		
		linearLayout = new LinearLayout(getApplicationContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.addView(textView);
		linearLayout.addView(editText);
		linearLayout.addView(addCommentButton);

		scrollView = new ScrollView(getApplicationContext());
		scrollView.setBackgroundColor(Color.TRANSPARENT);
		scrollView.layout(0, 0, 300, 180);
		scrollView.setHorizontalScrollBarEnabled(true);
		scrollView.addView(linearLayout);
		
		AbsoluteLayout.LayoutParams paramsScroll = new AbsoluteLayout.LayoutParams(
				300, 180, 100, 70);
		scrollView.setLayoutParams(paramsScroll);
		
		transPanel = new TransparentPanel(getApplicationContext());
		transPanel.setVisibility(View.GONE);
		transPanel.addView(scrollView);
		
		AbsoluteLayout.LayoutParams paramsLayout = new AbsoluteLayout.LayoutParams(
				300, 180, 100, 70);
		
		transPanel.setLayoutParams(paramsLayout);
		
		al.addView(transPanel);
		
		OnClickListener moreInfobuttonsListener = new Button.OnClickListener() {
			public void onClick(View v) {
				Button moreInfobuttonClicked = (Button) v;
				Log.e("Main", "******Clicked "+ moreInfobuttonClicked.getText());

				editText.setVisibility(View.GONE);
				editText.setText("");
				addCommentButton.setVisibility(View.GONE);
				
				if (moreInfobuttonClicked == generalInfo) {
					textView.setText("GENERAL INFO :) " + buttonClicked.getText());
				}

				if (moreInfobuttonClicked == viewComments) {
					textView.setText(comments);
					editText.setVisibility(View.VISIBLE);
					addCommentButton.setVisibility(View.VISIBLE);
				}

				if (moreInfobuttonClicked == moreInformation) {
					textView.setText("MORE INFORMATION :)"+ buttonClicked.getText());
				}

				if (moreInfobuttonClicked == back) {
					generalInfo.setVisibility(View.GONE);
					viewComments.setVisibility(View.GONE);
					moreInformation.setVisibility(View.GONE);
					back.setVisibility(View.GONE);
					transPanel.setVisibility(View.GONE);
					return;
				}
				transPanel.setVisibility(View.VISIBLE);
			}
		};
		
		AbsoluteLayout.LayoutParams paramsGeneralInfo = new AbsoluteLayout.LayoutParams(
				100, 50, 10, 265);
		generalInfo = new Button(getApplicationContext());
		generalInfo.setVisibility(View.GONE);
		generalInfo.setBackgroundColor(Color.GRAY);
		generalInfo.setText("General Informtion");
		generalInfo.setLayoutParams(paramsGeneralInfo);
		generalInfo.setOnClickListener(moreInfobuttonsListener);

		AbsoluteLayout.LayoutParams paramsComments = new AbsoluteLayout.LayoutParams(
				100, 50, 120, 265);
		viewComments = new Button(getApplicationContext());
		viewComments.setVisibility(View.GONE);
		viewComments.setBackgroundColor(Color.GRAY);
		viewComments.setText("Comments");
		viewComments.setLayoutParams(paramsComments);
		viewComments.setOnClickListener(moreInfobuttonsListener);

		AbsoluteLayout.LayoutParams paramsMoreInfo = new AbsoluteLayout.LayoutParams(
				100, 50, 230, 265);
		moreInformation = new Button(getApplicationContext());
		moreInformation.setVisibility(View.GONE);
		moreInformation.setBackgroundColor(Color.GRAY);
		moreInformation.setText("More Information");
		moreInformation.setLayoutParams(paramsMoreInfo);
		moreInformation.setOnClickListener(moreInfobuttonsListener);

		AbsoluteLayout.LayoutParams paramsBack = new AbsoluteLayout.LayoutParams(
				100, 50, 340, 265);
		back = new Button(getApplicationContext());
		back.setVisibility(View.GONE);
		back.setBackgroundColor(Color.GRAY);
		back.setText("Back");
		back.setLayoutParams(paramsBack);
		back.setOnClickListener(moreInfobuttonsListener);
				
		al.addView(generalInfo);
		al.addView(viewComments);
		al.addView(moreInformation);
		al.addView(back);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.e("Main", "***** ONDESTROY after super *****");
		cv.closeCamera();
		ar.close();
		Log.e("Main", "thread List has: " + threadList.size());
		try {

			for (int i = 0; i < threadList.size(); i++) {
				Log.e("Main", "in main of destroying threads "+ threadList.get(i).getId() + " "+ threadList.get(i).getName());
				threadList.get(i).stop();
			}
			threadList.clear();
			Log.e("Main", "in main of destroying threads");
		} catch (Exception e) {
			Log.e("Main", "in catch of destroying threads");
		}
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		try {
			this.finalize();
			Log.e("Main", "in finalize");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onStop() {
		super.onStop();
		Log.e("Main", "in stop application");

		try {
			this.finalize();
			Log.e("Main", "in stop application");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent event) { if (keyCode ==
	 * KeyEvent.KEYCODE_BACK) { Log.e("Main","back button clicked");
	 * cv.closeCamera(); ar.close(); finish(); /* if (source ==
	 * Source.NATIVE_APP_INTENT) { setResult(RESULT_CANCELED); finish(); return
	 * true; } else if ((source == Source.NONE || source == Source.ZXING_LINK)
	 * && lastResult != null) { resetStatusView(); if (handler != null) {
	 * handler.sendEmptyMessage(R.id.restart_preview); } return true; } } else
	 * if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode ==
	 * KeyEvent.KEYCODE_CAMERA) { // Handle these events so they don't launch
	 * the Camera app return true; } return super.onKeyDown(keyCode, event); }
	 */

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * ((keyCode == KeyEvent.KEYCODE_BACK)) { Log.e(this.getClass().getName(),
	 * "back button pressed"); // cv.closeCamera(); ar.close(); finish(); }
	 * return super.onKeyDown(keyCode, event); }
	 */

	
	/**
	 * starts zxing app and sends the users filteration
	 */
	private void StartZebraCrossing() {
		Intent i = new Intent(getApplicationContext(), CaptureActivity.class);
		i.putExtra("username", username);
		i.putExtra("password", password);
		i.putExtra("BarCodeContents", bundle.getString("BarCodeContents"));
		setResult(RESULT_OK, i);
		startActivity(i);
		Log.e("Main, StartZebraCrossing()", "will start ZXing activity");
		this.onDestroy();
		this.finish();
	}

	private void handleScannedBarcode() {
		String scanResult = bundle.getString("BarCodeContents");
		Log.e("Main, handleScannedBarcode()", "Barcode scanned :\""+scanResult+"\"");
		if (scanResult != null) {
			try {
				String[] tokens = scanResult.split("[?]");
				String[] LatLng = tokens[0].substring(4).split(",");
				String query = tokens[1].substring(2);
				Log.e("Main, handleScannedBarcode()", "LatLng: \""+LatLng[0]+"\" , \""+LatLng[1]+"\" and query: \""+ query+"\"");
				
				//double lat = Double.valueOf(scanResult.substring(4, 12));
				//double lng = Double.valueOf(scanResult.substring(13, 21));
				//String query = scanResult.substring(24);
				
				double lat = Double.valueOf(LatLng[0]);
				double lng = Double.valueOf(LatLng[1]);
				
				curLocation.setLatitude(lat);
				curLocation.setLongitude(lng);
				
				/* Scanned barcode is at the library entrance
				 * get from Geonames the general topics
				 * */
				if (query.equals("G")) {
					//placeLayoutTopics();
					Log.e("Main, handleScannedBarcode()", "Will contact GeoNames");
					getNearbyPlaces();
					if(!connected)
						getNearbyPlaces();
					if(!connected){
						Log.e("Main, handleScannedBarcode()", "Connection error while contacting GeoNames");
						Toast toast = Toast.makeText(this.getApplicationContext(), "Connection Error", 2);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				}
				else{
					String[] deweyCodes = query.split(",");
					
					startDewey = deweyCodes[0];
					endDewey = deweyCodes[1];
					//Double dewey = Double.valueOf(query);
					nextDewey = startDewey;
					Log.e("Main, handleScannedBarcode()", "BOOKS DEWEY start "+nextDewey);
					getNearbyBooks();
				}
			}
			catch (Exception e) {
				Log.e("Main, handleScannedBarcode()", "Error barcode");
				Toast toast = Toast.makeText(this.getApplicationContext(), "Wrong barcode, Please scan another one ", 6);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	}
	
	public void getNearbyPlaces(){
		Thread thread = new Thread() {
			@Override
			public void run() {
				threadList.add(this);
				Log.e("Main", "thread added is: " + this.getId() + " "
						+ this.getName());
				Log.e("Main", "***** Got tanyyyy *****");
				
				for (int k = 0; k < buttonPlaces.size(); k++) 
					imageButtonPlaces.get(k).setImageResource(R.drawable.library_books_icon);
				
				GeonameClient fc = new GeonameClient();
				Vector<GeonameObject> vc = fc.getNearPlaces(curLocation,new String[] { "LIBR" });
				Log.e("Main", "***** CurLocation LA:"
						+ curLocation.getLatitude() + " LO:"
						+ curLocation.getLongitude() + " *****");
				if(vc.size()>0){
					connected = true;
					for (int i = 0; i < buttonPlaces.size(); i++) 
						imageButtonPlaces.get(i).setImageResource(R.drawable.menu_iconbook_stand);
					drawPlaces(vc);
				}	
			}
		};
		runOnUiThread(thread);
	}
	
	public void getNearbyBooks(){//final Double dewey
		Thread thread = new Thread() {
			@Override
			public void run() {
				threadList.add(this);
				Log.e("Main", "thread added is: " + this.getId() + " "
						+ this.getName());
				Log.e("Main", "***** Got tanyyyy *****");
				int i=0;
				
				while(i<books.length){
					//if(dewey.compareTo(Double.valueOf(books[i][1]))==0)
					if(nextDewey.equals(books[i][1]))
						break;
					i++;
				}
				
				Vector<GeonameObject> v = new Vector<GeonameObject>();
				GeonameObject nearPlace;
				
				for (int k = 0; k < buttonPlaces.size(); k++) 
					imageButtonPlaces.get(k).setImageResource(R.drawable.menu_iconbook_stand);
				
				Double nextLong = 0.00001;
				boolean end = false;
				//for(int j=i; j<Math.min(Main.buttonPlaces.size(), books.length);j++){
				for(int j=0; j<Main.buttonPlaces.size()&&!end; j++){
					if(books[i][1].equals(endDewey)){
						end = true;
						Log.e("Main, getNearbyBooks()", "Shelf ended");
					}
					//buttonPlaces.get(j).setText(books[j][0]);
					buttonPlaces.get(j).setText(books[i][0]);
					nearPlace = new GeonameObject(Main.ctx);
					nearPlace.name = books[i][0];
					nearPlace.inclination = j*2;
				
					nearPlace.location = new Location(books[i][0]);
					nearPlace.location.setLatitude(curLocation.getLatitude());
					nearPlace.location.setLongitude((Double.valueOf(curLocation.getLongitude())) - nextLong);
					//nearPlace.location.setLongitude((Double.valueOf(curLocation.getLongitude())) + nextLong);
					v.add(nearPlace);
					Log.e("Main, getNearbyBooks()", "location "+nearPlace.name);
					Log.e("Main, getNearbyBooks()", "lat "+nearPlace.location.getLatitude()+" long "+nearPlace.location.getLongitude());
					
					nextLong += 5 * Integer.parseInt(books[i][2]);
					//nextLong += 0.00001 * Integer.parseInt(books[j][2]);
					i++;
				}
				if(!end)
					nextDewey = books[i][1];
				drawPlaces(v);
			}
		};
		runOnUiThread(thread);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Sign Out").setIcon(R.drawable.menu_signout);
		menu.add(0, 1, 0, "Refresh").setIcon(R.drawable.menu_refresh);
		menu.add(0, 2, 0, "Scan Barcode").setIcon(R.drawable.menu_icon);
		menu.add(0, 3, 0, "Help").setIcon(android.R.drawable.ic_menu_help);
		menu.add(0, 4, 0, "Next").setIcon(R.drawable.library_icon);
		menu.add(0, 5, 0, "Exit").setIcon(android.R.drawable.ic_lock_power_off);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			//Sign Out	
			case 0: {
				menuSignout();
				break;
			}
			//Refresh
			case 1: {
				handleScannedBarcode();
				break;
			}
			//Scan barcode
			case 2: {			
				StartZebraCrossing();	
				break;
			}
			//Help
			case 3: {
				break;
			}
			//Next
			case 4: {
				if(nextDewey.equals(""))
					getNearbyPlaces();
				else
					getNearbyBooks();
				break;
			}
			//Exit
			case 5: {
				onDestroy();
				break;
			}
		}
			
		return super.onOptionsItemSelected(item);
	}

	private void menuSignout() {
		Intent i = new Intent(ctx, layout.class);
		setResult(RESULT_OK, i);
		startActivity(i);
		this.onDestroy();
	}
}