package com.munin.music.net

import com.munin.library.thread.NetExecutor
import com.munin.music.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class ApiClient private constructor() {

    lateinit var retrofit: Retrofit
    lateinit var service: IApiService


    private fun init() {
        val trustAllManager = TrustAllManager()
        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .sslSocketFactory(createTrustAllSSLFactory(trustAllManager), trustAllManager)
                .hostnameVerifier(HostVerifier())
                .build()

        retrofit = Retrofit.Builder().client(client)
                .baseUrl(BuildConfig.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(NetExecutor.newInstance().executor)
                .build()
        service = retrofit!!.create(IApiService::class.java)
    }

    public class HostVerifier() : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }

    private object Holder {
        val INSTANCE = ApiClient()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
    }

    init {
        init()
    }

    fun createTrustAllSSLFactory(trustAllManager: TrustAllManager): SSLSocketFactory {
        var ssfFactory: SSLSocketFactory
        var sc = SSLContext.getInstance("TLS");
        sc.init(null, arrayOf(trustAllManager), SecureRandom())
        ssfFactory = sc.getSocketFactory()

        return ssfFactory;
    }

}

