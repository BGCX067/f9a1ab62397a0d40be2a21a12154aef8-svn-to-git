package com.thesunsoft.bevy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.adapters.PeopleAdapter;
import com.thesunsoft.bevy.model.PeopleObj;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PeopleActivity extends Activity implements OnItemClickListener, OnClickListener{
	private static final String TAG= "PeopleActivity";
	private static final String NAME = "name";
	private static final String GENDER = "gender";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";
    private ListView listView;
    private PeopleAdapter adapter;
    private Context context;
    private Button btnCancel;
    private EditText txtSearch;
    private TextView txtEmpty;
    private ProgressDialog dialog;
    private ArrayList<PeopleObj> lists = new ArrayList<PeopleObj>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peoples);
		this.context = this;
		initCompnent();
		setActionListener();
		dialog = ProgressDialog.show(context, "", "Waiting...", true, false);
		new Handler().postDelayed(new Runnable() {								
			@Override
			public void run() {
				loadData();								
			}
		}, 200);
		
	}
	private void initCompnent(){
		listView = (ListView)findViewById(R.id.list_view_peoples);
		txtEmpty = (TextView)findViewById(android.R.id.empty);
		listView.setEmptyView(txtEmpty);
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		txtSearch = (EditText)findViewById(R.id.txt_search);
		adapter = new PeopleAdapter(context, lists);
		listView.setAdapter(adapter);
	}
	private void setActionListener(){
		listView.setOnItemClickListener(this);
		btnCancel.setOnClickListener(this);
		txtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Log.i(TAG, "Key="+cs);
				String lowerSearch = cs.toString().toLowerCase();  
				ArrayList<PeopleObj> tmpLsts = new ArrayList<PeopleObj>();
				for (int i=0; i < lists.size(); i++)
				{
				    if (lists.get(i).getName().toLowerCase().contains(lowerSearch))
				    {
				        tmpLsts.add(lists.get(i));
				    }
				}
				if(tmpLsts.size()!=0){
					adapter = new PeopleAdapter(context, tmpLsts);
					listView.setAdapter(adapter);
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
	private void loadData(){
		Session session = Session.getActiveSession();
		if(session==null){
			session = Session.openActiveSessionFromCache(this);
		}else if(session.isOpened()){
			
			Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
				@Override
				public void onCompleted(List<GraphUser> users, Response response) {
					if(dialog!=null)
						dialog.dismiss();
					try
				    {
				        GraphObject go  = response.getGraphObject();
				        JSONObject  jso = go.getInnerJSONObject();
				        JSONArray   arr = jso.getJSONArray("data");

				        for ( int i = 0; i < (arr.length()); i++ )
				        {
				            JSONObject json_obj = arr.getJSONObject(i);
				            PeopleObj peopleObj = new PeopleObj();
				            peopleObj.setId(json_obj.getString("id"));
				            peopleObj.setName(json_obj.getString("name"));
				            peopleObj.setGender(json_obj.getString("gender"));
				            JSONObject picture_obj = json_obj.getJSONObject("picture");
				            //Log.e(TAG, "picture_obj=>"+picture_obj.toString());
				            JSONObject picture_data = picture_obj.getJSONObject("data");
				            
				            peopleObj.setPicture(picture_data.getString("url"));
				            Log.e(TAG, "picture_obj=>"+i+"=>"+picture_data.getString("url"));
				            lists.add(peopleObj);
				        }
				        
				    }
				    catch ( Throwable t )
				    {
				        t.printStackTrace();
				    }
					if(lists.size()!=0){
						adapter.notifyDataSetChanged();
					}
				}
			});
			//Log.i(TAG, "requestu=>"+requestu.toString());
			Bundle params = new Bundle();
			params.putInt("limit", 1000);
	    	params.putString(FIELDS, TextUtils.join(",", new String[] {ID, NAME, PICTURE, GENDER}));
			request.setParameters(params);
			request.executeAndWait();
		}
			
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_cancel){
			finish();
		}
	}
}
