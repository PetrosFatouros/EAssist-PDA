package com.unipi.pfatouros.eassist.utility;

import com.unipi.pfatouros.eassist.model.Inventory;
import com.unipi.pfatouros.eassist.model.Item;
import com.unipi.pfatouros.eassist.model.Order;
import com.unipi.pfatouros.eassist.model.Table;
import com.unipi.pfatouros.eassist.model.User;
import com.unipi.pfatouros.eassist.model.request.AddOrderRequest;
import com.unipi.pfatouros.eassist.model.request.SignInRequest;
import com.unipi.pfatouros.eassist.model.request.SignUpRequest;
import com.unipi.pfatouros.eassist.model.response.ItemStats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Retrofit {

    // Auth calls

    // Return json web token if user's credentials are valid
    @POST("api/auth/sign_in")
    Call<String> signIn(@Body SignInRequest request);

    // Return True if user has employee role, False otherwise
    @GET("api/auth/employee/{username}")
    Call<Boolean> hasRoleEmployee(@Path("username") String username);

    // Return True if user has manager role, False otherwise
    @GET("api/auth/manager/{username}")
    Call<Boolean> hasRoleManager(@Path("username") String username);

    // Return True if user has admin role, False otherwise
    @GET("api/auth/admin/{username}")
    Call<Boolean> hasRoleAdmin(@Path("username") String username);

    // Admin calls

    // Return a list that contains all existing users
    @GET("/api/users")
    Call<List<User>> getUsers();

    // Delete user with given username (if he exists)
    @DELETE("/api/users/{username}")
    Call<Void> deleteUser(@Path("username") String username);

    // Create and register a new app user
    @POST("api/users/sign_up")
    Call<Void> signUpUser(@Body SignUpRequest request);

    // Manager calls

    // Get all inventory items
    @GET("/api/inventory")
    Call<List<Inventory>> getInventory();

    // Update existing inventory item
    @FormUrlEncoded
    @PUT("/api/inventory/{inventory_item_id}")
    Call<Void> updateInventory(@Path("inventory_item_id") Long inventory_item_id,
                               @Field("price") Float price, @Field("quantity") Integer quantity);

    // Get statistics based on all orders (regardless of date)
    @GET("/api/statistics")
    Call<List<ItemStats>> getStatistics();

    // Get statistics based on orders of specific date
    @GET("api/statistics/{date}")
    Call<List<ItemStats>> getStatistics(@Path("date") String date);

    // Employee calls

    // Get all pending orders of given date
    @GET("/api/orders/status/pending")
    Call<List<Order>> getPendingOrders(@Query("date") String date);

    // Get all orders of existing table
    @GET("/api/orders/table/{table_id}")
    Call<List<Order>> getOrdersOfTable(@Path("table_id") Long table_id,
                                       @Query("date") String date);

    // Update existing order's status
    @FormUrlEncoded
    @PUT("api/orders/{order_id}")
    Call<Void> updateOrder(@Path("order_id") Long order_id, @Field("status") String status);

    // Add order with items to system
    @POST("/api/orders/full")
    Call<Void> addOrder(@Body AddOrderRequest request);

    // Get all tables
    @GET("/api/tables")
    Call<List<Table>> getTables();

    // Update existing table's availability
    @FormUrlEncoded
    @PUT("api/tables/{table_id}")
    Call<Void> updateTable(@Path("table_id") Long table_id,
                           @Field("is_available") Boolean is_available);

    // Get all items of existing order
    @GET("/api/items/{order_id}")
    Call<List<Item>> getItemsOfOrder(@Path("order_id") Long order_id);
}
