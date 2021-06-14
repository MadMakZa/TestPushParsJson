package maxim.sky.testpushparsjson

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import org.json.JSONObject
import java.net.URL

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
                getWebSite()
                try {
                    Thread.sleep(1000)
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
    //запрос на сервер
    private fun getWebSite(){
        val url = "https://redfront.space/api/sand-box-get/"

        val apiResponse = URL(url).readText()
        Log.d("test", apiResponse)
        //работа с массивом
        val number = JSONObject(apiResponse).getString("number") //вытянуть значение по ключу
        Log.d("test", number.toString())

    }


}