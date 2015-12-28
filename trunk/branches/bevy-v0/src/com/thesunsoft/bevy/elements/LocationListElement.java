package com.thesunsoft.bevy.elements;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.OpenGraphAction;
import com.thesunsoft.bevy.PickerActivity;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.helpers.BavyApplication;

public class LocationListElement extends BaseListElement{
	private String TAG = "LocationListElement";
    private static final String PLACE_KEY = "place";

    private GraphPlace selectedPlace = null;

    public LocationListElement(Activity activity, int requestCode) {
        super(activity.getResources().getDrawable(R.drawable.action_location),
        		activity.getResources().getString(R.string.action_location),
        		activity.getResources().getString(R.string.action_location_default),
              requestCode, activity);
    }

    @Override
    public View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.getActiveSession() != null &&
                        Session.getActiveSession().isOpened()) {
                    startPickerActivity(PickerActivity.PLACE_PICKER, getRequestCode());
                }
            }
        };
    }

    @Override
    public void onActivityResult(Intent data) {
        selectedPlace = ((BavyApplication) getActivity().getApplication()).getSelectedPlace();
        setPlaceText();
        notifyDataChanged();
    }

    @Override
    protected void populateOGAction(OpenGraphAction action) {
        if (selectedPlace != null) {
            action.setPlace(selectedPlace);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (selectedPlace != null) {
            bundle.putString(PLACE_KEY, selectedPlace.getInnerJSONObject().toString());
        }
    }

    @Override
    public boolean restoreState(Bundle savedState) {
        String place = savedState.getString(PLACE_KEY);
        if (place != null) {
            try {
                selectedPlace = GraphObject.Factory
                        .create(new JSONObject(place), GraphPlace.class);
                setPlaceText();
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "Unable to deserialize place.", e);
            }
        }
        return false;
    }

    private void setPlaceText() {
        String text = null;
        if (selectedPlace != null) {
            text = selectedPlace.getName();
        }
        if (text == null) {
            text = getActivity().getResources().getString(R.string.action_location_default);
        }
        setText2(text);
    }
    private void startPickerActivity(Uri data, int requestCode) {
        Intent intent = new Intent();
        intent.setData(data);
        intent.setClass(getActivity(), PickerActivity.class);
        getActivity().startActivityForResult(intent, requestCode);
    }
}
