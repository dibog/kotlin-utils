
import assertk.assertAll
import assertk.assertThat
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
            assertThat(0.ms(), "0ms").isEqualTo(0L)
            assertThat(1.ms(), "1ms").isEqualTo(1L)
            assertThat(1.seconds(), "1s").isEqualTo(1000L)

            assertThat(1.seconds() + 5.ms(), "1s 5ms").isEqualTo(1005L)

            assertThat(2.minutes(),"2m").isEqualTo(2L * 60L * 1000L)
            assertThat(2.hours(), "2h").isEqualTo(2L * 60L * 60L * 1000L)
            assertThat(2.days(), "2d").isEqualTo(2L * 24L * 60L * 60L * 1000L)
            assertThat(2.weeks(), "2w").isEqualTo(2L * 7L * 24L * 60L * 60L * 1000L)
            assertThat(2.years(), "2y").isEqualTo(2L * 365L * 24L * 60L * 60L * 1000L)

            assertThat(2.years() + 3.weeks() + 10.hours(), "2y 3w 10h").isEqualTo(
                    2L * 365L * 24L * 60L * 60L * 1000L +
                            3L * 7L * 24L * 60L * 60L * 1000L +
                            10L * 60L * 60L * 1000L
            )
        }
    }

    @Test fun testHumanDuration() {
        assertAll {

            assertThat( (0.ms()).toHumanDuration(), "0ms").isEqualTo("0ms")
            assertThat( (999.ms()).toHumanDuration(), "999ms").isEqualTo("999ms")

            assertThat( (1.seconds()).toHumanDuration(), "1s").isEqualTo("1s")
            assertThat( (1.seconds()+1.ms()).toHumanDuration(), "1s 1ms" ).isEqualTo("1s 1ms")

            assertThat( (1.minutes()+1.seconds()).toHumanDuration(), "1m 1s").isEqualTo("1m 1s")
            assertThat( (1.minutes()+1.ms()).toHumanDuration(), "1m 1ms").isEqualTo("1m")
            assertThat( (1.minutes()+1.seconds()+1.ms()).toHumanDuration(), "1m 1s 1ms").isEqualTo("1m 1s")
            assertThat( (2.minutes()+1.ms()).toHumanDuration(), "2m 1ms").isEqualTo("2m")

            assertThat( (2.hours()).toHumanDuration(), "2h").isEqualTo("2h")
            assertThat( (2.hours()+2.seconds()).toHumanDuration(), "2h 2s").isEqualTo("2h")
            assertThat( (3.hours()+4.minutes()).toHumanDuration(), "3h 4m").isEqualTo("3h 4m")
            assertThat( (1.hours()+10.minutes()+9.seconds()).toHumanDuration(), "1h 10m 9s").isEqualTo("1h 10m")

            assertThat( (2.days()).toHumanDuration(), "2d").isEqualTo("2d")
            assertThat( (2.days()+3.minutes()).toHumanDuration(), "2d 3m").isEqualTo("2d")
            assertThat( (3.days()+10.hours()).toHumanDuration(), "3d 10h").isEqualTo("3d 10h")
            assertThat( (4.days()+12.hours()+3.seconds()).toHumanDuration(), "4d 12h 3s").isEqualTo("4d 12h")
            assertThat( (2.days()+25.hours()+61.seconds()).toHumanDuration(), "2d 25h 61s").isEqualTo("3d 1h")

            assertThat( (7.days()).toHumanDuration(), "7d").isEqualTo("1w")
            assertThat( (15.days()).toHumanDuration(), "15d").isEqualTo("2w 1d")
            assertThat( (15.days()+10.hours()).toHumanDuration(), "15d 10h").isEqualTo("2w 1d")

            assertThat( (2.years()).toHumanDuration(), "2y").isEqualTo("2y")
            assertThat( (365.days()).toHumanDuration(), "365d").isEqualTo("1y")
            assertThat( (366.days()).toHumanDuration(), "366d").isEqualTo("1y")
            assertThat( (372.days()).toHumanDuration(), "372d").isEqualTo("1y 1w")
            assertThat( (372.days()+5.seconds()).toHumanDuration(), "372d").isEqualTo("1y 1w")

        }
    }

    @Test fun testDuration() {
        assertAll {
            assertThat((Duration.ofDays(5) + Duration.ofHours(12)).toHumanDuration(), "5d 12h")
                    .isEqualTo("5d 12h")

            assertThat((Duration.ofDays(15) + Duration.ofHours(3)).toHumanDuration(), "15d 3h")
                    .isEqualTo("2w 1d")
        }
    }


}