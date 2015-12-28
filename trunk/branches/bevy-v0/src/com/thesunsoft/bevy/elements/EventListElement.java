package com.thesunsoft.bevy.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.thesunsoft.bevy.LikesActivity;
import com.thesunsoft.bevy.PickerActivity;
import com.thesunsoft.bevy.R;
import com.thesunsoft.bevy.helpers.BavyApplication;

public class EventListElement extends BaseListElement {
	private String TAG = "LikeListElement";
	private static final String LIKES_KEY = "likes";

    private List<GraphUser> selectedUsers;

    public EventListElement(Activity activity, int requestCode) {
        super(activity.getResources().getDrawable(R.drawable.action_calendar),
        		activity.getResources().getString(R.string.action_event),
        		activity.getResources().getString(R.string.action_event_default), requestCode,
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
        selectedUsers = ((BavyApplication) getActivity().getApplication()).getSelectedUsers();
        setUsersText();
        notifyDataChanged();
    }

    @Override
    protected void populateOGAction(OpenGraphAction action) {
        if (selectedUsers != null) {
            action.setTags(selectedUsers);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (selectedUsers != null) {
            bundle.putByteArray(LIKES_KEY, getByteArray(selectedUsers));
        }
    }

    @Override
    public boolean restoreState(Bundle savedState) {
        byte[] bytes = savedState.getByteArray(LIKES_KEY);
        if (bytes != null) {
            selectedUsers = restoreByteArray(bytes);
            setUsersText();
            return true;
        }
        return false;
    }

    private void setUsersText() {
        String text = null;
        if (selectedUsers != null) {
            if (selectedUsers.size() == 1) {
                text = String.format(getActivity().getResources().getString(R.string.single_user_selected),
                        selectedUsers.get(0).getName());
            } else if (selectedUsers.size() == 2) {
                text = String.format(getActivity().getResources().getString(R.string.two_users_selected),
                        selectedUsers.get(0).getName(), selectedUsers.get(1).getName());
            } else if (selectedUsers.size() > 2) {
                text = String.format(getActivity().getResources().getString(R.string.multiple_users_selected),
                        selectedUsers.get(0).getName(), (selectedUsers.size() - 1));
            }
        }
        if (text == null) {
            text = getActivity().getResources().getString(R.string.action_people_default);
        }
        setText2(text);
    }

    private byte[] getByteArray(List<GraphUser> users) {
        // convert the list of GraphUsers to a list of String where each element is
        // the JSON representation of the GraphUser so it can be stored in a Bundle
        List<String> usersAsString = new ArrayList<String>(users.size());

        for (GraphUser user : users) {
            usersAsString.add(user.getInnerJSONObject().toString());
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            new ObjectOutputStream(outputStream).writeObject(usersAsString);
            return outputStream.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "Unable to serialize users.", e);
        }
        return null;
    }

    private List<GraphUser> restoreByteArray(byte[] bytes) {
        try {
            @SuppressWarnings("unchecked")
            List<String> usersAsString =
                    (List<String>) (new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
            if (usersAsString != null) {
                List<GraphUser> users = new ArrayList<GraphUser>(usersAsString.size());
                for (String user : usersAsString) {
                    GraphUser graphUser = GraphObject.Factory
                            .create(new JSONObject(user), GraphUser.class);
                    users.add(graphUser);
                }
                return users;
            }
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Unable to deserialize users.", e);
        } catch (IOException e) {
            Log.e(TAG, "Unable to deserialize users.", e);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to deserialize users.", e);
        }
        return null;
    }
    
    private void startPickerActivity(Uri data, int requestCode) {
        Intent intent = new Intent();
        intent.setData(data);
        intent.setClass(getActivity(), LikesActivity.class);
        getActivity().startActivityForResult(intent, requestCode);
    }
}
