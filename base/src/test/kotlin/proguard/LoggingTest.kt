/*
 * ProGuardCORE -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package proguard

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.string.shouldContain
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.appender.OutputStreamAppender
import java.io.ByteArrayOutputStream

class LoggingTest : FreeSpec({

    "Given a default logger" - {
        // Add an appender writing to an OutputStream we can capture instead of stdout.
        val loggerOutput = ByteArrayOutputStream()
        val context = LoggerContext.getContext(false)
        val config = context.configuration
        // Grab the same config as the current appender.
        val layout = config.rootLogger.appenders.values.first().layout
        val appender = OutputStreamAppender.createAppender(layout, null, loggerOutput, "TestLogger", false, true)
        appender.start()
        config.addAppender(appender)
        config.rootLogger.addAppender(appender, null, null)

        val testLogger = context.getLogger("TestLogger")
        "When logging a warning" - {
            testLogger.warn("Hello World")
            val output = loggerOutput.toString()
            "Then the log should contain the message" { output shouldContain "Hello World" }
        }
    }
})
