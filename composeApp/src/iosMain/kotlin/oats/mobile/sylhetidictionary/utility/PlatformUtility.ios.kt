package oats.mobile.sylhetidictionary.utility

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
val applicationSupportDirectory by lazy {
    requireNotNull(
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSApplicationSupportDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )
    )
}

operator fun NSURL.plus(pathComponent: String) =
    requireNotNull(URLByAppendingPathComponent(pathComponent))

val NSURL.absolutePath
    get() = requireNotNull(path)
