package io.github.dibog.kutils

interface TypedFeature<T: Any> {
    fun update(block: (T?, current: T)->Unit)
}

interface FeatureFactory {
    companion object {
        fun getFeature(featureName: String): TypedFeature<Boolean> {
            TODO()
        }

        fun getStringFeature(featureName: String): TypedFeature<String> {
            TODO()
        }
    }
}

class FeatureFactoryImpl {
    fun getFeature(featureName: String): TypedFeature<Boolean> {
        TODO()
    }

    fun getStringFeature(featureName: String): TypedFeature<String> {
        TODO()
    }
}

fun main() {
    FeatureFactory.getFeature("Foo").update { prev, current ->

    }

    FeatureFactory.getStringFeature("FooString").update { prev, current ->

    }

    Thread.sleep(100_000L)
}