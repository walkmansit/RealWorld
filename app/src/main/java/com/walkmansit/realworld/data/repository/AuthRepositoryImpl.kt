package com.walkmansit.realworld.data.repository

import com.google.gson.Gson
import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.remote.response.LoginErrorResponse
import com.walkmansit.realworld.data.remote.response.RegistrationErrorResponse
import com.walkmansit.realworld.data.util.getErrorResponse
import com.walkmansit.realworld.data.util.toDomain
import com.walkmansit.realworld.data.util.toLoginFailed
import com.walkmansit.realworld.data.util.toNetworkRequest
import com.walkmansit.realworld.data.util.toRegistrationFailed
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.Profile
import com.walkmansit.realworld.domain.model.ProfileFailed
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.util.Either
import retrofit2.HttpException
import java.io.IOException


class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(userCredentials: UserLoginCredentials): Either<LoginFailed, User> {
        return try {
            val response = apiService.loginUser(userCredentials.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(LoginFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            val errorResponse = getErrorResponse<LoginErrorResponse>(e.response()?.errorBody()?.string() ?: "")
            Either.fail(errorResponse.toLoginFailed())
        }
    }

    override suspend fun register(userCredentials: UserRegisterCredentials): Either<RegistrationFailed, User> {
        return try {
            val response = apiService.registerUser(userCredentials.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RegistrationFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            val errorResponse = getErrorResponse<RegistrationErrorResponse>(e.response()?.errorBody()?.string() ?: "")
            Either.fail(errorResponse.toRegistrationFailed())
        }

    }

    override suspend fun getProfile(username: String): Either<ProfileFailed, Profile> {
        return try {
            val response = apiService.getProfile(username)
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(ProfileFailed(commonError = e.message.orEmpty()))
        } catch (e: HttpException) {
            Either.fail(ProfileFailed(commonError = e.message.orEmpty()))
        }
    }

}