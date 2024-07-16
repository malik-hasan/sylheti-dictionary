
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask

val documentsDirectoryPath: NSString
    get() = NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as NSString
