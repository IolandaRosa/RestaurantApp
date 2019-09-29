package services

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskItems:AsyncTask<String,Integer,String>() {

    private lateinit var listener:OnUpdateListener

    override fun doInBackground(vararg urlString: String?): String {
        try {
            var result = " "
            //Iniciar a conexão
            val url = URL(urlString[0])
            val connection = url.openConnection() as HttpURLConnection

            connection.connect()

            val responseCode: Int = connection.responseCode

            if (responseCode == 200) {
                //Ler resposta
                val stream = connection.inputStream

                val reader = BufferedReader(InputStreamReader(stream,"iso-8859-1"),8)

                /*var buffer = StringBuffer()

                while(reader.readLine() != null){
                    buffer.append(reader.readLine())
                }

                reader.close()
                stream.close()

                return buffer.toString()*/


                var tempStr: String?

                try {

                    while (true) {
                        tempStr = reader.readLine()
                        if (tempStr == null) {
                            break
                        }
                        result += tempStr
                    }

                    return result
                } catch (Ex: Exception) {
                    Log.e("e", "Error in convertToString " + Ex.printStackTrace())
                }
            }


        }
        catch (e:Exception){
            Log.d("MyAsyncTask",e.message)
        }

        return " "
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if(result!=null){
            listener.onUpdate(result)
        }
    }

    fun setUpdateListener(listener:OnUpdateListener){
        this.listener = listener
    }

}

interface OnUpdateListener{
    fun onUpdate(jsonResponse:String)
}