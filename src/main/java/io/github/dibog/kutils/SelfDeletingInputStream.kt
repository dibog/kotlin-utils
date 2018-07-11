package io.github.dibog.kutils

import mu.KLogging
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path

class SelfDeletingInputStream(val out: InputStream, val onDelete: Runnable): InputStream() {

    companion object : KLogging() {
        @JvmStatic operator fun invoke(path: Path) = SelfDeletingInputStream(path.newInputStream(), Runnable {
            try { path.delete() }
            catch(e: IOException) { logger.error { "Could not delete stream for stream '${path.toAbsolutePath()}'" } }
        })

        @JvmStatic operator fun invoke(file: File) = invoke(file.toPath())
    }

    override fun skip(n: Long) = out.skip(n)
    override fun available() = out.available()
    override fun reset() = out.reset()
    override fun mark(readlimit: Int) = out.mark(readlimit)
    override fun markSupported() = out.markSupported()
    override fun read() = out.read()
    override fun read(b: ByteArray) = out.read(b)
    override fun read(b: ByteArray, off: Int, len: Int) = out.read(b,off,len)

    override fun close() {
        try { super.close() } finally { onDelete.run() }
    }
}