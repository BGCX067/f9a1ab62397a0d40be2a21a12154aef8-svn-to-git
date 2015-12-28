/**
 * 
 */
package com.thesunsoft.bevy;



import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * @author DAICA
 *
 */
public class SplashActivity extends Activity{
	public static final String TAG= "SplashActivity";
	private UiLifecycleHelper uiHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.activity_splash);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.thesunsoft.bevy", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.i("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
        	Log.i(TAG, e.toString());
        } catch (NoSuchAlgorithmException e) {
        	Log.i(TAG, e.toString());
        }*/
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
    }

	@Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Log.i(TAG, "onResume");
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
        	Intent intent = new Intent(this, HomeActivity.class);
        	startActivity(intent);
        }
    }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

    }
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	
        if (state.equals(SessionState.OPENED)) {
        	Intent intent = new Intent(this, HomeActivity.class);
        	startActivity(intent);
        } 
    }

}
