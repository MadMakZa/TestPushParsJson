package maxim.sky.testpushparsjson

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.json.JSONObject
import java.net.URL


class JobSchedulerService: JobService() {

    private var jobCancelled: Boolean = false
    private var currentNumber = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("test","Job started")
        doBackgroundWork(params)
        createNotificationChannel()
        return true
    }


    private fun doBackgroundWork(params: JobParameters?){
        Thread(Runnable {
            while (!jobCancelled) {
                getWebSite()
                sendNotification()
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {

                }
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
    private fun getWebSite(){
        val url = "https://redfront.space/api/sand-box-get/"

        val apiResponse = URL(url).readText()
//        Log.d("test", apiResponse)
        //работа с массивом
        val number = JSONObject(apiResponse).getString("number") //вытянуть значение по ключу
        Log.d("test", number.toString())
        currentNumber = number.toInt()

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
        val pi = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT) as PendingIntent

        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Полученные данные:")
            .setContentText(currentNumber.toString())
            .setContentIntent(pi)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(1, builder.build())
        }

    }


}