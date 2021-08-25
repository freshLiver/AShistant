package com.freshliver.ashistant

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.freshliver.ashistant.utils.FileUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.File

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.freshliver.ashistant", appContext.packageName)
    }

    @Test
    fun getFileMimetype() {
        val input = FileUtils.getMimeTypeFromFile(File("test.png"))
        val expected = "image/png"

        assertEquals(expected, input)
    }

    @Test
    fun getFileExtension() {
        val input = FileUtils.getExtensionFromFile(File("test.jpeg"))
        val expected = "jpg"

        assertEquals(expected, input)
    }
}