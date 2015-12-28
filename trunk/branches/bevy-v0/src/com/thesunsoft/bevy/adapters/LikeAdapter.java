package com.thesunsoft.bevy.adapters;

import java.util.ArrayList;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.model.LikeObj;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<LikeObj> lists = new ArrayList<LikeObj>();
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public LikeAdapter(Context _context, ArrayList<LikeObj> _lists){
		this.context = _context;
		this.lists = _lists;
    	options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showStubImage(R.drawable.ic_launcher)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();
    	imageLoader = ImageLoader.getInstance();
    	imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	static class ViewHolder{
		TextView textName;
		TextView textCategory;
		ImageView thumbnail;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder viewholder;
		if(view==null){
			viewholder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.cell_like, null);
			viewholder.textName = (TextView)view.findViewById(R.id.text_name);
			viewholder.textCategory = (TextView)view.findViewById(R.id.text_category);
			viewholder.thumbnail = (ImageView)view.findViewById(R.id.img_thumbnail);
			view.setTag(viewholder);
		}else{
			viewholder = (ViewHolder)view.getTag();
		}
		LikeObj item = lists.get(position);
		if(item !=null){
			viewholder.textName.setText("Name : "+item.getName());
			viewholder.textCategory.setText("Category : "+item.getCategory());
			//imageLoader.d
			imageLoader.displayImage(item.getImageUrl(), viewholder.thumbnail, options);
		}
		return view;
	}

}
