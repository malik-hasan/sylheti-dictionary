package oats.mobile.sylhetidictionary.utility

import platform.Foundation.NSSearchPathDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask

val NSSearchPathDirectory.path: NSString
    get() = NSSearchPathForDirectoriesInDomains(this, NSUserDomainMask, true).first() as NSString
