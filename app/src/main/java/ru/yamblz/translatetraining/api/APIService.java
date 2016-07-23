package ru.yamblz.translatetraining.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Litun on 23.07.2016.
 */

public interface APIService {

    //https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=API-ключ&lang=en-ru&text=time
    @GET("dicservice.json/lookup")
    Call<APIResponse> getTranslate(@Query("key") String key, @Query("lang") String lang, @Query("text") String word);

    class APIResponse {
        List<Def> def;

        public String getTranslate() {
            if (def.size() > 0 && def.get(0).tr.size() > 0)
                return def.get(0).tr.get(0).text;
            else return null;
        }
    }

    class Def {
        String text;
        String pos;
        List<Translate> tr;
    }

    class Translate {
        String text;
        String pos;
    }
}
