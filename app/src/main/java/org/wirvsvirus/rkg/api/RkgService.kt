package org.wirvsvirus.rkg.api

import org.wirvsvirus.rkg.model.*
import retrofit2.Call
import retrofit2.http.*

interface RkgService {

    // Vendor
    @POST("vendors/signup")
    fun vendorSignUp(@Body vendorSignup: VendorSignup): Call<Vendor>

    @GET("vendors/verify/{verifyCode}")
    fun vendorVerify(@Path("verifyCode") verifyCode: String): Call<Void>

    @GET("vendors/login")
    fun vendorLogIn(@Query("mail") mail: String, @Query("password") password: String) // TODO return?

    // Store
    @GET("stores")
    fun getAllStores(): Call<List<Store>>

    @POST("stores")
    fun createStore(@Body store: Store): Call<Void>

    @PATCH("stores/{storeId}")
    fun updateStore(@Path("storeId") storeId: String, @Body store: Store): Call<Void>

    @DELETE("stores/{storeId}")
    fun deleteStore(@Path("storeId") storeId: String): Call<Void>

    // Products
    @GET("stores/{storeId}/products")
    fun getProducts(@Path("storeId") storeId: String): Call<List<Product>>

    @POST("stores/{storeId}/products")
    fun addProduct(@Path("storeId") storeId: String, @Body product: Product): Call<Void>

    @PATCH("stores/{storeId}/products/{productId}")
    fun updateProduct(@Path("storeId") storeId: String, @Path("productId") productId: String, @Body product: Product): Call<Void>

    @DELETE("stores/{storeId}/products/{productId}")
    fun deleteProduct(@Path("storeId") storeId: String, @Path("productId") productId: String): Call<Void>

    // Orders
    @GET("orders")
    fun getAllOrders(): Call<List<Order>>

    @GET("orders/{orderId}")
    fun getOrder(@Path("orderId") orderId: String): Call<OrderWithProducts>

}