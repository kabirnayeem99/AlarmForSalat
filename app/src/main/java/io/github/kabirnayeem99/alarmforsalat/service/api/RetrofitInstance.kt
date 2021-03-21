package io.github.kabirnayeem99.alarmforsalat.service.api


import android.util.Log
import dimitrovskif.smartcache.BasicCaching
import dimitrovskif.smartcache.SmartCallFactory
import io.github.kabirnayeem99.alarmforsalat.utils.App
import io.github.kabirnayeem99.alarmforsalat.utils.Constants
import io.github.kabirnayeem99.alarmforsalat.utils.Utilities
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInstance {

    companion object {
        private const val TAG = "RetrofitInstance"

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            Log.d(TAG, "retrofit_instance: will be set for cache size")


            //retrofit

            val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
            val cache = Cache(App.context.cacheDir, cacheSize)


            // retrofit
            var onlineInterceptor: Interceptor = Interceptor { chain ->
                val response = chain.proceed(chain.request())
                val maxAge =
                    60 // read from cache for 60 seconds even if there is internet connection
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .removeHeader("Pragma")
                    .build()
            }

            var offlineInterceptor = Interceptor { chain ->
                var request: Request = chain.request()
                if (!Utilities.isInternetAvailable(App.context)) {
                    val maxStale = 60 * 60 * 24 * 2 // Offline cache available for 2 days
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
                }
                chain.proceed(request)
            }
            //retrofit
            //retrofit

            val client = OkHttpClient.Builder()
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .cache(cache)
                .build().also {
                    Log.d(TAG, "retrofit_instance: retrofit will be started with $it")
                }
            Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }

        val api: AlAdhanApi by lazy {
            retrofit.create(AlAdhanApi::class.java)

        }

    }


}