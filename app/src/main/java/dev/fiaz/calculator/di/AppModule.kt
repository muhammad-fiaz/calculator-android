package dev.fiaz.calculator.di

import android.content.Context
import androidx.room.Room
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dev.fiaz.calculator.data.local.CalculatorDatabase
import dev.fiaz.calculator.data.preferences.SettingsDataStore
import dev.fiaz.calculator.data.repository.AppSettingsRepositoryImpl
import dev.fiaz.calculator.data.repository.CalculationHistoryRepositoryImpl
import dev.fiaz.calculator.data.repository.UnitConverterRepositoryImpl
import dev.fiaz.calculator.domain.engine.CalculatorExpressionEvaluator
import dev.fiaz.calculator.domain.repository.AppSettingsRepository
import dev.fiaz.calculator.domain.repository.CalculationHistoryRepository
import dev.fiaz.calculator.domain.repository.UnitConverterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: CalculationHistoryRepositoryImpl): CalculationHistoryRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: AppSettingsRepositoryImpl): AppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindUnitConverterRepository(impl: UnitConverterRepositoryImpl): UnitConverterRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CalculatorDatabase =
        Room.databaseBuilder(context, CalculatorDatabase::class.java, "calculator_pro.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideHistoryDao(database: CalculatorDatabase) = database.calculationHistoryDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore = SettingsDataStore(context)

    @Provides
    @Singleton
    fun provideEvaluator(): CalculatorExpressionEvaluator = CalculatorExpressionEvaluator()

    @Provides
    @Singleton
    fun provideReviewManager(@ApplicationContext context: Context): ReviewManager =
        ReviewManagerFactory.create(context)

    @Provides
    @Singleton
    fun provideAppUpdateManager(@ApplicationContext context: Context): AppUpdateManager =
        AppUpdateManagerFactory.create(context)
}
