package com.walkmansit.realworld.data.repository

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.toLocal
import com.walkmansit.realworld.data.toNetworkRequest
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.util.Either
import com.walkmansit.realworld.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException


class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(userCredentials: UserLoginCredentials): Either<LoginFailed, User> {
        return try {
            val response = apiService.loginUser(userCredentials.toNetworkRequest())
            Either.success(response.toLocal())
        } catch (e : IOException){
            Either.fail(LoginFailed(commonError = e.message.orEmpty()))
        }  catch (e : HttpException){
            //TODO map response error to domain error
            Either.fail(LoginFailed(commonError = e.message.orEmpty()))
        }
    }

    override suspend fun register(userCredentials: UserRegisterCredentials): Either<RegistrationFailed, User> {
        return try {
            val response = apiService.registerUser(userCredentials.toNetworkRequest())
            Either.success(response.toLocal())
        } catch (e : IOException){
            Either.fail(RegistrationFailed(commonError = e.message.orEmpty()))
        }  catch (e : HttpException){
            //e.response().errorBody()?.string()
            val errorResponse: ErrorResponse = Gson().fromJson(
                e.response()?.errorBody()?.string() ?: "", ErrorResponse::class.java
            )
            Either.fail(RegistrationFailed(commonError = e.message.orEmpty()))
        }

    }
}

data class UserAuthRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)

data class ErrorResponse(
    val status: String,
    @SerializedName("status_code")
    val statusCode: String,
    val type: String,
    val message: String,
)