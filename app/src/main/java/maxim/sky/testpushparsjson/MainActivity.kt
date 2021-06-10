package maxim.sky.testpushparsjson

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var textView: TextView
    private val JOB_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btn_on_off)
        textView = findViewById(R.id.tv_text)
        createObjectJobInfo()

        button.setOnClickListener {

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
            .setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            .setPeriodic(1*60*1000)
            .build()

        //task
        val scheduler: JobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = scheduler.schedule(jobInfo)
        if (result==JobScheduler.RESULT_SUCCESS) {
            Log.d("test","JOb Scheduled");
        } else {
            Log.d("test","Job Scheduling fail");
        }
    }



}