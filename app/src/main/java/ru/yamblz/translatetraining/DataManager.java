package ru.yamblz.translatetraining;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.yamblz.translatetraining.api.API;
import ru.yamblz.translatetraining.api.APIService;
import ru.yamblz.translatetraining.model.WordPair;

/**
 * Created by Litun on 23.07.2016.
 */

public class DataManager {
    private Realm realm;
    public static final String API_KEY = "dict.1.1.20160723T103526Z.a6023816b945356b.da2a4a3bad61c82cef86d45a6acd69d2b04e6f02";

    private InputStream wordsJson;

    public DataManager(Context context) {
        realm = Realm.getInstance(
                new RealmConfiguration.Builder(context)
                        .name("myOtherRealm.realm")
                        .schemaVersion(0)
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

    public WordPair getWordPair() {
        RealmResults<WordPair> all = realm.where(WordPair.class).findAll();
        return all.get(new Random().nextInt(all.size()));
    }

    void loadData() {
        new AsyncTaskLoadDictionary() {
            @Override
            protected void onPostExecute(final List<WordPair> wordPairs) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(wordPairs);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.i("realm", "ok");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.i("realm", "fail");
                    }
                });
            }
        }.execute();
    }

    class AsyncTaskLoadDictionary extends AsyncTask<Void, Void, List<WordPair>> {

        @Override
        protected List<WordPair> doInBackground(Void... params) {
            Words words;
            try {
                words = readJsonFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (words == null)
                return null;

            List<WordPair> pairs = new ArrayList<>(200);

            APIService service = API.createService(APIService.class);
            for (String s : words.en) {
                Call<APIService.APIResponse> translate = service.getTranslate(API_KEY, "en-ru", s);
                try {
                    Response<APIService.APIResponse> response = translate.execute();
                    String translateString = response.body().getTranslate();
                    if (translateString == null)
                        continue;
                    WordPair pair = new WordPair();
                    pair.setEn(s);
                    pair.setRu(translateString);
                    pairs.add(pair);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String s : words.ru) {
                Call<APIService.APIResponse> translate = service.getTranslate(API_KEY, "ru-en", s);
                try {
                    Response<APIService.APIResponse> response = translate.execute();
                    String translateString = response.body().getTranslate();
                    if (translateString == null)
                        continue;
                    WordPair pair = new WordPair();
                    pair.setRu(s);
                    pair.setEn(translateString);
                    pairs.add(pair);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return pairs;
        }
    }

    void getTranslates() {
        Call<APIService.APIResponse> tasks = API.createService(APIService.class)
                .getTranslate(API_KEY, "en-ru", "time");
        tasks.enqueue(new Callback<APIService.APIResponse>() {
            @Override
            public void onResponse(Call<APIService.APIResponse> call, Response<APIService.APIResponse> response) {
                Log.i("api", String.valueOf(response.isSuccessful()));
            }

            @Override
            public void onFailure(Call<APIService.APIResponse> call, Throwable t) {
                Log.i("api", t.getMessage());
            }
        });
    }

    Words readJsonFile() throws IOException {
        InputStream is = this.wordsJson;

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();

        Words words = new Gson().fromJson(jsonString, Words.class);
        return words;
    }

    public void setWordsJson(InputStream wordsJson) {
        this.wordsJson = wordsJson;
    }

    static class Words {
        List<String> en;
        List<String> ru;
    }
}
