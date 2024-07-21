import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask

val NSSearchPathDirectory.path: NSString
    get() = NSSearchPathForDirectoriesInDomains(
        directory = this,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as NSString

