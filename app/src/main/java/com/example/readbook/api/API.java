package com.example.readbook.api;

import com.example.readbook.accoount.Account;
import com.example.readbook.book.Book;
import com.example.readbook.book.Category;
import com.example.readbook.response.ResponseMess;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    API api = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.4:3000") // Physical device
            .baseUrl("http://10.0.2.2:3000") // Virtual device
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API.class);

    @GET("/no_need_login/all_book")
    Call<ArrayList<Book>> allBook();

    @GET("/no_need_login/limit_hot")
    Call<ArrayList<Book>> limitHot();

    @GET("/no_need_login/limit_new")
    Call<ArrayList<Book>> limitNew();

    @GET("/no_need_login/category")
    Call<ArrayList<Category>> listCategory();

    @GET("/no_need_login/{id}")
    Call<ArrayList<Book>> listBookByCategory(@Path("id") String link);

    @FormUrlEncoded
    @POST("no_need_login/information_book")
    Call<Book> infoBook(@Field("name") String name);

    @FormUrlEncoded
    @POST("no_need_login/related")
    Call<ArrayList<Book>> relatedBook(@Field("name") String name, @Field("type") String type);

    @FormUrlEncoded
    @POST("no_need_login/add_number_of_read")
    Call<ArrayList<Book>> addNumberOfRead(@Field("name") String name);

    @FormUrlEncoded
    @POST("account/login")
    Call<Account> login(@Field("email") String email, @Field("password") String pass);

    @FormUrlEncoded
    @POST("account/require_create")
    Call<ResponseMess> requireCreate(@Field("email") String email, @Field("password") String password, @Field("name") String name);

    @FormUrlEncoded
    @POST("account/create")
    Call<Account> create(@Field("otp") String otp);

    @FormUrlEncoded
    @POST("account/send_mail")
    Call<ResponseMess> forgetPass(@Field("email") String email);

    @GET("/account/verification")
    Call<ResponseMess> verificationForgetPass(@Query("otp") String otp);

    @FormUrlEncoded
    @POST("account/change_info")
    Call<Account> changeInfo(@Field("email") String email, @Field("newPassword") String newPassword, @Field("newName") String newName);

    @FormUrlEncoded
    @POST("need_login/collection")
    Call<ArrayList<Book>> collection(@Field("email") String email);

    @FormUrlEncoded
    @POST("need_login/collection_yes_or_no")
    Call<ResponseMess> collectionYesNo(@Field("email") String email, @Field("name") String name);

    @FormUrlEncoded
    @POST("need_login/recently")
    Call<ArrayList<Book>> recently(@Field("email") String email);

    @FormUrlEncoded
    @POST("need_login/add_collection")
    Call<ResponseMess> addCollection(@Field("email") String email, @Field("img") String img, @Field("name") String name, @Field("author") String author);

    @FormUrlEncoded
    @POST("need_login/delete_collection")
    Call<ResponseMess> delCollection(@Field("email") String email, @Field("name") String name);

    @FormUrlEncoded
    @POST("need_login/add_recently")
    Call<ResponseMess> addRecently(@Field("email") String email, @Field("img") String img, @Field("name") String name, @Field("author") String author);
}
