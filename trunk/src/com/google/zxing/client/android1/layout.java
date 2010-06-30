package com.google.zxing.client.android1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

public class layout extends Activity {
	String userName;
	String passwd;
	EditText username, password, newUsername, newPassword;
	TableLayout register;
	TableLayout signin;
	ScrollView home;
	ArrayList<String> users;
	ArrayList<String> passwords;
	ArrayList<String> names;
	Context ctx;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ctx = this.getApplicationContext();
		
		username = (EditText) findViewById(R.id.editName);
		password = (EditText) findViewById(R.id.editPasswd);
		newUsername = (EditText) findViewById(R.id.neweditName);
		newPassword = (EditText) findViewById(R.id.neweditPasswd);
		users = new ArrayList<String>();
		passwords = new ArrayList<String>();
		names = new ArrayList<String>();
		
		final Button signin = (Button) findViewById(R.id.buttonSignIn);
		Button visitor = (Button) findViewById(R.id.visitor);
		
		OnClickListener signInListener = new Button.OnClickListener() {
			public void onClick(View v) {
				userName = username.getText().toString();
				passwd = password.getText().toString();
				
				Log.e("layout", "Signing in, username\"" +userName+"\", password \""+passwd+"\"");
				
				Button buttonClicked = (Button) v;
				boolean logIn = false;
				
				if (buttonClicked == signin){
					logIn = chkUser(userName, passwd);
				}
				else{
					//if(! (userName.equals("")))
						logIn = true;
				}
				
				if (logIn) {
					Log.e("layout", "starting zebraCrossing");
					signinPageGone();
					Intent i = new Intent(ctx, CaptureActivity.class);
					i.putExtra("username", userName);
					i.putExtra("password", passwd);
					i.putExtra("BarCodeContents", "");
			    	setResult(RESULT_OK, i);
			    	startActivity(i);
					finish();
				}
				else{
					Log.e("layout", "Wrong user info");
					Toast toast;
					toast = Toast.makeText(ctx, "Please add missing data", 2);
					if((userName.equals("")) || (passwd.equals(""))){
						 toast.setText("Please add missing data");
					}else{
						if (buttonClicked == signin){
							toast.setText("Please enter a valid username and password");
						}else{
							toast.setText("Please add your name");
						}
					}
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();					
					
					/*AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
					if((userName.equals("")) || (passwd.equals(""))){
						alertDialog.setTitle("Missing Data");
						alertDialog.setMessage("Please add missing data");
					}
					else{
						if (buttonClicked == signin){
							alertDialog.setTitle("Wrong User");
							alertDialog.setMessage("Please enter a valid username and password");
						}else{
							alertDialog.setTitle("Username");
							alertDialog.setMessage("Please add your name");
						}	
					}
					alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
					alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			             public void onClick(DialogInterface dialog, int which) {
			                     return;
			             }
			        });
					alertDialog.show();*/
				}
			}
		};
		
		signin.setOnClickListener(signInListener);
		visitor.setOnClickListener(signInListener);
		
		Button signup = (Button) findViewById(R.id.buttonSignUp);
		signup.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				showSignupPage();
				signinPageGone();
			}
		});

		Button registering = (Button) findViewById(R.id.buttonRegister);
		registering.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				users.add(newUsername.getText().toString());
				passwords.add(newPassword.getText().toString());
				username.setText(newUsername.getText().toString());
				password.setText(newPassword.getText().toString());
				registerPageGone();
				showSigninPage();
			}
		});
	}

	@Override
	public void onStop() {
		finish();
		super.onStop();
	}

	public boolean chkUser(String username, String password) {
		users.add("a");
		passwords.add("a");
		for (int i = 0; i < users.size();) {
			if ((username.equals(users.get(i)))
					&& (password.equals(passwords.get(i)))) {
				Log.e("layout", "Correct user");
				return true;
			} else {
				Log.e("layout", "Wrong user");
				return false;
			}
		}
		return false;
	}

	public void showSigninPage() {
		signin = (TableLayout) findViewById(R.id.signinLayout);
		signin.setVisibility(View.VISIBLE);
	}

	public void signinPageGone() {
		signin = (TableLayout) findViewById(R.id.signinLayout);
		signin.setVisibility(View.GONE);
	}

	public void registerPageGone() {
		register = (TableLayout) findViewById(R.id.registerLayout);
		register.setVisibility(View.GONE);
	}
	
	public void showSignupPage() {
		register = (TableLayout) findViewById(R.id.registerLayout);
		register.setVisibility(View.VISIBLE);
	}
}