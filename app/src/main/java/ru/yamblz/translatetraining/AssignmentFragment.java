package ru.yamblz.translatetraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentFragment extends Fragment {
    private AssignmentPresenter presenter;

    public AssignmentFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        presenter = new AssignmentPresenter();
        presenter.init();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
//        ButterKnife.bind(this, view);
//        progressBar.setProgress(95);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
//        progressBar.getProgressDrawable().setColorFilter(
//                getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

        FragmentManager childFragmentManager = getChildFragmentManager();

        Fragment mainFragment = childFragmentManager.findFragmentById(R.id.assignment_container);

        if (mainFragment == null) {
            mainFragment = new ComposeWordFragment();
            FragmentTransaction transaction = childFragmentManager.beginTransaction();
            transaction.replace(R.id.assignment_container, mainFragment);
            transaction.commit();
        }
        return view;
    }

}
