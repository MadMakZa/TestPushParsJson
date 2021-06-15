package maxim.sky.testpushparsjson


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class JobSchedulerService: JobService() {

    private var jobCancelled: Boolean = false
    private var currentNumber = "Error Request"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("test","Job started")
        createNotificationChannel()
        doBackgroundWork(params)

        return true
    }


    private fun doBackgroundWork(params: JobParameters?){
        Thread(Runnable {
            while (!jobCancelled) {
                getWebSite()
                SystemClock.sleep(1000*60)
            }
            Log.d("test","Job finished")
            jobFinished(params, false)
            }).start()
        }


    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("test","Job stopped")
        jobCancelled = true
        return true
    }
    //запрос на сервер
    private fun getWebSite() {
        val url = "https://redfront.space/api/sand-box-get/"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        val codeClient = client.newCall(request).execute().code
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                Log.d("test","onResponse $body")
                Log.d("test","CodeResponse $codeClient")
                if (response.isSuccessful) {
                    val gson = GsonBuilder().create()
                    val gsonData = gson.fromJson(body, GsonData::class.java)
                    currentNumber = gsonData.number
                    sendNotification()
                }
                else{
                    Log.d("test","response not successful")
                    currentNumber = "Error Request"
                    sendNotification()
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                //error here
                Log.d("test","Error request execute")
                currentNumber = "Error Request"
                sendNotification()
            }

        })

    }

    //notification
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val name = "Notification title"
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1", name, importance).apply {
            description = descriptionText
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification(){
        Log.d("test","Notification sending")
        val pi = PendingIntent.getBroadcast(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT) as PendingIntent
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.restorehealth)
            .setContentTitle("Полученные данные:")
            .setContentText(currentNumber)
            .setContentIntent(pi)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

            notificationManager.notify(1, builder.build())


    }

}
//макет Gson
data class GsonData(val result: String, val number: String)

