package com.thesunsoft.bevy;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.adapters.LikeAdapter;
import com.thesunsoft.bevy.model.LikeObj;

public class PlaceActivity extends FragmentActivity implements OnItemClickListener{
	private static final String TAG= "PlaceActivity";

    private RequestAsyncTask taskPeople=null;
    private ListView listView;
    private LikeAdapter adapter;
    private Context context;
    private ArrayList<LikeObj> lists = new ArrayList<LikeObj>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locations);
		this.context = this;
		initCompnent();
		setActionListener();
		this.loadData();
	}
	private void initCompnent(){
		listView = (ListView)findViewById(R.id.list_view_peoples);
		adapter = new LikeAdapter(context, lists);
		listView.setAdapter(adapter);
	}
	private void setActionListener(){
		listView.setOnItemClickListener(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(taskPeople!=null)
			taskPeople.cancel(true);

	}
	private void loadData(){
		Session session = Session.getActiveSession();
		if(session==null){
			session = Session.openActiveSessionFromCache(this);
		}else if(session.isOpened()){
			Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
				@Override
				public void onCompleted(List<GraphUser> users, Response response) {
					// TODO Auto-generated method stub
					if(response!=null){
					Log.e(TAG, "response"+response.toString());
					Log.i(TAG, "GraphUser"+users.toString());
					}
					/*try
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
					}*/
				}
			});
			//Log.i(TAG, "requestu=>"+requestu.toString());
			taskPeople = new RequestAsyncTask(request);
			taskPeople.execute();
		}
			
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
