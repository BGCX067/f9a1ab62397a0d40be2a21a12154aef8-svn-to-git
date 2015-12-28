package com.thesunsoft.bevy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.adapters.LikeAdapter;
import com.thesunsoft.bevy.model.LikeObj;

public class EventsActivity extends FragmentActivity implements OnClickListener, OnItemClickListener{
	private static final String TAG= "PeopleActivity";
	private static final String MY_LIKES = "me/events";
	private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String IMAGE = "picture";
    private static final String LINK = "link";
    private RequestAsyncTask taskLike=null;
    private ListView listView;
    private LikeAdapter adapter;
    private Context context;
    private Button btnCancel;
    private ArrayList<LikeObj> lists = new ArrayList<LikeObj>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		this.context = this;
		initCompnent();
		setActionListener();
		//this.loadData();
	}
	private void initCompnent(){
		listView = (ListView)findViewById(R.id.list_view_like);
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		adapter = new LikeAdapter(context, lists);
		listView.setAdapter(adapter);
	}
	private void setActionListener(){
		listView.setOnItemClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(taskLike!=null)
			taskLike.cancel(true);
	}
	public void loadData(){
		Session session = Session.getActiveSession();
		if(session==null){
			session = Session.openActiveSessionFromCache(this);
		}else if(session.isOpened()){
			Set<String> fields = new HashSet<String>();
			Bundle params =new Bundle();
			//params.putString("type", "like");
			//params.putString("q", "platform");
			//params.putString("type", "post");
			//params.putString("q", "watermelon");
			//params.putString("object", "website");
			String[] requiredFields = new String[]{
	                ID,
	                NAME,
	                CATEGORY,
	                IMAGE,
	                LINK
	        };
			fields.addAll(Arrays.asList(requiredFields));
			params.putString("fields", TextUtils.join(",", fields));
			params.putInt("limit", 1000);
			Request request = new Request(session, MY_LIKES, params, HttpMethod.GET, new Request.Callback() {
				
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
					try
				    {
						
				        GraphObject go  = response.getGraphObject();
				        JSONObject  jso = go.getInnerJSONObject();
				        JSONArray   arr = jso.getJSONArray("data");

				        for ( int i = 0; i < (arr.length()); i++ )
				        {
				            JSONObject json_obj = arr.getJSONObject(i);
				            LikeObj likeObj = new LikeObj();
				            likeObj.setId(json_obj.getString("id"));
				            likeObj.setName(json_obj.getString("name"));
				            likeObj.setCategory(json_obj.getString("category"));
				            likeObj.setLink(json_obj.getString("link"));
				            JSONObject picture_obj = json_obj.getJSONObject("picture");
				            //Log.e(TAG, "picture_obj=>"+picture_obj.toString());
				            JSONObject picture_data = picture_obj.getJSONObject("data");
				            
				            likeObj.setImageUrl(picture_data.getString("url"));
				            Log.e(TAG, "picture_obj=>"+i+"=>"+picture_data.getString("url"));
				            lists.add(likeObj);
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
			taskLike = new RequestAsyncTask(request);
			taskLike.execute();
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
