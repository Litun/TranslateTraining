package ru.yamblz.translatetraining;

import ru.yamblz.translatetraining.model.WordPair;

/**
 * Created by Litun on 23.07.2016.
 */

public class ComposeWordPresenter {
    public WordPair getPair(String string) {
        WordPair wordPair = YaApp.getDataManager().getWordPair();
        return wordPair;
    }
}
