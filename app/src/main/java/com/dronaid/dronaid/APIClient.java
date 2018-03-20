package com.dronaid.dronaid;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bennyhawk on 12/18/17.
 */

public class APIClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://dronaid-api.herokuapp.com/mad/";

    public static APIInterface getAPIInterface(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(APIInterface.class);
    }



    public interface APIInterface{
        @GET("signin")
        Call<SigninResponseModel> signin(@Query("username") String username, @Query("password") String password);

        @GET("sync")
        Call<SyncResponseModel> sync(@Query("username") String username, @Query("token") String token);

        @GET("deleteUser")
        Call<DeleteUserResponseModel> deleteUser(@Query("username") String username, @Query("token") String token, @Query("password") String password);

        @GET("register")
        Call<RegisterResponseModel> register(@Query("username") String username, @Query("password") String accpassword, @Query("accounttype") String accounttype);

        @GET("requestDrone")
        Call<RequestDroneResponseModel> requestDrone(@Query("username") String username, @Query("password") String accpassword, @Query("lat") double latitude,@Query("long") double longitude, @Query("token") String token);

        @GET("updateUser")
        Call<UpdateUserResponseModel> updateUser(@Query("username") String username, @Query("token") String token, @Query("password") String password,@Query("newusername") String newusername, @Query("newpassword") String newpassword);

        @GET("searchDoctor")
        Call<SearchDoctorResponseModel> searchDoctor(@Query("username") String username, @Query("token") String token,@Query("dUsername") String dUsername);

    }
}
