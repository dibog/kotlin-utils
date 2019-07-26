package io.github.dibog.kutils


fun <T> Sequence<T>.doEffect(effect: (T) -> Unit): Sequence<T> {
    val iter = iterator()

    return sequence {
        iter.forEach {
            effect(it)
            yield(it)
        }
    }
}

fun <T> Sequence<T>.window(window: Long, trim: Boolean = false, f: (T)->Long): Sequence<List<T>> {
    val iter = iterator()

    // check that the values are monotone increasing
    return sequence {
        var nextTime = -1L
        lateinit var currentList : MutableList<T>

        iter.forEach {
            val itemTime = f(it)

            if (nextTime < 0) {
                nextTime = if(trim) (itemTime/window) * window else itemTime
                nextTime += window
                currentList = mutableListOf()
            }

            if (itemTime < nextTime) {
                currentList.add(it)
            } else {
                yield(currentList)
                currentList = mutableListOf()
                nextTime += window

                while (itemTime >= nextTime ) {
                    nextTime += window
                    yield(listOf())
                }
                currentList.add(it)
            }
        }

        if(currentList.isNotEmpty()) {
            yield(currentList)
        }
    }
}

fun <T> Iterator<T>.toList(): List<T> = mutableListOf<T>().also {
    while(hasNext()) {
        it.add(next())
    }
}
