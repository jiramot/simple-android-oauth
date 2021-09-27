package one.authz.app.model

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token")
    var accessToken: String,
    @SerializedName("expires_at")
    var expireAt: Long,
    @SerializedName("token_type")
    var tokenType: String
)