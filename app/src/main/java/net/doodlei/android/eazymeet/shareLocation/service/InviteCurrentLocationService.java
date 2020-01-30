package net.doodlei.android.eazymeet.shareLocation.service;

import net.doodlei.android.eazymeet.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InviteCurrentLocationService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_LOCATION)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.INVITE_TO_CURRENT)
    @FormUrlEncoded
    Call<String> getInviteWaitingList(
            @Field("user_id") String userId,
            @Field("location_name") String locationName,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("validity_time") long validity,
            @Field("mobile") List<String> mobileList,
            @Field("country_code") List<String> countryList,
            @Field("name") List<String> nameList
    );

    @POST(AppConstants.INVITE_ACCEPTED_LIST)
    @FormUrlEncoded
    Call<String> getInviteAcceptedList(
            @Field("user_id") String userId,
            @Field("invitation_identity_id") String invitationIdentityId
    );

}
