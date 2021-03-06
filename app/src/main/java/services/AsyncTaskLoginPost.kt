package services

import android.os.AsyncTask
import android.util.Log
import com.example.myrestaurantapp.helpers.APIConstants
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskLoginPost(private var data: JSONObject) : AsyncTask<String, Int, String>() {

    private lateinit var listener: OnUpdateListener

    override fun doInBackground(vararg urlString: String?): String {
        var result = ""

        var connection: HttpURLConnection? = null
        var stream: InputStream? = null
        var reader: BufferedReader? = null

        try {

            //init post connection
            val url = URL(urlString[0])
            connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("Content-Type", "application/json")
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true

            //Enviar o corpo do post JSON Object
            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(data.toString())
            writer.flush()

            connection.connect()


            val responseCode: Int = connection.responseCode

            if (responseCode == 200) {
                //Ler resposta
                stream = connection.inputStream

                reader = BufferedReader(InputStreamReader(stream!!))

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
            } else if (responseCode == 401) {
                //Ver se mensagem é de credenciais inválidas
                result = APIConstants.unauthorizedResult
            }

        } catch (e: Exception) {
            Log.d("MyAsyncTask", e.message!!)
        } finally {
            reader?.close()
            stream?.close()
            connection?.disconnect()
            return result
        }
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