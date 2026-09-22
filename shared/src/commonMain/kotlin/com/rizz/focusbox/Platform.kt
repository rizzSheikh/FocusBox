package com.rizz.focusbox

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform