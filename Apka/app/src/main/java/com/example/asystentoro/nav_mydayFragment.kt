package com.example.asystentoro

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [nav_mydayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class nav_mydayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

      var search: EditText? = null
      var search_floating: FloatingActionButton? = null
      var view_weather: ImageView? = null

      var view_city: TextView? = null
      var view_temp: TextView? = null
      var view_press: TextView? = null
      var view_hum: TextView? = null
    var button: FloatingActionButton?=null
    var CurrentLoc:TextView?=null
    var lat :Double?=null
    var lon:Double?=null
    var City:String?=null
    var image: ImageView?=null
    var title_task: TextView?=null
    var type_task: TextView?=null
    var date_task: TextView?=null
    var time_task: TextView?=null
    var text_task: TextView?=null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSION_ID = 1010
    lateinit var myDayTask: ArrayList<DoTAsk>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_myday, container, false)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search = view.findViewById(R.id.search_edit)
        search_floating = view.findViewById(R.id.floating_search)
        view_weather = view.findViewById(R.id.wheather_image)
        view_city = view.findViewById(R.id.town)
        view_temp = view.findViewById(R.id.temp)
        view_press = view.findViewById(R.id.press)
        view_hum = view.findViewById(R.id.hum)
        image =view.findViewById(R.id.imageViewcardPresent)
        title_task=view.findViewById(R.id.titleTask)
        type_task=view.findViewById(R.id.typeTask)
        date_task=view.findViewById(R.id.dateTask)
        time_task=view.findViewById(R.id.timerTask)
        text_task=view.findViewById(R.id.infoText)

        title_task?.isEnabled = false
        type_task?.isEnabled = false
        date_task?.isEnabled = false
        time_task?.isEnabled = false
        text_task?.isEnabled = false

        myDayTask = DoTAsk().Sorting(MyApplication.globalTask!!)
        MyApplication.globalTask = myDayTask

        var soon_task = DoTAsk().CurrentTask(myDayTask)
        title_task?.setText(myDayTask[soon_task!!].title)
        text_task?.setText(myDayTask[soon_task!!].text)
        image?.setImageResource(when (myDayTask[soon_task!!].type?.toLowerCase()) {
            "meeting" -> R.drawable.meeting
            "shop list" -> R.drawable.shoplist
            "to do" -> R.drawable.todo
            "other" -> R.drawable.qmark
            else -> R.drawable.circle
        }
        )
        date_task?.text ="${myDayTask[soon_task!!].year}-${myDayTask[soon_task].month}-${myDayTask[soon_task].day}"
        time_task?.text ="${myDayTask[soon_task].hour}:${myDayTask[soon_task].minute}"


        type_task?.setText(myDayTask[soon_task].type)


        if(MyApplication.gcity==null){

        }
        else {
            api_key("https://api.openweathermap.org/data/2.5/weather?q=${MyApplication.gcity}&appid=a6f41d947e0542a26580bcd5c3fb90ef&units=metric")
        }

        if(MyApplication.glat==null && MyApplication.glon==null){

        }else {
            api_key("https://api.openweathermap.org/data/2.5/weather?lat=${MyApplication.glat}&lon=${MyApplication.glon}&appid=a6f41d947e0542a26580bcd5c3fb90ef&units=metric")
        }

        search_floating?.setOnClickListener {
            val imm = getSystemService(requireView().context, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
            City=(search!!.text.toString())
            MyApplication.gcity=City
            api_key("https://api.openweathermap.org/data/2.5/weather?q=$City&appid=a6f41d947e0542a26580bcd5c3fb90ef&units=metric")
            MyApplication.glat=null
            MyApplication.glon=null
        }

        button=view.findViewById(R.id.button)
        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!
        button?.setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())

            RequestPermission()
            getLastLocation()
            MyApplication.gcity=null

        }


    }

     fun api_key(Key: String) {
        val client = OkHttpClient()


        val request = Request.Builder()
            .url(Key)
            .get()
            .build()
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val response = client.newCall(request).execute()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }


                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body!!.string()
                    try {
                        val json = JSONObject(responseData)
                        val array = json.getJSONArray("weather")
                        val `object` = array.getJSONObject(0)
                        val icons = `object`.getString("icon")
                        val temp1 = json.getJSONObject("main")
                        val Temperature = temp1.getDouble("temp")
                        val pressure = temp1.getDouble("pressure")
                        val humidity = temp1.getDouble("humidity")
                        val Town = json.getString("name")

                        fun String.capitalizeWords(): String = split(" ").map { it.toLowerCase().capitalize() }.joinToString(" ")

                        val CapCity = Town?.capitalizeWords()

                        view_city?.let {
                            if (CapCity != null) {
                                setText(it, CapCity)

                            }
                        }
                        MyApplication.city=CapCity
                        MyApplication.weather=icons
                        MyApplication.hum=humidity
                        MyApplication.press=pressure
                        MyApplication.temp=Temperature

                        val temps =
                            Math.round(Temperature).toString() + "°C"
                        view_temp?.let { setText(it, temps) }
                        val press = "Pressure: "+
                            Math.round(pressure).toString() + " hPa"
                        view_press?.let { setText(it, press) }
                        val humid ="Humidity: "+
                            Math.round(humidity).toString() + " %"
                        view_hum?.let { setText(it, humid) }
                        view_weather?.let { setImage(it, icons) }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setText(text: TextView, value: String) {
        this.activity?.runOnUiThread(Runnable { text.text = value })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setImage(
        imageView: ImageView,
        value: String
    ) {
        this.activity?.runOnUiThread(Runnable { //paste switch
            when (value) {
                "01d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d01d))
                "01n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d01d))
                "02d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d02d))
                "02n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d02d))
                "03d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d03d))
                "03n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d03d))
                "04d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d04d))
                "04n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d04d))
                "09d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d09d))
                "09n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d09d))
                "10d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d10d))
                "10n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d10d))
                "11d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d11d))
                "11n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d11d))
                "13d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d13d))
                "13n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d13d))
                else -> imageView.setImageDrawable(resources.getDrawable(R.drawable.wheather))
            }
        })
    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLastLocation(){

        if(CheckPermission()){
            if(isLocationEnabled()){

                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        lat=location.latitude
                        lon=location.longitude
                        api_key("https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=a6f41d947e0542a26580bcd5c3fb90ef&units=metric")
                        MyApplication.glat=lat
                        MyApplication.glon=lon
                        CurrentLoc?.text  = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" + getCityName(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(context,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }!!
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback,Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var location: Location = locationResult.lastLocation
            Log.d("Debug:","your last last location: "+ location.longitude.toString())
        }
    }

    private fun CheckPermission():Boolean{
        if(
            context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED ||
            context?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
            )
        }
    }

    fun isLocationEnabled():Boolean{
        var locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(context, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment nav_mydayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            nav_mydayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}


