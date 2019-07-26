package io.github.dibog.kutils

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class FileResource(private val resource: Path)
{
    constructor(resource: File) : this(resource.toPath())

    fun exists() = Files.exists(resource)
    fun delete() = Files.delete(resource)
    fun deleteIfExists() = Files.deleteIfExists(resource)
    fun toPath() = resource
    fun toFile() = resource.toFile()
    fun toAbsolute() = FileResource(resource.toAbsolutePath())
    fun isSameFileAs(other: FileResource) = Files.isSameFile(resource, other.toPath())
    fun isReadable() = Files.isReadable(resource)
    fun isWriteable() = Files.isWritable(resource)
    fun isRegularFile() = Files.isRegularFile(resource)
    fun isDirectory() = Files.isDirectory(resource)
    fun isExecutable() = Files.isExecutable(resource)
    fun isHidden() = Files.isHidden(resource)
    fun isSameFileas(other: FileResource) = Files.isSameFile(resource, other.toPath())
    fun isSymbolicLink() = Files.isSymbolicLink(resource)
    fun newBufferedReader() = Files.newBufferedReader(resource)
    fun newBufferedWriter() = Files.newBufferedWriter(resource)
    fun newInputStream() = Files.newInputStream(resource)
    fun lastModified() = Files.getLastModifiedTime(resource)
    fun copyInto(destination: OutputStream) = destination.use { Files.copy(resource, it) }
    fun copyFrom(source: InputStream) = source.use { Files.copy(it, resource, StandardCopyOption.REPLACE_EXISTING) }
    fun size() = Files.size(resource)

    fun <R> useAndDelete(block: (FileResource)->R) : R {
        try {
            return block(this)
        }
        catch(e: Exception) {
            try {
                delete()
            }
            catch(e2: Exception) {
                e.addSuppressed(e2)
            }
            throw e
        }
    }

    override fun toString(): String {
        return resource.toString()
    }

    override fun equals(other: Any?): Boolean {
        if(other is FileResource) {
            return isSameFileAs(other)
        }
        return false
    }

    override fun hashCode() = resource.hashCode()
}

fun File.toFileResource(): FileResource = FileResource(this)
fun Path.toFileResource(): FileResource = FileResource(this)

