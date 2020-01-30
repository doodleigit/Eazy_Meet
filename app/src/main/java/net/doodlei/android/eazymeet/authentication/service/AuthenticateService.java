package net.doodlei.android.eazymeet.authentication.service;

import net.doodlei.android.eazymeet.utils.AppConstants;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthenticateService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_LOCATION)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(AppConstants.COUNTRY_CODE_LIST)
    Call<String> getCountry();

    @POST(AppConstants.SEND_OTP)
    @FormUrlEncoded
    Call<String> sendOtp(
            @Field("phone_num") String phoneNumber,
            @Field("country_code") String countryCode
    );

    @POST(AppConstants.USER_DETAILS_BY_MOBILE)
    @FormUrlEncoded
    Call<String> getUserDetails(
            @Field("mobile") String phoneNumber,
            @Field("country_id") String countryId
    );

    @Multipart
    @POST(AppConstants.USER_PHOTO_UPLOAD)
    Call<String> uploadUserPhoto(
            @Part MultipartBody.Part file
    );

    @POST(AppConstants.USER_UPDATE)
    @FormUrlEncoded
    Call<String> updateUserInfo(
            @Field("user_id") String userId,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("media_id") String media_id
    );

}
