package ru.yamblz.translatetraining;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.yamblz.translatetraining.model.WordPair;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeWordFragment extends Fragment {
    public static final String PAIR_KEY = "pair";
    private WordPair wordPair;

    private ComposeWordPresenter presenter;

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
        return view;
    }

    Map<Character, View> keyButtons = new HashMap<>();

    private void addLetters(LayoutInflater inflater) {
        String enWord = wordPair.getEn();
        ArrayList<Character> characters = new ArrayList<>();
        for (int i = 0; i < enWord.length(); i++)
            characters.add(enWord.charAt(i));
        Collections.shuffle(characters);

        for (char c : characters) {
            View view = inflater.inflate(R.layout.key_letter, keyboard, false);
            TextView letter = (TextView) view.findViewById(R.id.letter);
            letter.setText(String.valueOf(c));
            keyboard.addView(view);
            keyButtons.put(c, view);
        }
    }

}
