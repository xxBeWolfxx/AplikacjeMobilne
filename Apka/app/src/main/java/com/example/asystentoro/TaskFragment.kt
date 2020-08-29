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
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.toDeferred
import com.example.SaveTasksMutation
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
class TaskFragment : Fragment(), MyAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var myTaskFromMainActivity:ArrayList<DoTAsk> = ArrayList()
    var RecyclerTasks: RecyclerView? = null
    lateinit var exampleList: ArrayList<ItemCardView>
    lateinit var adapter: MyAdapter
    lateinit var apolloclientTask: ApolloClient
    var isclicked:Boolean = false





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

        myTaskFromMainActivity = MyApplication.globalTask!!
        apolloclientTask = MyApplication.globalApolloClient!!


        RecyclerTasks = view.findViewById(R.id.RycyclerTask)
        exampleList = DoTAsk().generateTaskList(myTaskFromMainActivity)
        adapter = MyAdapter(exampleList, this)

//       RecyclerTasks?.adapter = MyAdapter(exampleList)
        RecyclerTasks?.adapter = adapter
        RecyclerTasks?.layoutManager = LinearLayoutManager(view.context)
        RecyclerTasks?.setHasFixedSize(true)

        val btnAdd:ImageButton = view.findViewById(R.id.Adder)
        btnAdd.setOnClickListener{
            val newItem = ItemCardView(R.drawable.circle, "kolo","YOLO", exampleList.size)
            exampleList.add(newItem.ID,newItem)
            adapter.notifyDataSetChanged()

            if (isclicked)
            {

            }
        }
        val btnRem:ImageButton = view.findViewById(R.id.Remover)
        btnRem.setOnClickListener{
            exampleList.removeAt(exampleList.size - 1)
            adapter.notifyDataSetChanged()
           // apolloclientTask.mutate(SaveTasksMutation("Spotkanko na winko", myTaskFromMainActivity[3].id!!,"A to jest z Tasku XD".toInput())).toDeferred()

        }














    }


    override fun onItemClick(position: Int) {
        Log.d("Klik", "Position $position")
        isclicked = true
        val clickedItem= exampleList[position]
        clickedItem.text1 = "POKAŻ CYCKI"
        adapter.notifyDataSetChanged()

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