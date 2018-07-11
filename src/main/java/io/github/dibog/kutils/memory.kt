package io.github.dibog.kutils

fun Long.toHumanMemoryString(): String {
    val bytes = this
    val kiloBytes = bytes / 1024L
    val megaBytes = kiloBytes / 1024L
    val gigaBytes = megaBytes/ 1024L
    val teraBytes = gigaBytes / 1024L
    val petaBytes = teraBytes / 1024L

    return when {
        petaBytes>0 -> humanify(petaBytes, teraBytes % 1024L, "PB", "TB")
        teraBytes>0 -> humanify(teraBytes, gigaBytes % 1024L, "TB", "GB")
        gigaBytes>0 -> humanify(gigaBytes, megaBytes % 1024L, "GB", "MB")
        megaBytes>0 -> humanify(megaBytes, kiloBytes % 1024L, "MB", "KB")
        kiloBytes>0 -> humanify(kiloBytes, bytes % 1024L, "KB", "B")
        bytes>0 -> "${bytes}B"
        else -> "no mem"
    }
}