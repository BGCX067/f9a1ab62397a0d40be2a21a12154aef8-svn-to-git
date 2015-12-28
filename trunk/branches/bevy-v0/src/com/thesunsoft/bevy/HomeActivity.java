/**
 * 
 */
package com.thesunsoft.bevy;



import java.util.ArrayList;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.thesunsoft.bevy.adapters.ActionListAdapter;
import com.thesunsoft.bevy.elements.BaseListElement;
                                
import com.thesunsoft.bevy.elements.LikeListElement;
import com.thesunsoft.bevy.elements.LocationListElement;
import com.thesunsoft.bevy.elements.PeopleListElement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


/**
 * @author DAICA
 *
 */
public class HomeActivity extends Activity implements OnClickListener {
	public static final String TAG= "HomeActivity";
	private UiLifecycleHelper uiHelper;
	private ListView listView;
	private Button btnSettings;
	private List<BaseListElement> listElements;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initComponent();
		setOnActionListener();
		init(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
	}

	@Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Session session = Session.getActiveSession();
        if (session == null) {
        	Intent intent = new Intent(this, SplashActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    private void initComponent(){
    	btnSettings = (Button)findViewById(R.id.btn_settings);
    	listView = (ListView) findViewById(R.id.selection_list);
    	
    }
    private void setOnActionListener(){
    	btnSettings.setOnClickListener(this);
    }
    /**
     * Resets the view to the initial defaults.
     */
    private void init(Bundle savedInstanceState) {

        listElements = new ArrayList<BaseListElement>();

        listElements.add(new LocationListElement(this, 1));
        listElements.add(new PeopleListElement(this, 2));
        listElements.add(new LikeListElement(this, 3));
        //listElements.add(new EventListElement(this, 4));
        if (savedInstanceState != null) {
            for (BaseListElement listElement : listElements) {
                listElement.restoreState(savedInstanceState);
            }
        }

        listView.setAdapter(new ActionListAdapter(this, R.id.selection_list, listElements));

    }
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	
        if (state.equals(SessionState.OPENED)) {
        	Intent intent = new Intent(this, SplashActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        } 
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_settings){
			Intent intent = new Intent(this, SettingsActivity.class);
        	startActivity(intent);
		}
	}

}
