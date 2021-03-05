package com.example.appclimadaniarroyo.utils

import com.scottyab.aescrypt.AESCrypt
import java.security.GeneralSecurityException

object CifradoManager {
    val key = "Cul1J0anMa3g"
    var encryptedMsg = ""
    var decryptedMsg = ""

    fun encryptAES(message: String): String {
        try {
            encryptedMsg = AESCrypt.encrypt(key, message)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        return encryptedMsg
    }

    fun decryptAES(message: String): String {
        try {
            decryptedMsg = AESCrypt.decrypt(key, message)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        }
        return decryptedMsg
    }
}