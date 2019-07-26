package ut

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.github.dibog.kutils.FileResource
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

        it("exists") {
            assertThat(resource.exists()).isTrue()
        }

        it("has a file size") {
            assertThat(resource.size()).isEqualTo(123)
        }

        it("is a regular file") {
            assertThat(resource.isReadable()).isTrue()
        }

        it("is not a dolfer") {
            assertThat(resource.isDirectory()).isFalse()
        }
    }
})