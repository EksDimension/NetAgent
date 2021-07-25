package com.eks.netagent.utils

import okhttp3.ResponseBody
import java.io.*

/**
 * Created by Riggs on 12/19/2019
 */
object FileUtil {
    fun saveFile(filePath: String?, body: ResponseBody): File? {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        var file: File? = null
        try {
            if (filePath == null) {
                return null
            }
            file = File(filePath)
            if (!file.exists()) {
                file.createNewFile()
            }
            body.contentLength()
            var fileSizeDownloaded: Long = 0
            val fileReader = ByteArray(4096)
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
            }
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file
    }
}