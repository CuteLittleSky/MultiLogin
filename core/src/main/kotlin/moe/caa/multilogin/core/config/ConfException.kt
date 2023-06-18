package moe.caa.multilogin.core.config

import java.io.IOException

class ConfException : IOException {
        constructor()
        constructor(message: String) : super(message)
        constructor(message: String, cause: Throwable) : super(message, cause)
        constructor(cause: Throwable) : super(cause)
    }