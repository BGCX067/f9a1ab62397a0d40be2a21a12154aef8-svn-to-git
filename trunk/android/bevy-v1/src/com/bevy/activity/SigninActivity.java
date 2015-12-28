package com.bevy.activity;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bevy.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class SigninActivity extends FragmentActivity {
	private static final String TAG = "SigninActivity";

	private LoginButton btLoginfacebook;

	private UiLifecycleHelper uiHelper;

	boolean isOpenIntent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_main);
		fInitialize(savedInstanceState);
	}

	private void fInitialize(final Bundle savedInstanceState) {
		uiHelper = new UiLifecycleHelper(SigninActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);

		btLoginfacebook = (LoginButton) findViewById(R.id.bt_loginfacebook);
		btLoginfacebook.setReadPermissions(Arrays.asList("user_likes",
				"user_status", "email"));
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			if (!isOpenIntent) {
				isOpenIntent = true;
				startActivity(new Intent(SigninActivity.this,
						LeftAndRightActivity.class));
				finish();
			}
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
