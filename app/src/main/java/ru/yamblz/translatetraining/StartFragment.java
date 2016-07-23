package ru.yamblz.translatetraining;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartFragment extends Fragment {

    enum FragmentCode {
        CARDS, SPEAK, LISTEN, SEARCH, BUILD, COMBO, CHOOSE, TRANSLATE
    }

    private boolean isOpened = false;

    private OnFragmentInteractionListener mListener;

    public static StartFragment newInstance() {
        return new StartFragment();
    }

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

    public interface OnFragmentInteractionListener {
        void onItemClick(FragmentCode fragmentCode);
    }

    private void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        //((LinearLayout) view.findViewById(R.id.ll_ex)).animate().scaleY(0);
        View ex = view.findViewById(R.id.ll_ex);
        int width = ((View) ex.getParent()).getWidth();
        ex.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ex.getLayoutParams().height = 0;
        ex.setVisibility(View.VISIBLE);
        ex.requestLayout();
        ex.setLayoutParams(new LinearLayout.LayoutParams(width, 0));



        ((MainActivity) getActivity()).setSupportActionBar(toolbar);


        view.findViewById(R.id.cv_cards).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(FragmentCode.BUILD);

                /*if (isOpenned)
                    view.findViewById(R.id.ll_ex).animate().scaleY(0).setDuration(500).start();
                else
                    view.findViewById(R.id.ll_ex).animate().scaleY(1).setDuration(500).start();

                isOpenned = !isOpenned;*/

                TextView btn = (TextView) view.findViewById(R.id.btn_cards);

                if (isOpened) {
                    //btn.setBackgroundColor(0xfffafafa);
                    //btn.setTextColor(0xff000000);
                    collapse(view.findViewById(R.id.ll_ex), btn, 0xfffafafa, 0xff000000);
                } else {
                    btn.setBackgroundColor(0xffff5252);
                    btn.setTextColor(0xffffffff);
                    expand(view.findViewById(R.id.ll_ex));
                }

                isOpened = !isOpened;
            }
        });
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        final int width = ((View) v.getParent()).getWidth();


        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }

        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setLayoutParams(new LinearLayout.LayoutParams(width, targetHeight));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v, final TextView btn, final int c1, final int c2) {
        final int initialHeight = v.getMeasuredHeight();
        final int width = ((View) v.getParent()).getWidth();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn.setBackgroundColor(c1);
                btn.setTextColor(c2);
                v.setLayoutParams(new LinearLayout.LayoutParams(width, 0));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
