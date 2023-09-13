package com.sygic.example.ipcdemo3d.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sygic.example.ipcdemo3d.SdkApplication;
import com.sygic.sdk.remoteapi.ApiMaps;
import com.sygic.sdk.remoteapi.ApiPoi;
import com.sygic.sdk.remoteapi.exception.GeneralException;
import com.sygic.sdk.remoteapi.exception.InvalidLocationException;
import com.sygic.sdk.remoteapi.model.Poi;
import com.sygic.example.ipcdemo3d.R;

/**
 * shows info about the poi
 */
public class PoiInfoFragment extends Fragment {
    private Poi mPoi;

    /**
     * read the arguments to a POI class
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPoi = Poi.readBundle(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi_info, container, false);
        view.findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPoi != null) {
                    try {
                        ApiMaps.showCoordinatesOnMap(mPoi.getLocation(), SdkApplication.ZOOM, SdkApplication.MAX);
                    } catch (GeneralException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        fillView(view);
        return view;
    }

    /**
     * show fields
     *
     * @param view
     */
    private void fillView(View view) {

        String empty = "empty";

        if (mPoi != null) {
            TextView tv = (TextView) view.findViewById(R.id.tv1);
            tv.setText(mPoi.getName().equals("") ? empty : mPoi.getName());

            tv = (TextView) view.findViewById(R.id.tv2);
            tv.setText(mPoi.getAddress().equals("") ? empty : mPoi.getAddress());

            tv = (TextView) view.findViewById(R.id.tv3);
            tv.setText(mPoi.getCategory().equals("") ? empty : mPoi.getCategory());

            tv = (TextView) view.findViewById(R.id.tv4);
            tv.setText("Lon:" + mPoi.getLocation().getX() + " Lat:" + mPoi.getLocation().getY());

            tv = (TextView) view.findViewById(R.id.tv5);
            tv.setText(mPoi.isSearchAddress() ? "true" : "false");
        }
    }
}