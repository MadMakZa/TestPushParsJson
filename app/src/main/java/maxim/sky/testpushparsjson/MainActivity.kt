package maxim.sky.testpushparsjson

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val JOB_ID = 1
    private var flagBtn: Boolean = false
    private lateinit var button: Button
    lateinit var textView: TextView

    private var jobScheduler: JobScheduler? = null
    private lateinit var jobInfo: JobInfo


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun init(){
        button = findViewById(R.id.btn_on_off)
        textView = findViewById(R.id.tv_text)
//        jobScheduler = applicationContext.getSystemService(jobScheduler::class.java) as JobScheduler


        button.setOnClickListener {
            if (!flagBtn) {
                flagBtn = true
                button.text = "OFF"
                scheduleJob()
                Toast.makeText(this, "Scheduler is launch!", Toast.LENGTH_SHORT).show()
            }
            else if (flagBtn){
                flagBtn = false
                button.text = "ON"
                cancelJob()
                Toast.makeText(this,"Scheduler is stopped!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //непосредственно создание и запуск шедулера
    private fun scheduleJob(){
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val cn = ComponentName(this, JobSchedulerService::class.java)
        val jobInfo: JobInfo.Builder = JobInfo.Builder(JOB_ID,cn)
        val job = jobInfo
//                .setPeriodic(60 * 1000 * 60)
                .setMinimumLatency(TimeUnit.MILLISECONDS.toMillis(10)) //The minimum delay time to execute
                .setOverrideDeadline(TimeUnit.MILLISECONDS.toMillis(15)) //Maximum delay time to execute
                .setBackoffCriteria(TimeUnit.MILLISECONDS.toMillis(1000), JobInfo.BACKOFF_POLICY_LINEAR) //Linear retry scheme
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setPersisted(true)
                .build()

        if (jobScheduler != null) {
            jobScheduler!!.schedule(job)

            Log.d("test", "Job Scheduler created")
        }

    }
    //выключить шедулер
    private fun cancelJob(){
        jobScheduler!!.cancel(JOB_ID)
        Log.d("test","Job Cancelled")
    }

}