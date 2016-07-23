package ru.yamblz.translatetraining;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartFragment extends Fragment implements OnTitleClickListener, OnStartClickListener {

    enum FragmentCode {
        CARDS, SPEAK, LISTEN, SEARCH, COMPOSE, COMBO, CHOOSE, TRANSLATE
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        setRetainInstance(true);
        initViews(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTitleClick(int position) {

    }

    @Override
    public void onStartClickListener(int position) {
        mListener.onItemClick(null);
    }

    public interface OnFragmentInteractionListener {
        void onItemClick(FragmentCode fragmentCode);
    }

    private void initViews(View view) {
        RecyclerView rvStartMenu = (RecyclerView) view.findViewById(R.id.rv_start_menu);

        //if (adapter == null) {
        StartMenuAdapter adapter = new StartMenuAdapter(this, this);
        rvStartMenu.setAdapter(adapter);
        //adapter.setData();
        //}



        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);



/*        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Режим обучения");

        view.findViewById(R.id.cv_cards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(FragmentCode.CARDS);

                TextView btn = (TextView) view.findViewById(R.id.tv_cards);

                if (isOpened[0]) {
                    isOpened[0] = false;
                    collapse(view.findViewById(R.id.ll_ex_cards), btn, 0xfffafafa);
                } else {
                    isOpened[0] = false; isOpened[1] = false;
                    isOpened[0] = true;

                    btn.setBackgroundColor(0xffff5252);
                    btn.setTextColor(0xffffffff);
                    expand(view.findViewById(R.id.ll_ex_cards));
                }
            }
        });

        view.findViewById(R.id.cv_compose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(FragmentCode.COMPOSE);

                TextView btn = (TextView) view.findViewById(R.id.tv_compose);

                if (isOpened[1]) {
                    isOpened[1] = false;
                    collapse(view.findViewById(R.id.ll_ex_compose), btn, 0xfffafafa);
                } else {
                    isOpened[0] = false; isOpened[1] = false;
                    isOpened[1] = true;

                    btn.setBackgroundColor(0xffff5722);
                    btn.setTextColor(0xffffffff);
                    expand(view.findViewById(R.id.ll_ex_compose));
                }
            }
        });*/
    }



}
