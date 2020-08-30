package com.example.asystentoro

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.example.TaskDetailsQuery
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var respone: Response<TaskDetailsQuery.Data>
    lateinit var TaskManger: ArrayList<DoTAsk>
    private lateinit var apolloClient: ApolloClient


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_task)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        apolloClient = ApolloClient.builder()
            .serverUrl("https://api-eu-central-1.graphcms.com/v2/ckd4epu1q0meu01xr4wx3arzu/master?query=%7B%0A%20%20tasks%20%7B%0A%20%20%20%20name%0A%20%20%20%20type%0A%20%20%20%20date%0A%20%20%20%20text%0A%20%20%20%20%0A%20%20%7D%0A%7D%0A")
            .build()


        lifecycleScope.launchWhenStarted {
            respone = apolloClient.query(TaskDetailsQuery()).toDeferred().await()
            TaskManger = convertDatabse(respone)
            TaskManger = DoTAsk().Sorting(TaskManger)
            MyApplication.globalTask = TaskManger
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_task, R.id.nav_myday, R.id.nav_pomodoro
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present. mmm
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getApolloClient(): ApolloClient {
        return apolloClient
    }

    private fun convertDatabse(database: Response<TaskDetailsQuery.Data>): ArrayList<DoTAsk> {
        val arrayTask = database.data?.tasks?.size?.let { ArrayList<DoTAsk>(it) }
        var variable: Int = 0
        for (item in database.data?.tasks!!) {
            var job: DoTAsk = DoTAsk()
            job.title = item.name
            job.type = item.type
            job.id = item.id
            job.text = item.text
            job.date = item.date.toString()
            job.number = variable
            job = DoTAsk().dataConverter(job)
            arrayTask?.add(variable, job)
            variable += 1

            if (variable == database.data?.tasks?.size) {
                DoTAsk().dataConverter(job)
                variable = 0
            }
        }
        arrayTask?.forEach { it.title.let { it -> Log.d("Checking:", it) } }
        return arrayTask!!

    }


}

private fun NavDeepLinkBuilder.setDestination(navDeepLink: NavDeepLink) {

}
