package net.doodlei.android.eazymeet.eventList.service;

import net.doodlei.android.eazymeet.utils.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EventListService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_LOCATION)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.INVITE_LIST)
    @FormUrlEncoded
    Call<String> getInviteList(
            @Field("user_id") String userId
    );

    @POST(AppConstants.INVITE_ACCEPT)
    @FormUrlEncoded
    Call<String> invitationAccept(
            @Field("invitation_identity_id") String invitationIdentityId,
            @Field("user_id") String userId,
            @Field("status") String status,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );

    @POST(AppConstants.USER_INVITED_MAP)
    @FormUrlEncoded
    Call<String> userInvitedMap(
            @Field("user_id") String userId,
            @Field("invitation_identity_id") String invitationIdentityId

    );

}
