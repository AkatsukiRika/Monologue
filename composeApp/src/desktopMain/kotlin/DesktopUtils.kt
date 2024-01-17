import java.io.File
import java.net.URL

object DesktopUtils {
    fun getResourceURL(path: String): URL? {
        return this::class.java.classLoader?.getResource("raw/$path")
    }

    fun createTempFileFromResource(url: URL, extension: String = ".tmp"): File {
        val tempFile = File.createTempFile("temp_file", extension)
        tempFile.deleteOnExit()

        url.openStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    fun getHomeDirectory() = System.getProperty("user.home")
}