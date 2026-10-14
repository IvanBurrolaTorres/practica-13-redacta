package mx.tec.redacta.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.tec.redacta.data.GemmaLocal
import mx.tec.redacta.data.ModeloLocal

@Module
@InstallIn(SingletonComponent::class)
abstract class ModeloModule {
    @Binds
    abstract fun modelo(gemma: GemmaLocal): ModeloLocal
}
