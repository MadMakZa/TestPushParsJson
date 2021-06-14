package maxim.sky.testpushparsjson

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var textView: TextView
    private val JOB_ID = 1
    private var flagBtn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btn_on_off)
        textView = findViewById(R.id.tv_text)

        button.setOnClickListener {
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

    private fun info(){
        textView.text = "Scheduler"
    }

    private fun createObjectJobInfo(){
        //builder
        val serviceName = ComponentName(this, JobSchedulerService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID, serviceName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//            .setRequiresDeviceIdle(true)
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