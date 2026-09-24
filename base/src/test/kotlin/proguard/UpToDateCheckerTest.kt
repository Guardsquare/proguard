package proguard

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FreeSpec
import java.io.File
import java.net.URL
import java.nio.file.Files

class UpToDateCheckerTest : FreeSpec({
    "An invalid file URI does not hide an outdated obfuscation dictionary" {
        val directory = Files.createTempDirectory("proguard-up-to-date-test").toFile()

        try {
            val output = File(directory, "output.jar").apply {
                createNewFile()
                setLastModified(1_000L)
            }
            val dictionary = File(directory, "dictionary with spaces.txt").apply {
                createNewFile()
                setLastModified(2_000L)
            }

            val configuration = Configuration().apply {
                programJars = ClassPath().apply {
                    add(ClassPathEntry(output, true))
                }
                obfuscationDictionary = URL(dictionary.toURI().toString().replace("%20", " "))
            }

            shouldNotThrowAny {
                UpToDateChecker(configuration).check()
            }
        } finally {
            directory.deleteRecursively()
        }
    }
})
