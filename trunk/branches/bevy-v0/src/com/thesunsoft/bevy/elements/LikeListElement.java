package com.thesunsoft.bevy.elements;





import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.facebook.Session;

import com.facebook.model.OpenGraphAction;
import com.thesunsoft.bevy.LikesActivity;
import com.thesunsoft.bevy.PickerActivity;
import com.thesunsoft.bevy.R;

public class LikeListElement extends BaseListElement {
	public String TAG = "LikeListElement";


    public LikeListElement(Activity activity, int requestCode) {
        super(activity.getResources().getDrawable(R.drawable.action_like),
        		activity.getResources().getString(R.string.action_like),
        		activity.getResources().getString(R.string.action_like_default), requestCode,
        		activity);
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.getActiveSession() != null &&
                        Session.getActiveSession().isOpened()) {
                    startPickerActivity(PickerActivity.FRIEND_PICKER, getRequestCode());
                }
            }
        };
    }

    @Override
    public void onActivityResult(Intent data) {
        notifyDataChanged();
    }

    @Override
    protected void populateOGAction(OpenGraphAction action) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @Override
    public boolean restoreState(Bundle savedState) {
        return false;
    }



    
    private void startPickerActivity(Uri data, int requestCode) {
        Intent intent = new Intent();
        intent.setData(data);
        intent.setClass(getActivity(), LikesActivity.class);
        getActivity().startActivityForResult(intent, requestCode);
    }
}
