package io.github.dibog.kutils

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.regex.Pattern

fun InputStream.asProperties() : Properties {
    val props = Properties()
    this.use {
        props.load(it)
    }
    return props
}

inline fun <reified T:Any> Array<T>.reduceBy(i: Int): Array<T> {
    require(size >= i) { "Can not reduce array as its length (=$size) is smaller then specified index (=$i)" }
    return Array(size-i, { j -> this[j+i] })
}

fun InputStream.copyTo(out: OutputStream, bufferSize: Int=4096) {
    val buffer = kotlin.ByteArray(bufferSize)
    while(true) {
        val len = read(buffer)
        if(len<0) break
        out.write(buffer, 0, len)
    }
}

inline fun File.useAndDelete(block: (File)->Unit) {
    require(this.isFile) { "'$this' must be a file" }
    try {
        block.invoke(this)
    }
    finally {
        this.delete()
    }
}

inline fun useAndDelete(file1: File, file2: File, block: (File,File)->Unit) {
    file1.useAndDelete { f1 ->
        file2.useAndDelete { f2 ->
            block(f1, f2)
        }
    }
}

inline fun useAndDelete(file1: File, file2: File, file3: File, block: (File,File,File)->Unit) {
    file1.useAndDelete { f1 ->
        file2.useAndDelete { f2 ->
            file3.useAndDelete { f3 ->
                block(f1, f2, f3)
            }
        }
    }
}

inline fun Path.useAndDelete(block: (Path)->Unit) {
    require(Files.isRegularFile(this)) { "'$this' must be a file" }
    try {
        block.invoke(this)
    }
    finally {
        Files.deleteIfExists(this)
    }
}


inline fun useAndDelete(file1: Path, file2: Path, block: (Path,Path)->Unit) {
    file1.useAndDelete { f1 ->
        file2.useAndDelete { f2 ->
            block(f1, f2)
        }
    }
}

inline fun useAndDelete(file1: Path, file2: Path, file3: Path, block: (Path,Path,Path)->Unit) {
    file1.useAndDelete { f1 ->
        file2.useAndDelete { f2 ->
            file3.useAndDelete { f3 ->
                block(f1, f2, f3)
            }
        }
    }
}

fun InputStream.asByteArray(): ByteArray {
    val buffer = ByteArray(4096)
    val bout = ByteArrayOutputStream()
    use { stream ->
        loop@ while(true) {
            val pos =stream.read(buffer)
            if(pos<0) break@loop
            bout.write(buffer, 0, pos)
        }
    }
    return bout.toByteArray()
}


fun Path.ensureDirExists(): Path {
    Files.createDirectories(this)
    require(Files.exists(this) && Files.isDirectory(this))
    return this
}

fun File.ensureDirExists(): File  = toPath().ensureDirExists().toFile()

fun ByteArray.base64encode() = Base64.getEncoder().encodeToString(this)!!
fun String.base64decode(): ByteArray = Base64.getDecoder().decode(this)

fun String.base64() = Base64.getEncoder().encodeToString(trimIndent().toByteArray(Charsets.US_ASCII) )!!

internal fun String.expandMacros(macros: Map<String,String>): String {
    val re = Pattern.compile("\\$\\{(.*?)\\}")
    val m = re.matcher(this)

    val sb = StringBuffer()

    while(m.find()) {
        val variable = m.group(1)
        val expanded = macros[ variable ] ?: "\\\${$variable}"
        m.appendReplacement(sb, expanded)
    }
    m.appendTail(sb)

    return sb.toString()
}

