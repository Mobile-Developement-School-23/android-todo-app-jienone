package com.example.yandextodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

class MainActivity : AppCompatActivity() {
    private lateinit var sdk: YandexAuthSdk
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sdk = YandexAuthSdk(this, YandexAuthOptions(this))

        val loginOptionsBuilder = YandexAuthLoginOptions.Builder()
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())

        loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                try {
                    val yandexAuthToken = sdk.extractToken(resultCode, data)
                    if (yandexAuthToken != null) {
                        Log.d("AUTH_TEST", "success")
                        setContentView(R.layout.activity_main)
                    }
                } catch (e: YandexAuthException) {
                    Log.e("AUTH_TEST", e.toString())
                    setContentView(R.layout.activity_main)
                }
            }
        }
        loginLauncher.launch(intent)
    }

}