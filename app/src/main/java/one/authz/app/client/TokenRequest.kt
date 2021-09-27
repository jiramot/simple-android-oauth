package one.authz.app.client

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    val cif: String,
    @SerializedName("client_id")
    val clientId: String,
    val amr: String = "app"
)