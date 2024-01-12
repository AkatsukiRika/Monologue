package utils

import kotlinx.coroutines.flow.SharedFlow

suspend fun <T> SharedFlow<List<T>>.observeEffect(action: (T) -> Unit) {
    this.collect {
        it.forEach { event ->
            action.invoke(event)
        }
    }
}