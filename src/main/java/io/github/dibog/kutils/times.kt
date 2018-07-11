package io.github.dibog.kutils

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


inline fun Number.ms() = toLong()
inline fun Number.seconds() = 1000L*toLong()
inline fun Number.minutes() = 60L*1000L*toLong()
inline fun Number.hours() = 60L*60L*1000L*toLong()
inline fun Number.days() = 24L*60L*60L*1000L*toLong()
inline fun Number.weeks() = 7L*24L*60L*60L*1000L*toLong()
inline fun Number.years() = 365L*24L*60L*60L*1000L*toLong()

fun Long.toHumanDuration(): String {

    var time = this
    val ms = this % 1000L

    time /= 1000L
    val seconds = time % 60L

    time /= 60L
    val minutes = time % 60L

    time /= 60L
    val hours = time % 24L

    time /= 24L
    val days = time % 365L
    val day = days % 7

    val year = time / 365L

    val week = (time - (year*365L)) / 7L

    return when {
        year > 0 -> humanify(year, week, "y", "w")
        week > 0 -> humanify(week, day, "w", "d")
        days > 0 -> humanify(day, hours, "d", "h")
        hours > 0 -> humanify(hours, minutes, "h", "m")
        minutes > 0 -> humanify(minutes, seconds, "m", "s")
        seconds > 0 -> humanify(seconds, ms, "s", "ms")
        else -> "${ms}ms"
    }
}

internal fun humanify(majorValue: Long, minorValue: Long, majorUnit: String, minorUnit: String): String {
    return if(minorValue>0) {
        "$majorValue$majorUnit $minorValue$minorUnit"
    } else {
        "$majorValue$majorUnit"
    }
}

fun Duration.toHumanDuration() = toMillis().toHumanDuration()


fun Date.toLocalDateTime() = LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault())!!
fun Long.toLocalDateTime() = Date(this).toLocalDateTime()!!

fun Duration.toHumanString() = toMillis().toHumanDuration()

fun TimeWithUnit.toHumanString() = asMillis().toHumanDuration()

data class TimeWithUnit(val time: Long, val unit: TimeUnit) {
    fun asMillis() = unit.toMillis(time)

    operator fun compareTo(other: TimeWithUnit): Int {
        return asMillis().let { left ->
            other.asMillis().let { right ->
                when {
                    left == right -> 0
                    left < right -> -1
                    else -> 1
                }
            }
        }
    }

}

fun ScheduledExecutorService.scheduleAtFixedRate(period: TimeWithUnit, cmd: ()->Unit) =
        scheduleAtFixedRate(cmd, period.time, period.time, period.unit)

fun ScheduledExecutorService.scheduleWithFixedDelay(period: TimeWithUnit, cmd: ()->Unit) =
        scheduleWithFixedDelay(cmd, period.time, period.time, period.unit)

fun ScheduledExecutorService.scheduleAtFixedRate(initialDelay: Long, delay: Long, unit: TimeUnit, cmd: ()->Unit) =
        scheduleAtFixedRate(cmd, initialDelay, delay, unit)

fun ScheduledExecutorService.scheduleWithFixedDelay(initialDelay: Long, delay: Long, unit: TimeUnit, cmd: ()->Unit) =
        scheduleWithFixedDelay(cmd, initialDelay, delay, unit)


