package testutils

import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.OutputStreamAppender
import java.io.ByteArrayOutputStream
import java.util.*


fun getLogOutputOf(closure: () -> Unit): String {
    val loggerOutput = ByteArrayOutputStream()
    val context = LoggerContext.getContext(false)
    val config = context.configuration
    val layout = config.rootLogger.appenders.values.first().layout
    val name = "Logger-${UUID.randomUUID()}"
    val appender = OutputStreamAppender.createAppender(layout, null, loggerOutput, name, false, true)
    appender.start()
    config.addAppender(appender)
    config.rootLogger.addAppender(appender, null, null)
    try {
        closure()
    } finally {
        config.rootLogger.removeAppender(name)
    }
    return loggerOutput.toString()
}