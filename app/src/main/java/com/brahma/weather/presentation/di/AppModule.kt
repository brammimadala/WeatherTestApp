package com.brahma.weather.presentation.di

import com.brahma.weather.data.api.WeatherApiService
import com.brahma.weather.data.repository.WeatherReportRepositoryImpl
import com.brahma.weather.data.repository.datasource.WeatherReportRemoteDataSource
import com.brahma.weather.data.repository.datasourceImpl.WeatherReportRemoteDataSourceImpl
import com.brahma.weather.domain.repository.WeatherReportRepository
import com.brahma.weather.domain.usecase.GetCurrentWeatherUseCase
import com.brahma.weather.domain.usecase.GetForecastWeatherUseCase
import com.brahma.weather.presentation.viewModel.WeatherViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder()
        return builder.addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/")
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideWeatherReportRemoteDataSource(weatherApiService: WeatherApiService): WeatherReportRemoteDataSource {
        return WeatherReportRemoteDataSourceImpl(weatherApiService)
    }

    @Singleton
    @Provides
    fun provideWeatherReportRepository(weatherReportRemoteDataSource: WeatherReportRemoteDataSource): WeatherReportRepository {
        return WeatherReportRepositoryImpl(weatherReportRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideGetCurrentWeatherUseCase(weatherReportRepository: WeatherReportRepository): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(weatherReportRepository)
    }

    @Singleton
    @Provides
    fun provideGetForecastWeatherUseCase(weatherReportRepository: WeatherReportRepository): GetForecastWeatherUseCase {
        return GetForecastWeatherUseCase(weatherReportRepository)
    }

    @Singleton
    @Provides
    fun provideWeatherViewModelFactory(
        getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
        getForecastWeatherUseCase: GetForecastWeatherUseCase
    ): WeatherViewModelFactory {
        return WeatherViewModelFactory(getCurrentWeatherUseCase, getForecastWeatherUseCase)
    }


}