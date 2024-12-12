package app.culturedev.cultureconnect.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import app.culturedev.cultureconnect.data.remote.api.RecommendationApiService
import app.culturedev.cultureconnect.data.response.recommendation.RecommendationRequest
import app.culturedev.cultureconnect.data.response.recommendation.RecommendationResponse
import app.culturedev.cultureconnect.data.result.ResultCafe
import retrofit2.HttpException

class RecommendationRepository(private val recommendationApiService: RecommendationApiService) {
    fun sendUserMood(userMood: String): LiveData<ResultCafe<RecommendationResponse>> =
        liveData {
            emit(ResultCafe.Loading)
            try {
                val request = RecommendationRequest(userMood)
                val response = recommendationApiService.getRecommendation(request)
                emit(ResultCafe.Success(response))
            } catch (e: HttpException) {
                val errorMessage = "Error send user mood data"
                emit(ResultCafe.Error(errorMessage))
            }
        }

    companion object {
        @Volatile
        private var instance: RecommendationRepository? = null
        fun getInstance(
            apiService: RecommendationApiService
        ): RecommendationRepository =
            instance ?: synchronized(this) {
                instance ?: RecommendationRepository(apiService)
            }.also { instance = it }
    }
}