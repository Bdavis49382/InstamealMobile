package com.instamealmobile.network

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

class FirebaseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val idToken = runBlocking { user.getIdToken(true).await().token }
            if (idToken != null ){
                return chain.proceed(chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $idToken")
                    .build()
                )
            }
        }
        return chain.proceed(chain.request())
    }
}