package maxim.sky.testpushparsjson

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val JOB_ID = 1
    private val NOTIFICATION_ID = 120
    private val CHANNEL_ID = "101"
    var exampleTitle = "number is:"
    var exampleDescription = "number"

    private var flagBtn: Boolean = false
    private lateinit var button: Button
    lateinit var textView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        button = findViewById(R.id.btn_on_off)
        textView = findViewById(R.id.tv_text)
        CreateNotificationChannel()

        button.setOnClickListener {
//            sendNotification()
            Log.d("test","value in example: $exampleDescription");
            if (!flagBtn){
                flagBtn = true
                button.text = "OFF"
                createObjectJobInfo()
            }
            else if (flagBtn){
                flagBtn = false
                button.text = "ON"
                cancelJob()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun CreateNotificationChannel(){
        val name = "Notification title"
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    fun sendNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(exampleTitle)
            .setContentText(exampleDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createObjectJobInfo(){
        //builder
        val serviceName = ComponentName(this, JobSchedulerService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setRequiresCharging(false)
            .setPersisted(true)
            .setPeriodic(15*60*1000) //15 минут минимум
            .build()

        //task
        val scheduler: JobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = scheduler.schedule(jobInfo)
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d("test","JOb Scheduled created");
        } else {
            Log.d("test","Job Scheduling fail");
        }
    }

    private fun cancelJob(){
        val scheduler: JobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)
        Log.d("test","Job Cancelled");
    }

}