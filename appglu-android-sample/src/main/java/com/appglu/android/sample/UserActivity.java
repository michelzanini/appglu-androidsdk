package com.appglu.android.sample;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appglu.AsyncCallback;
import com.appglu.AuthenticationResult;
import com.appglu.User;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class UserActivity extends Activity {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private TextView text;
	private Button signup;
	private Button login;
	private Button logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.user_activity);
		
		this.text = (TextView) findViewById(R.id.text);
		
		signup = (Button) findViewById(R.id.signup);
		signup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				signup();
			}

			
		});
		
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				login();
			}
		});
		
		logout = (Button) findViewById(R.id.logout);
		logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				logout();
			}
		});
		
		this.changeAuthenticatedState();
	}

	private void changeAuthenticatedState() {
		if (AppGlu.isUserAuthenticated()) {
			String username = AppGlu.getAuthenticatedUser().getUsername();
			text.setText("Authenticated user: " + username);
			
			signup.setVisibility(View.GONE);
			login.setVisibility(View.GONE);
			logout.setVisibility(View.VISIBLE);
		} else {
			text.setText("User is not authenticated");
			
			signup.setVisibility(View.VISIBLE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
		}
	}
	
	private void signup() {
		String username = UUID.randomUUID().toString();
		AppGlu.userApi().signupInBackground(new User(username, "password"), authenticationResultCallback);
	}
	
	private void login() {
		AppGlu.userApi().loginInBackground("username", "password", authenticationResultCallback);
	}

	private void logout() {
		AppGlu.userApi().logoutInBackground(logoutCallback);
	}
	
	private AsyncCallback<AuthenticationResult> authenticationResultCallback = new SampleAsyncCallback<AuthenticationResult>(this) {
		
		public void onResult(AuthenticationResult result) {
			logger.info("Signup or Login result: " + result);
			if (!result.succeed()) {
				Toast.makeText(UserActivity.this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
			}
			changeAuthenticatedState();
		}
		
	};
	
	private SampleAsyncCallback<Boolean> logoutCallback = new SampleAsyncCallback<Boolean>(this) {
		public void onResult(Boolean result) {
			logger.info("Logout: " + result);
			changeAuthenticatedState();
		}
	};

}