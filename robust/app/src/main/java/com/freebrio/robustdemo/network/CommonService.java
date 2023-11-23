package com.freebrio.robustdemo.network;

import com.freebrio.robustdemo.model.ModeBase;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommonService {

    /**
     * 获取更新patch包
     *
     * @param keyPatch String 格式需符合android_patch_versionCode_robustApkHash，如android_patch_10200_xxxx
     * @return Observable<Response < ModelBase < String>>>
     */
    @GET("/api/v4/config/android")
    Observable<Response<ModeBase<String>>> getPatchInfo(@Query("key") String keyPatch);
}