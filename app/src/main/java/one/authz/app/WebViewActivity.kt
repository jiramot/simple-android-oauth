package one.authz.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import one.authz.app.client.ApiInterface
import one.authz.app.client.TokenRequest
import one.authz.app.model.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebViewActivity : Activity() {

    companion object {
        const val TAG = "TAG"
        const val APP = "AUTHZ"
        const val APP_VERSION = "1.0"
    }

    lateinit var mActivity: Activity
    lateinit var myWebView: WebView
    lateinit var clientId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        mActivity = this
        clientId = when {
            intent.data != null -> intent.data!!.pathSegments[0]
            intent.getStringExtra("data") != null -> {
                intent.getStringExtra("data")!!.replace("https://app.authz.one/", "")
            }
            else -> "1234"
        }
        myWebView = findViewById(R.id.webView)
        val settings = myWebView.settings
        val defaultUserAgent = settings.userAgentString
        settings.javaScriptEnabled = true
        settings.userAgentString = "$defaultUserAgent $APP/$APP_VERSION $APP"
        settings.domStorageEnabled = true
        myWebView.webViewClient = SimpleWebViewClient(mActivity)
        getAccessTokenFrom("1234", clientId)

    }

    private fun getAccessTokenFrom(cif: String, clientId: String) {
        val tokenApi = ApiInterface.create().token(TokenRequest(cif, clientId))
        tokenApi.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                val accessToken = response.body()?.accessToken
                val fragment = "#access_token=${accessToken}"
                myWebView.loadUrl("https://app.authz.one/${clientId}${fragment}")
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
            }
        })
    }

    class SimpleWebViewClient(val mActivity: Activity) : WebViewClient() {
        override fun onLoadResource(view: WebView?, url: String?) {
            Log.i(TAG, "onLoadResource $url")
            super.onLoadResource(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.i(TAG, "shouldOverrideUrlLoading $url")
            if (url.contains("app.authz.one")) {
                val intent = Intent(mActivity, WebViewActivity::class.java)
                intent.putExtra("data", url)
                mActivity.startActivity(intent)
                return true
            }
            return false
        }
    }
}

