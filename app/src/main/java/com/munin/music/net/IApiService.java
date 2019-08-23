package com.munin.music.net;

import com.munin.music.BuildConfig;
import com.munin.music.data.PictureData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author M
 */
public interface IApiService {
    /***/
    @GET(BuildConfig.IMAGE_REQUEST_URL)
    Observable<PictureData> getPictures(@Query("page") int page);
}
