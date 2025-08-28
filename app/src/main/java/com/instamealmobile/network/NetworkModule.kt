package com.instamealmobile.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.GsonBuilder
import com.instamealmobile.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cache = Cache(File(context.cacheDir, "http_cache"), 10 * 1024 * 1024) // 10 mb space
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                if (!hasInternetConnection(context)) { // Without internet, don't attempt to add authorization token.
                    chain.proceed(chain.request())
                }
                else {
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        val idToken = runBlocking { user.getIdToken(true).await().token }
                        if (idToken != null ){
                            chain.proceed(chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer $idToken")
                                .build()
                            )
                        } else {
                            chain.proceed(chain.request())
                        }
                    } else {
                        Log.e("AUTH","An Unauthorized request was just attempted.")
                        chain.proceed(chain.request())
                    }

                }
            }
            .addInterceptor { chain ->
                var request = chain.request()
                val hasOwnCache = request.header("Cache-Control") != null
                request = if (hasInternetConnection(context)) {
                    if (hasOwnCache) {
                        request
                    }
                    else {
                        request.newBuilder()
                            .header("Cache-Control","public, max-age=60") // 1 minute cache
                            .build()
                    }
                } else {
                    request.newBuilder()
                        .header("Cache-Control","public, only-if-cached, max-stale=" + 60 * 60 * 24) // 1 day cache if offline
                        .build()
                }
                chain.proceed(request)
            }
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()
                response.newBuilder()
                    .header("Cache-Control", chain.request().header("Cache-Control")?: cacheControl.toString())
                    .build()
            }
            .build()
    }

    fun hasInternetConnection(context: Context): Boolean {
        // Check for a network connection
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        // Check if that network connection provides internet
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideShoppingListService(retrofit: Retrofit): ShoppingListService {
        return retrofit.create(ShoppingListService::class.java)
    }
    @Provides
    @Singleton
    fun provideFeedService(retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }
    @Provides
    @Singleton
    fun provideMenuService(retrofit: Retrofit): MenuService {
        return retrofit.create(MenuService::class.java)
    }
    @Provides
    @Singleton
    fun provideHouseholdService(retrofit: Retrofit): HouseholdService {
        return retrofit.create(HouseholdService::class.java)
    }
}
