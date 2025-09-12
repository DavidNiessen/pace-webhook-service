package dev.niessen.webhookservice.utils

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat

class Counter(
    internal val expected: Int,
) {

    var value: Int = 0
        private set

    fun increment() = ++value
    fun decrement() = --value

    fun assert() = assertCounter(this)
}

fun <T> assertWithCounter(actual: T, matcher: Matcher<in T>, counter: Counter) {
    assertThat(actual, matcher)
    counter.increment()
}

fun assertCounter(counter: Counter) {
    assertThat(counter.value, `is`(counter.expected))
}
