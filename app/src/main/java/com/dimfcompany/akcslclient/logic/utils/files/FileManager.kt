package com.dimfcompany.akcslclient.logic.utils.files

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.logic.utils.strings.StringManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.net.URLConnection
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FileManager
{
    companion object
    {
        fun create_temp_image_file(extension: String? = Constants.EXTENSION_PNG): File
        {
            val name = StringManager.getNameForNewFile(extension!!)
            return createFile(name, null, Constants.FOLDER_TEMP_FILES)
        }

        fun createPdfFile(name: String): File
        {
            return createFile(name, null, Constants.FOLDER_PDFS)
        }

        fun getPdfFile(file_name: String):File
        {
            val folder_pdfs = File(getRootFolder() + "/" + Constants.FOLDER_PDFS)
            val file = File(folder_pdfs, file_name)
            return file
        }

        fun checkIfFileExists(file_name: String, folder: String = Constants.FOLDER_PDFS): Boolean
        {
            val folder_file = File(getRootFolder() + "/" + folder)
            val file = File(folder_file, file_name)

            return file.exists() && file.length() > 0
        }

        fun createFile(name: String, extansion: String?, folder: String): File
        {
            val file: File

            val folder_file = File(getRootFolder() + "/" + folder)
            if (!folder_file.exists())
            {
                folder_file.mkdirs()
            }

            var file_name = name
            if (extansion != null && extansion.length > 0)
            {
                file_name = file_name + "." + extansion
            }

            Log.e("FileManager", "createFile: Will create new file $file_name in folder $folder");

            file = File(folder_file, file_name)
            if (file.exists())
            {
                file.delete()
            }

            file.createNewFile()

            return file
        }

        fun getRootFolder(): String
        {
            return AppClass.app.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.toString()
//            return Environment.getExternalStorageDirectory()!!.toString()
        }

        fun uriFromFile(file: File): Uri
        {
            var uri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(
                    AppClass.app,
                    AppClass.app.packageName + ".provider",
                    file
                )
            }
            else
            {
                uri = Uri.fromFile(file)
            }

            if (uri == null)
            {
                throw RuntimeException("Error could not create uri!")
            }

            return uri
        }

        fun isContentImage(uri: Uri): Boolean
        {
            val cR = AppClass.app.getContentResolver()
            val type = cR.getType(uri)
            return type != null && type!!.startsWith("image")
        }

        fun isContentPdf(uri: Uri): Boolean
        {
            val cR = AppClass.app.getContentResolver()
            val type = cR.getType(uri)
            return type != null && type.contains("pdf")
        }

        fun isContentVideo(uri: Uri): Boolean
        {
            val cR = AppClass.app.getContentResolver()
            val type = cR.getType(uri)
            return type != null && type!!.startsWith("video")
        }

        fun getExtensionFromContentUri(uri: Uri): String?
        {
            val extension: String?

            if (uri.scheme == ContentResolver.SCHEME_CONTENT)
            {
                val mime = MimeTypeMap.getSingleton()
                extension = mime.getExtensionFromMimeType(AppClass.app.getContentResolver().getType(uri))
            }
            else
            {
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
            }

            return extension
        }

        fun copyToFileFromIntentData(file: File, data: Uri)
        {
            val inputStream = AppClass.app.contentResolver.openInputStream(data)
            val outputStream = FileOutputStream(file)

            copy(inputStream!!, outputStream)
        }

        fun copy(input: InputStream, output: OutputStream)
        {
            try
            {
                val buf: ByteArray = ByteArray(1024)
                var len: Int = input.read(buf)

                while (len > 0)
                {
                    output.write(buf, 0, len)
                    len = input.read(buf)
                }
            }
            catch (e: java.lang.Exception)
            {
            }
            finally
            {
                if (input != null)
                {
                    try
                    {
                        input.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }

                }

                if (output != null)
                {
                    try
                    {
                        output.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }
                }
            }
        }

        fun isFileImage(path_real: String): Boolean
        {
            val mimeType = URLConnection.guessContentTypeFromName(path_real)
            return mimeType != null && mimeType!!.startsWith("image")
        }

        fun getMimeType(url: String): String?
        {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null)
            {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }
    }
}


fun File.toPartBody(field_name: String): MultipartBody.Part?
{
    var bodyFile: MultipartBody.Part? = null

    if (this.exists())
    {
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), this)
        bodyFile = MultipartBody.Part.createFormData(field_name, this.name, requestBody)
    }

    return bodyFile
}

fun File.getMimeType(): String?
{

    return FileManager.getMimeType(this.path)
}

suspend fun File.toBase64(add_header: Boolean): String?
{
    val inputStream = FileInputStream(this) // You can get an inputStream using any I/O API
    val bytes: ByteArray
    val buffer = ByteArray(8192)
    var bytesRead: Int = inputStream.read(buffer)
    val output = ByteArrayOutputStream()
    var str_result: String? = null

    try
    {
        while (bytesRead != -1)
        {
            output.write(buffer, 0, bytesRead)
            bytesRead = inputStream.read(buffer)
        }
    }
    catch (e: IOException)
    {
        e.printStackTrace()
        return null
    }


    bytes = output.toByteArray()
    str_result = Base64.encodeToString(bytes, Base64.DEFAULT)
    if (add_header)
    {
        val mime_type = this.getMimeType()
        if (mime_type != null)
        {
            str_result = "data:$mime_type;base64," + str_result
        }
    }
    return str_result
}