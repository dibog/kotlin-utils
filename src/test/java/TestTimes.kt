
import assertk.assert
import assertk.assertAll
import assertk.assertions.isEqualTo
import io.github.dibog.kutils.days
import io.github.dibog.kutils.hours
import io.github.dibog.kutils.minutes
import io.github.dibog.kutils.ms
import io.github.dibog.kutils.seconds
import io.github.dibog.kutils.toHumanDuration
import io.github.dibog.kutils.weeks
import io.github.dibog.kutils.years
import org.junit.Test
import java.time.Duration

class TestTimes {

    @Test fun testTimeUnits() {

        assertAll {
            assert(0.ms(), "0ms").isEqualTo(0L)
            assert(1.ms(), "1ms").isEqualTo(1L)
            assert(1.seconds(), "1s").isEqualTo(1000L)

            assert(1.seconds() + 5.ms(), "1s 5ms").isEqualTo(1005L)

            assert(2.minutes(),"2m").isEqualTo(2L * 60L * 1000L)
            assert(2.hours(), "2h").isEqualTo(2L * 60L * 60L * 1000L)
            assert(2.days(), "2d").isEqualTo(2L * 24L * 60L * 60L * 1000L)
            assert(2.weeks(), "2w").isEqualTo(2L * 7L * 24L * 60L * 60L * 1000L)
            assert(2.years(), "2y").isEqualTo(2L * 365L * 24L * 60L * 60L * 1000L)

            assert(2.years() + 3.weeks() + 10.hours(), "2y 3w 10h").isEqualTo(
                    2L * 365L * 24L * 60L * 60L * 1000L +
                            3L * 7L * 24L * 60L * 60L * 1000L +
                            10L * 60L * 60L * 1000L
            )
        }
    }

    @Test fun testHumanDuration() {
        assertAll {

            assert( (0.ms()).toHumanDuration(), "0ms").isEqualTo("0ms")
            assert( (999.ms()).toHumanDuration(), "999ms").isEqualTo("999ms")

            assert( (1.seconds()).toHumanDuration(), "1s").isEqualTo("1s")
            assert( (1.seconds()+1.ms()).toHumanDuration(), "1s 1ms" ).isEqualTo("1s 1ms")

            assert( (1.minutes()+1.seconds()).toHumanDuration(), "1m 1s").isEqualTo("1m 1s")
            assert( (1.minutes()+1.ms()).toHumanDuration(), "1m 1ms").isEqualTo("1m")
            assert( (1.minutes()+1.seconds()+1.ms()).toHumanDuration(), "1m 1s 1ms").isEqualTo("1m 1s")
            assert( (2.minutes()+1.ms()).toHumanDuration(), "2m 1ms").isEqualTo("2m")

            assert( (2.hours()).toHumanDuration(), "2h").isEqualTo("2h")
            assert( (2.hours()+2.seconds()).toHumanDuration(), "2h 2s").isEqualTo("2h")
            assert( (3.hours()+4.minutes()).toHumanDuration(), "3h 4m").isEqualTo("3h 4m")
            assert( (1.hours()+10.minutes()+9.seconds()).toHumanDuration(), "1h 10m 9s").isEqualTo("1h 10m")

            assert( (2.days()).toHumanDuration(), "2d").isEqualTo("2d")
            assert( (2.days()+3.minutes()).toHumanDuration(), "2d 3m").isEqualTo("2d")
            assert( (3.days()+10.hours()).toHumanDuration(), "3d 10h").isEqualTo("3d 10h")
            assert( (4.days()+12.hours()+3.seconds()).toHumanDuration(), "4d 12h 3s").isEqualTo("4d 12h")
            assert( (2.days()+25.hours()+61.seconds()).toHumanDuration(), "2d 25h 61s").isEqualTo("3d 1h")

            assert( (7.days()).toHumanDuration(), "7d").isEqualTo("1w")
            assert( (15.days()).toHumanDuration(), "15d").isEqualTo("2w 1d")
            assert( (15.days()+10.hours()).toHumanDuration(), "15d 10h").isEqualTo("2w 1d")

            assert( (2.years()).toHumanDuration(), "2y").isEqualTo("2y")
            assert( (365.days()).toHumanDuration(), "365d").isEqualTo("1y")
            assert( (366.days()).toHumanDuration(), "366d").isEqualTo("1y")
            assert( (372.days()).toHumanDuration(), "372d").isEqualTo("1y 1w")
            assert( (372.days()+5.seconds()).toHumanDuration(), "372d").isEqualTo("1y 1w")

        }
    }

    @Test fun testDuration() {
        assertAll {
            assert((Duration.ofDays(5) + Duration.ofHours(12)).toHumanDuration(), "5d 12h")
                    .isEqualTo("5d 12h")

            assert((Duration.ofDays(15) + Duration.ofHours(3)).toHumanDuration(), "15d 3h")
                    .isEqualTo("2w 1d")
        }
    }


}