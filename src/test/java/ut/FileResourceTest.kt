package ut

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.github.dibog.kutils.FileResource
import io.github.dibog.spec.extension.test
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayInputStream
import java.nio.file.Files

object FileResourceSpec : Spek({

    describe("An existing file FileResource") {
        lateinit var resource: FileResource

        before {
            val tmp = Files.createTempFile("spek",".test")
            resource = FileResource(tmp)
            val bytes = """
                demo
                file
            """.trimIndent().toByteArray(Charsets.UTF_8)

            resource.copyFrom(ByteArrayInputStream(bytes))
        }

        after {
            resource.delete()
        }

        test("exists") {
            assertThat(resource.exists()).isTrue()
        }

        test("has a file size") {
            assertThat(resource.size()).isEqualTo(123)
        }

        test("is a regular file") {
            assertThat(resource.isReadable()).isTrue()
        }

        test("is not a dolfer") {
            assertThat(resource.isDirectory()).isFalse()
        }
    }
})