package services

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class AsyncTaskSuperGet(private val isAuth: Boolean, open val token: String = "") :
    AsyncTask<String, Int, String>() {
    private lateinit var listener: OnUpdateListener

    override fun doInBackground(vararg urlString: String?): String {
        var result = ""
        var connection: HttpURLConnection? = null
        var stream: InputStream? = null
        var reader: BufferedReader? = null

        try {
            //Iniciar a conexão
            val url = URL(urlString[0])
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            if (isAuth) {
                connection.setRequestProperty("Authorization", "Bearer $token")
            }

            connection.connect()

            val responseCode: Int = connection.responseCode

            if (responseCode == 200) {
                //Ler resposta
                stream = connection.inputStream!!

                reader = BufferedReader(InputStreamReader(stream, "iso-8859-1"), 8)

                var tempStr: String?

                try {

                    while (true) {
                        tempStr = reader.readLine()
                        if (tempStr == null) {
                            break
                        }
                        result += tempStr
                    }

                } catch (Ex: Exception) {
                    Log.e("e", "Error in convertToString " + Ex.printStackTrace())
                }
            }

        } catch (e: Exception) {
            Log.d("MyAsyncTask", e.message!!)
        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
        }

        return result
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

interface OnUpdateListener {
    fun onUpdate(jsonResponse: String)
}