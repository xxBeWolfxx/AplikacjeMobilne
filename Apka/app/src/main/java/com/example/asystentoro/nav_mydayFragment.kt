package com.example.asystentoro

import android.os.Bundle
import android.os.StrictMode
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        search = view.findViewById(R.id.search_edit)
        search_floating = view.findViewById(R.id.floating_search)
        view_weather = view.findViewById(R.id.wheather_image)
        view_city = view.findViewById(R.id.town)
        view_temp = view.findViewById(R.id.temp)
        view_press = view.findViewById(R.id.press)
        view_hum = view.findViewById(R.id.hum)

        search_floating?.setOnClickListener {
            val imm = getSystemService(requireView().context, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(requireView().windowToken, 0)
            api_key(search!!.text.toString())
        }
    }

    private fun api_key(City: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=$City&appid=a6f41d947e0542a26580bcd5c3fb90ef&units=metric")
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

                        fun String.capitalizeWords(): String = split(" ").map { it.toLowerCase().capitalize() }.joinToString(" ")

                        val CapCity = City.capitalizeWords()

                        view_city?.let { setText(it, CapCity) }
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

    private fun setText(text: TextView, value: String) {
        this.activity?.runOnUiThread(Runnable { text.text = value })
    }

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