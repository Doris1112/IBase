package com.doris.ibase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Doris on 2018/10/27.
 */
public interface PoetryService {

    String BASE_URL = "http://api.apiopen.top/";

    /**
     * 获取宋朝古诗词：
     * http://api.apiopen.top/getSongPoetry?page=1&count=20
     * 获取唐朝古诗词：
     * http://api.apiopen.top/getTangPoetry?page=1&count=20
     * 搜索古诗词作者：
     * http://api.apiopen.top/searchAuthors?name=李白
     **/

    @GET("getSongPoetry")
    Call<ResponseBody> getSonyPoetry(@Query("page") int page,
                                     @Query("count") int count);

    @GET("getTangPoetry")
    Call<ResponseBody> getTangPoetry(@Query("page") int page,
                                     @Query("count") int count);

    @FormUrlEncoded
    @POST("searchAuthors")
    Call<ResponseBody> searchAuthors(@Field("name") String name);
}
