import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = ArgParser("elekuro image converter")
    val filePath by parser.argument(ArgType.String, description = "Input image file")
    val outputFileName by parser.option(ArgType.String, shortName = "o", description = "Output file name(default: out.bit)")
    val debugFile by parser.option(ArgType.String, shortName = "d")
    parser.parse(args)
    if (debugFile != null) {
        debug(filePath, debugFile!!)
        return
    }
    val inputFile = File(filePath)
    if (!inputFile.exists()) {
        println("File not found")
        exitProcess(1)
    }
    val image = ImageIO.read(inputFile)
    val width = image.width
    val height = image.height
    val outputStream = FileOutputStream(outputFileName ?: "out.bit")
    for (x in 0 until width) {
        for (y in 0 until height) {
            val color = Color(image.getRGB(x, y))
            val r = color.red
            val g = color.green
            val b = color.blue
            outputStream.write(r)
            outputStream.write(g)
            outputStream.write(b)
        }
    }
    outputStream.close()
}

fun debug(baseFile: String, debugFile: String) {
    val inputFile = File(debugFile)
    if (!inputFile.exists()) {
        println("File not found")
        exitProcess(1)
    }
    val baseImage = ImageIO.read(File(baseFile))
    val image = BufferedImage(baseImage.width, baseImage.height, BufferedImage.TYPE_INT_RGB)
    val debugStream = FileInputStream(inputFile)
    for (x in 0 until baseImage.width) {
        for (y in 0 until baseImage.height) {
            val r = debugStream.read()
            val g = debugStream.read()
            val b = debugStream.read()
            val color = Color(r, g, b)
            image.setRGB(x, y, color.rgb)
        }
    }
    ImageIO.write(image, "png", File("debug.png"))
}
