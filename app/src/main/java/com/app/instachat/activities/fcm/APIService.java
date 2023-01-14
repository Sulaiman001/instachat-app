package com.app.instachat.activities.fcm;

import com.app.instachat.activities.fcmmodels.MyResponse;
import com.app.instachat.activities.fcmmodels.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization: key=AAAAFtD87aA:APA91bFE3Ue7HQQct_H9p4l_YEWeqPKcanV_B6rxBX--kKNwFcEImTHGeglDBuWxLCmnM0GboNbBhELLUygkP8erwIEI8NyPbus6ZFhfFvKI-_GGGi9cIJGoUTNe_2Lxtfb_wsjztwOr"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
