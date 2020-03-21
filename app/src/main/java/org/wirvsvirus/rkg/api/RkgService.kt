package org.wirvsvirus.rkg.api

import org.wirvsvirus.rkg.model.Product
import org.wirvsvirus.rkg.model.Store
import org.wirvsvirus.rkg.model.VendorSignup
import retrofit2.Call
import retrofit2.http.*

interface RkgService {

    // Vendor
    @POST("vendors/signup")
    fun vendorSignUp(@Body vendorSignup: VendorSignup): Call<String>

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
    fun updateStore(@Path("storeId") storeId: Int, @Body store: Store): Call<Void>

    @DELETE("stores/{storeId}")
    fun deleteStore(@Path("storeId") storeId: Int): Call<Void>

    // Products
    @GET("stores/{storeId}/products")
    fun getProducts(@Path("storeId") storeId: Int): Call<List<Product>>

    @POST("stores/{storeId}/products")
    fun addProduct(@Path("storeId") storeId: Int, @Body product: Product): Call<Void>

    @PATCH("stores/{storeId}/products/{productId}")
    fun updateProduct(@Path("storeId") storeId: Int, @Path("productId") productId: Int, @Body product: Product): Call<Void>

    @DELETE("stores/{storeId}/products/{productId}")
    fun deleteProduct(@Path("storeId") storeId: Int, @Path("productId") productId: Int): Call<Void>

}