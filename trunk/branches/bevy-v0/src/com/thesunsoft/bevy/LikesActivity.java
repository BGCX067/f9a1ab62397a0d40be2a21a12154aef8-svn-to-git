package com.thesunsoft.bevy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.adapters.LikeAdapter;
import com.thesunsoft.bevy.model.LikeObj;

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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class LikesActivity extends Activity implements OnClickListener, OnItemClickListener{
	private static final String TAG= "LikesActivity";
	private static final String MY_LIKES = "me/likes";
	private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String IMAGE = "picture";
    private static final String LINK = "link";
    private static final String[] KEY_WORDS = new String[]{"coffee", "wine", "beer", "tea", "liquor"};
    private ListView listView;
    private LikeAdapter adapter;
    private Context context;
    private Button btnCancel;
    private EditText txtSearch;
    private TextView txtEmpty;
    private ProgressDialog dialog;
    private ArrayList<LikeObj> lists = new ArrayList<LikeObj>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_likes);
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
		listView = (ListView)findViewById(R.id.list_view_like);
		txtEmpty = (TextView)findViewById(android.R.id.empty);
		listView.setEmptyView(txtEmpty);
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		txtSearch = (EditText)findViewById(R.id.txt_search);
		adapter = new LikeAdapter(context, lists);
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
				ArrayList<LikeObj> tmpLsts = new ArrayList<LikeObj>();
				for (int i=0; i < lists.size(); i++)
				{
				    if (lists.get(i).getName().toLowerCase().contains(lowerSearch))
				    {
				        tmpLsts.add(lists.get(i));
				    }
				}
				if(tmpLsts.size()!=0){
					adapter = new LikeAdapter(context, tmpLsts);
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
			Request requests = new Request(session, MY_LIKES, params, HttpMethod.GET, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub
					try
				    {
						if(dialog!=null)
							dialog.dismiss();
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
				            //Log.i(TAG, "picture_obj=>"+i+"=>"+picture_data.getString("url"));
				            if(json_obj.getString("name").toLowerCase().contains(KEY_WORDS[0]) || json_obj.getString("name").toLowerCase().contains(KEY_WORDS[1])
				               || json_obj.getString("name").toLowerCase().contains(KEY_WORDS[2]) || json_obj.getString("name").toLowerCase().contains(KEY_WORDS[3])
				               || json_obj.getString("name").toLowerCase().contains(KEY_WORDS[4]))
				            	lists.add(likeObj);
				            	
				        }
				        
				    }
				    catch ( Throwable t )
				    {
				    	if(dialog!=null)
							dialog.dismiss();
				        t.printStackTrace();
				    }
					if(lists.size()!=0){
						adapter.notifyDataSetChanged();
					}
				}
			});
			requests.executeAndWait();

			//taskLike = new RequestAsyncTask(requests);
			
			//taskLike.execute();
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
