package dev.fiaz.calculator

import android.content.Context
import android.content.Intent
import android.os.Process
import kotlin.system.exitProcess

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtra("is_crash", true)
                putExtra("error_message", throwable.localizedMessage ?: "Unknown Error")
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            defaultHandler?.uncaughtException(thread, throwable)
        } finally {
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
    }

    companion object {
        fun initialize(context: Context) {
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(context))
        }
    }
}
