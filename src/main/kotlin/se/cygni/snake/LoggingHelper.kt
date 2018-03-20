package se.cygni.snake

import java.util.logging.Logger

// Return logger for Java class, if companion object fix the name
public fun <T: Any> logger(forClass: Class<T>): Logger {
    return Logger.getLogger(unwrapCompanionClass(forClass).name)
}

// unwrap companion class to enclosing class given a Java Class
public fun <T: Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return if (ofClass.enclosingClass != null && ofClass.enclosingClass.kotlin.companionObject?.java == ofClass) {
        ofClass.enclosingClass
    } else {
        ofClass
    }
}

// return a lazy logger property delegate for enclosing class
public fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { logger(this.javaClass) }
}

