/**
 * 
 */
package com.thesunsoft.bevy;

import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author DAICA
 *
 */
public class SettingsActivity extends Activity implements OnClickListener{
	private static final String TAG = "SettingsActivity";
	private static final String ME = "me";
	private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";
    private ProgressDialog dialog;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[] {ID, NAME, PICTURE});
    private Button btnCancel;
	private TextView txtName;
	private ImageView imgAvatar;
	private UiLifecycleHelper uiHelper;
	//private LoginButton btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.activity_settings);
		initComponent();
		setActionListener();
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showStubImage(R.drawable.ic_launcher)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		dialog = ProgressDialog.show(this, "", "Waiting...", true, false);

	}
	
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Log.i(TAG, "onResume");
        final Session session = Session.getActiveSession();
        //Log.i(TAG, "Session->"+session.toString());
        
		new Handler().postDelayed(new Runnable() {								
			@Override
			public void run() {
				if (session != null && session.isOpened()) {
		        	makeMeRequest(session);
		        }						
			}
		}, 200);
        
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
    private void initComponent(){
    	btnCancel = (Button)findViewById(R.id.btn_cancel);
    	txtName = (TextView)findViewById(R.id.txt_name);
    	imgAvatar = (ImageView)findViewById(R.id.img_avatar);
    	//btnLogin = (LoginButton)findViewById(R.id.login_button);
    }
    private void setActionListener(){
    	btnCancel.setOnClickListener(this);
    	//btnLogin.setOnClickListener(new LoginButtonListener());
    }
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state, final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
    	Log.e(TAG, "onSessionStateChange");
    	Log.e(TAG, "session"+session.toString());
        if (state.isClosed()) {
        	Intent intent = new Intent(this, SplashActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        } 
    }
    private void makeMeRequest(final Session session) {
    	Bundle params = new Bundle();
    	params.putString(FIELDS, REQUEST_FIELDS);
    	Request requests = new Request(session, ME, params, HttpMethod.GET, new Request.Callback() {
			@Override
			public void onCompleted(Response response) {
				// TODO Auto-generated method stub
				Log.i(TAG, "onCompleted->"+response.toString());
				if(dialog!=null)
					dialog.dismiss();
				if(response.getError()!=null){
					Log.i(TAG, response.getError().toString());
				}
				
				try{
			        GraphObject go  = response.getGraphObject();
			        JSONObject  jso = go.getInnerJSONObject();
			        txtName.setText(jso.getString("name"));
			        JSONObject picture_obj = jso.getJSONObject("picture");
		            JSONObject picture_data = picture_obj.getJSONObject("data");
		            String url = picture_data.getString("url");
		            imageLoader.displayImage(url, imgAvatar, options);
			    }catch (Throwable t){
			        t.printStackTrace();
			    }
			}
		});
    	requests.executeAndWait();
    }
/*    private class LoginButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.d(TAG, "LoginButtonListener");
		}
    	
    }*/

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_cancel){
			finish();
		}
		
	}

}
