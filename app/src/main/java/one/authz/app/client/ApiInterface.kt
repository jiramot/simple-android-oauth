package one.authz.app.client

import one.authz.app.model.AccessToken
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("token")
    fun token(@Body body: TokenRequest): Call<AccessToken>

    companion object {
        const val BASE_URL = "https://api.authz.one/"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }

}