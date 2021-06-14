package maxim.sky.testpushparsjson

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class JobSchedulerService: JobService() {

    private var jobCancelled: Boolean = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("test","Job started")
        doBackgroundWork(params)
        return true
    }


    private fun doBackgroundWork(params: JobParameters?){
        Thread(Runnable {
            for (i in 1..10) {
                if (jobCancelled) {
                    return@Runnable
                }
                Log.d("test", "Thread wisp $i")
                try {
                    Thread.sleep(500)
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
        return false
    }


}