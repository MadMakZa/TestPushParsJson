package maxim.sky.testpushparsjson

import android.app.job.JobParameters
import android.app.job.JobService

class JobSchedulerService: JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}