package io.github.dibog.kutils

import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

fun Path.exists(vararg options: LinkOption) = Files.exists(this, *options)
fun File.exists(vararg options: LinkOption) = Files.exists(toPath(), *options)

fun Path.isRegularFile(vararg options: LinkOption) = Files.isRegularFile(this, *options)
fun File.isregularFile(vararg options: LinkOption) = Files.isRegularFile(toPath(), *options)

fun Path.isDirectory(vararg options: LinkOption) = Files.isDirectory(this, *options)
fun File.isDirectory(vararg options: LinkOption) = Files.isDirectory(toPath(), *options)

fun Path.isSymbolicLink() = Files.isSymbolicLink(this)
fun File.isSymbolicLink() = Files.isSymbolicLink(toPath())

fun Path.newInputStream(vararg options: OpenOption) = Files.newInputStream(this, *options)
fun File.newInputStream(vararg options: OpenOption) = Files.newInputStream(toPath(), *options)

fun Path.newOutputStream(vararg options: OpenOption) = Files.newOutputStream(this, *options)
fun File.newOutputStream(vararg options: OpenOption) = Files.newOutputStream(toPath(), *options)

fun Path.asProperties() = Files.newInputStream(this).asProperties()
fun File.asProperties() = toPath().asProperties()

fun Path.delete() : Map<Path,IOException> = if(isDirectory()) deleteDirectory() else deleteFile()
fun File.delete() : Map<File,IOException> = toPath().delete().mapKeys { (key,_) -> key.toFile() }

private fun Path.deleteFile() : Map<Path,IOException>  = try { Files.delete(this); mapOf() } catch(e: IOException) { mapOf(this to e) }
private fun Path.deleteDirectory(): Map<Path,IOException> {
    require(Files.isDirectory(this)) { "folder must be a folder (e.g. not a a file)" }
    val resultMap = mutableMapOf<Path,IOException>()

    Files.walkFileTree(this, object : SimpleFileVisitor<Path>() {
        override fun postVisitDirectory(path: Path, exception: IOException?): FileVisitResult {
            try { Files.delete(path) } catch(e: IOException) { resultMap[path] = e }
            return FileVisitResult.CONTINUE
        }

        override fun visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult {
            try { Files.delete(path) } catch(e: IOException) { resultMap[path] = e }
            return FileVisitResult.CONTINUE
        }
    })

    return resultMap
}