package com.example.asystentoro

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_task.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var myTaskFromMainActivity:ArrayList<DoTAsk> = ArrayList()
    var RecyclerTasks: RecyclerView? = null
    lateinit var exampleList: ArrayList<ItemCardView>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val let = arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ace: MainActivity? = activity as MainActivity?
        myTaskFromMainActivity = ace?.getTasks()!!


        RecyclerTasks = view.findViewById(R.id.RycyclerTask)
        exampleList = DoTAsk().generateTaskList(myTaskFromMainActivity)
        val adapter = MyAdapter(exampleList)

//       RecyclerTasks?.adapter = MyAdapter(exampleList)
        RecyclerTasks?.adapter = adapter
        RecyclerTasks?.layoutManager = LinearLayoutManager(view.context)
        RecyclerTasks?.setHasFixedSize(true)

        var btn:ImageButton = view.findViewById(R.id.Adder)
        btn.setOnClickListener{
            val newItem = ItemCardView(R.drawable.circle, "kolo","YOLO")
            exampleList.add(exampleList.size,newItem)
            adapter.notifyItemChanged(exampleList.size)
        }













    }

    fun insertItem(view: View)
    {
        val index: Int = Random.nextInt(8)
        val newItem = ItemCardView(
            R.drawable.ic_arrow_upward_24,
            "Kupa",
            "Frajer"
        )


    }

    fun removeItem(view: View)
    {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




}