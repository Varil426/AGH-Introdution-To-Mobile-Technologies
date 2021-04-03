package com.example.bartlomiej_kregielewski_wtorek14

import java.lang.StringBuilder

class FlagHelper {
    companion object {
        private val flagOffset = 127397

        fun countryCodeToFlagEmoji(countryCode: String): String {
            var code = countryCode
            if (countryCode.length != 2) {
                return "\uD83E\uDD14"
            }

            if (countryCode.equals("uk", true)) {
                code = "gb"
            }

            code = code.toUpperCase()

            val emoji = StringBuilder()

            for (i in code.indices) {
                emoji.appendCodePoint(code.get(i).toByte().toInt() + flagOffset)
            }

            return emoji.toString()
        }
    }
}