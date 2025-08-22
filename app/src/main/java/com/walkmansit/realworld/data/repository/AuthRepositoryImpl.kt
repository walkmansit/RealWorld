package com.walkmansit.realworld.data.repository

import com.walkmansit.realworld.data.remote.ApiService
import com.walkmansit.realworld.data.remote.response.LoginFailedResponse
import com.walkmansit.realworld.data.remote.response.RegistrationFailedResponse
import com.walkmansit.realworld.data.repository.NetworkErrorMessages.SERVICE_UNAVAILABLE
import com.walkmansit.realworld.data.util.ModelsMapper
import com.walkmansit.realworld.data.util.getErrorEither
import com.walkmansit.realworld.data.util.toDomain
import com.walkmansit.realworld.data.util.toLoginFailed
import com.walkmansit.realworld.data.util.toNetworkRequest
import com.walkmansit.realworld.data.util.toRegistrationFailed
import com.walkmansit.realworld.domain.model.CommonError
import com.walkmansit.realworld.domain.model.LoginFailed
import com.walkmansit.realworld.domain.model.Profile
import com.walkmansit.realworld.domain.model.RegistrationFailed
import com.walkmansit.realworld.domain.model.RequestFailed
import com.walkmansit.realworld.domain.model.User
import com.walkmansit.realworld.domain.model.UserLoginCredentials
import com.walkmansit.realworld.domain.model.UserRegisterCredentials
import com.walkmansit.realworld.domain.repository.AuthRepository
import com.walkmansit.realworld.domain.util.Either
import retrofit2.HttpException
import java.io.IOException

object NetworkErrorMessages {
    const val SERVICE_UNAVAILABLE = "Service unavailable"
}

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun login(userCredentials: UserLoginCredentials): Either<Either<CommonError,LoginFailed>, User> {
        return try {
            val response = apiService.loginUser(userCredentials.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(Either.fail(CommonError(SERVICE_UNAVAILABLE)))
        }  catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string() ?: ""
            val mapper = object : ModelsMapper<LoginFailedResponse, LoginFailed> {
                override fun map(data: LoginFailedResponse): LoginFailed {
                    return data.toLoginFailed()
                }
            }
            return Either.fail(getErrorEither<LoginFailedResponse, LoginFailed>(body, mapper))
        }
    }

    override suspend fun register(userCredentials: UserRegisterCredentials): Either<Either<CommonError,RegistrationFailed>, User> {
        return try {
            val response = apiService.registerUser(userCredentials.toNetworkRequest())
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(Either.fail(CommonError( SERVICE_UNAVAILABLE)))
        }  catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string() ?: ""
            val mapper = object : ModelsMapper<RegistrationFailedResponse, RegistrationFailed> {
                    override fun map(data: RegistrationFailedResponse): RegistrationFailed {
                        return data.toRegistrationFailed()
                    }
                }
            return Either.fail(getErrorEither<RegistrationFailedResponse, RegistrationFailed>(body, mapper))
//            val errorResponse = getErrorResponse<RegistrationErrorResponse>(e.response()?.errorBody()?.string() ?: "")
//            Either.fail(Either.success(errorResponse.toRegistrationFailed()))
        }

    }

    override suspend fun getProfile(username: String): Either<RequestFailed, Profile> {
        return try {
            val response = apiService.getProfile(username)
            Either.success(response.toDomain())
        } catch (e: IOException) {
            Either.fail(RequestFailed(commonError = SERVICE_UNAVAILABLE))
        } catch (e: HttpException) {
            Either.fail(RequestFailed(commonError = e.message.orEmpty()))
        }
    }
}