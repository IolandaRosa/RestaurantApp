package services

import android.os.AsyncTask
import android.os.StrictMode
import android.util.Log
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL


class AsyncTaskLogUnauthorizedPost(private var logFile: File) : AsyncTask<String, Int, String>() {

    private lateinit var listener: OnUpdateListener

    override fun doInBackground(vararg urlString: String?): String {
        try {
            var connection: HttpURLConnection? = null
            val boundary = "*****"
            val twoHyphens = "--"
            val lineEnd = "\r\n"
            var bytesRead = 0
            var bytesAvailable = 0
            var bufferSize = 0
            val maxBufferSize = 1 * 1024 * 1024;

            if (logFile.isFile) {
                try {
                    val fileInputStream = FileInputStream(logFile)

                    val url = URL(urlString[0])

                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)

                    //Abrir a conexao
                    connection = url.openConnection() as HttpURLConnection

                    connection.doInput = true
                    connection.doOutput = true
                    connection.useCaches = false
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Connection", "Keep-Alive")
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data")
                    connection.setRequestProperty(
                        "Content-Type",
                        "multipart/form-data; boundary=$boundary"
                    )

                    val outputStream = DataOutputStream(connection.outputStream)

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd)
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"logFile\"; filename=\"logFile.txt\"" + lineEnd)
                    outputStream.writeBytes(lineEnd)

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    val buffer = ByteArray(bufferSize)

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize)

                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize)
                        bytesAvailable = fileInputStream.available()
                        bufferSize = Math.min(bytesAvailable, maxBufferSize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    }

                    // send multipart form data necessary after file
                    outputStream.writeBytes(lineEnd)
                    outputStream.writeBytes(
                        twoHyphens + boundary + twoHyphens
                                + lineEnd
                    )

                    //connection.connect()

                    val code = connection.responseCode

                    if (code == 200) {
                        Log.d("AsyncUnauth", "Upload")
                    }

                    fileInputStream.close()
                    outputStream.flush()
                    outputStream.close()


                } catch (e: java.lang.Exception) {
                    Log.d("Exce", e.message)
                }
            }

        } catch (e: Exception) {
            Log.d("AsyncUnauth", e.message)
        }

        return ""
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null) {
            listener.onUpdate(result)
        }
    }

    fun setUpdateListener(listener: OnUpdateListener) {
        this.listener = listener
    }
}