package com.sygic.example.ipcdemo3d.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.sygic.example.ipcdemo3d.R;
import com.sygic.example.ipcdemo3d.SdkApplication;
import com.sygic.sdk.remoteapi.ApiLocation;
import com.sygic.sdk.remoteapi.ApiNavigation;
import com.sygic.sdk.remoteapi.exception.GeneralException;
import com.sygic.sdk.remoteapi.exception.InvalidLocationException;
import com.sygic.sdk.remoteapi.model.Position;
import com.sygic.sdk.remoteapi.model.RoadInfo;
import com.sygic.sdk.remoteapi.model.WayPoint;

/**
 * geocoding
 */
public class LocationFragment extends Fragment {
    private EditText mPosX, mPosY, mAddress, mCustomAddress;
    private TextView mText;

    public LocationFragment() {
    }

    /**
     * inflate corresponding view, register views...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.fragment_location, container, false);

        registerButtons(mRoot);
        registerFields(mRoot);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int[] coords = {sharedPref.getInt("locStopLon", 0), sharedPref.getInt("locStopLat", 0)};
        String address = sharedPref.getString("locAddress", "");
        String customAddress = sharedPref.getString("customAddress", "");

        mPosX.setText(coords[0] == 0 ? "" : Integer.toString(coords[0]));
        mPosY.setText(coords[1] == 0 ? "" : Integer.toString(coords[1]));
        mAddress.setText(address);
        mCustomAddress.setText(customAddress);

        return mRoot;
    }

    @Override
    public void onDestroyView() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("locStopLon", mPosX.getText().toString().equals("") ? 0 : Integer.parseInt(mPosX.getText().toString()));
        editor.putInt("locStopLat", mPosY.getText().toString().equals("") ? 0 : Integer.parseInt(mPosY.getText().toString()));
        editor.putString("locAddress", mAddress.getText().toString());
        editor.putString("customAddress", mCustomAddress.getText().toString());
        editor.commit();
        super.onDestroyView();
    }

    private void registerFields(View view) {
        mPosX = view.findViewById(R.id.et1);
        mPosY = view.findViewById(R.id.et2);
        mAddress = view.findViewById(R.id.et3);
        mText = view.findViewById(R.id.tv1);
        mCustomAddress = view.findViewById(R.id.edCustomAddress);
    }

    private void registerButtons(View rootView) {
        final Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        Button btn = (Button) rootView.findViewById(R.id.btn_loc_address_info);
        btn.setOnClickListener(view -> {
            String str = null;
            try {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                } else {
                    mPosY.startAnimation(shake);
                }
                str = ApiLocation.getLocationAddressInfo(new Position(x, y), SdkApplication.MAX);
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
            if (str != null) {
                mText.setText(str);
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_road_info);
        btn.setOnClickListener(view -> {
            RoadInfo info = null;
            try {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                } else {
                    mPosY.startAnimation(shake);
                }
                info = ApiLocation.getLocationRoadInfo(new Position(x, y), SdkApplication.MAX);
            } catch (InvalidLocationException e) {
                e.printStackTrace();
            }
            if (info != null) {
                mText.setText(info.toString());
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_navi_point);
        btn.setOnClickListener(view -> {
            try {
                int x = 0, y = 0;
                if (!mPosX.getText().toString().equals("")) {
                    x = Integer.parseInt(mPosX.getText().toString());
                } else {
                    mPosX.startAnimation(shake);
                }
                if (!mPosY.getText().toString().equals("")) {
                    y = Integer.parseInt(mPosY.getText().toString());
                    String address = ApiLocation.getLocationAddressInfo(new Position(x, y), SdkApplication.MAX);
                    String customAddress = mCustomAddress.getText().toString();
                    WayPoint wayPoint;

                    if (customAddress.isEmpty()) {
                        wayPoint = new WayPoint(address, x, y);
                    } else {
                        wayPoint = new WayPoint(address, x, y, customAddress);
                    }

                    ApiNavigation.startNavigation(wayPoint, 0, false, SdkApplication.MAX);
                } else {
                    mPosY.startAnimation(shake);
                }
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        });

        btn = (Button) rootView.findViewById(R.id.btn_loc_navi_add);
        btn.setOnClickListener(view -> {
            try {
                if (mAddress.getText().length() == 0) {
                    mAddress.startAnimation(shake);
                } else {
                    Position pos = ApiLocation.locationFromAddress(mAddress.getText().toString(), false, true, 0);
                    String customAddress = mCustomAddress.getText().toString();
                    WayPoint wayPoint;
                    if (customAddress.isEmpty()) {
                        wayPoint = new WayPoint(mAddress.getText().toString(), pos.getX(), pos.getY());
                    } else {
                        wayPoint = new WayPoint(mAddress.getText().toString(), pos.getX(), pos.getY(), customAddress);
                    }
                    ApiNavigation.startNavigation(wayPoint, 0, false, 0);
                }
            } catch (GeneralException e) {
                e.printStackTrace();
            }
        });
    }
}
