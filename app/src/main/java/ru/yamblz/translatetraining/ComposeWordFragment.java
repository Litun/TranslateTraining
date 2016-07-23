package ru.yamblz.translatetraining;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yamblz.translatetraining.model.WordPair;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeWordFragment extends Fragment {
    public static final String PAIR_KEY = "pair";
    private WordPair wordPair;
    private StringBuilder answerBuilder = new StringBuilder();

    private ComposeWordPresenter presenter;

    @BindView(R.id.root)
    View root;
    @BindView(R.id.target_text)
    TextView target;
    @BindView(R.id.answer_text)
    TextView answer;
    @BindView(R.id.grid)
    GridLayout keyboard;

    public ComposeWordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ComposeWordPresenter();
        Bundle arguments = getArguments();
//        String string = arguments.getString(PAIR_KEY);
        wordPair = presenter.getPair(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard_assignment, container, false);
        ButterKnife.bind(this, view);
        addLetters(inflater);
        target.setText(wordPair.getRu());
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErase();
            }
        });
        return view;
    }

    ArrayList<Key> keys = new ArrayList<>();

    private void addLetters(LayoutInflater inflater) {
        String enWord = wordPair.getEn();

        for (int i = 0; i < enWord.length(); i++) {
            Key key = new Key();
            key.id = i;
            key.character = enWord.charAt(i);
            keys.add(key);
        }

        List<View> keyViews = new ArrayList<>();
        for (final Key k : keys) {
            View view = inflater.inflate(R.layout.key_letter, keyboard, false);
            TextView letter = (TextView) view.findViewById(R.id.letter);
            letter.setText(String.valueOf(k.character));
            keyViews.add(view);
            k.view = view;
            final int id = k.id;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onKeyClicked(id);
                }
            });
        }
        Collections.shuffle(keyViews);
        for (View keyView : keyViews) {
            keyboard.addView(keyView);
        }

    }

    private void onKeyClicked(int id) {
        Key key = keys.get(id);
        answerBuilder.append(key.character);
        answer.setText(answerBuilder);
        key.view.setVisibility(View.INVISIBLE);

        if (answerBuilder.length() == wordPair.getEn().length())
            checkAnswer();
    }

    private void checkAnswer() {
        if (wordPair.getEn().equalsIgnoreCase(answerBuilder.toString())) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View inflate = getActivity().getLayoutInflater().inflate(R.layout.done_bottomsheet, (ViewGroup) root, false);
            bottomSheetDialog.setContentView(inflate);
            bottomSheetDialog.show();
        } else {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            View inflate = getActivity().getLayoutInflater().inflate(R.layout.fail_bottomsheet, (ViewGroup) root, false);
            bottomSheetDialog.setContentView(inflate);
            bottomSheetDialog.show();
        }
    }

    private void onErase() {
        if (answerBuilder.length() == 0)
            return;
        int lastI = answerBuilder.length() - 1;
        char lastC = answerBuilder.charAt(lastI);
        answerBuilder.deleteCharAt(lastI);
        answer.setText(answerBuilder);
        for (Key key : keys) {
            if (key.character == lastC && key.view.getVisibility() == View.INVISIBLE) {
                key.view.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    static class Key {
        int id;
        char character;
        View view;
    }

}
