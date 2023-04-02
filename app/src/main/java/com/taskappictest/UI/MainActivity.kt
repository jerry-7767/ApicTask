package com.taskappictest.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.taskappictest.R
import com.taskappictest.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var strcompanyname: String? = null
    var strallcount: String? = null
    var strallcount1: String? = null
    var viewItems: ArrayList<String> = ArrayList()
    var viewItems1: ArrayList<String> = ArrayList()

    lateinit var brandlistAdapter: BrandlistAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inita()

        addItemsFromJSON()

    }

    private fun inita() {
        binding.txtShowFilter.setOnClickListener {
            val dialog =
                BottomSheetDialog(this, com.taskappictest.R.style.AppBottomSheetDialogTheme)

            val view =
                layoutInflater.inflate(com.taskappictest.R.layout.bottomsheet_applyfilter, null)

            val txt_cmp_name = view.findViewById<TextView>(com.taskappictest.R.id.txt_cmp_name)
            txt_cmp_name.text = strcompanyname

            val img_close = view.findViewById<ImageView>(com.taskappictest.R.id.img_close)

            val txt_brand_selection_count =
                view.findViewById<TextView>(com.taskappictest.R.id.txt_brand_selection_count)
            val txt_location_selection_count =
                view.findViewById<TextView>(com.taskappictest.R.id.txt_location_selection_count)

            txt_brand_selection_count.text = "Brand : " + strallcount
            txt_location_selection_count.text = "Location : " + strallcount1

            val txt_selected_brandcount =
                view.findViewById<TextView>(com.taskappictest.R.id.txt_selected_brandcount)
            val txt_selectedlocationcount =
                view.findViewById<TextView>(com.taskappictest.R.id.txt_selectedlocationcount)

            txt_selected_brandcount.text = strallcount
            txt_selectedlocationcount.text = strallcount1

            val rl_select_brands =
                view.findViewById<RelativeLayout>(com.taskappictest.R.id.rl_select_brands)
            val rl_select_location =
                view.findViewById<RelativeLayout>(com.taskappictest.R.id.rl_select_location)

            img_close.setOnClickListener {
                dialog.dismiss()
            }

            rl_select_brands.setOnClickListener {
                if (view.parent != null) {
                    (view.parent as ViewGroup).removeView(view)
                    dialog.dismiss()
                    val dialog1 =
                        BottomSheetDialog(this, com.taskappictest.R.style.AppBottomSheetDialogTheme)

                    val view1 =
                        layoutInflater.inflate(com.taskappictest.R.layout.bottomsheet_brand, null)
                    val img_close1 = view1.findViewById<ImageView>(com.taskappictest.R.id.img_close)
                    val recycler_brands =
                        view1.findViewById<RecyclerView>(com.taskappictest.R.id.recycler_brands)
                    val txt_title =
                        view1.findViewById<TextView>(com.taskappictest.R.id.txt_title)
                    val txt_add_to_filter =
                        view1.findViewById<TextView>(R.id.txt_add_to_filter)
                    txt_add_to_filter.setOnClickListener {
                        Log.e("checkfiltfsd", strallcount.toString())
                        dialog.setContentView(view)
                        dialog.show()
                        dialog1.dismiss()
                    }
                    txt_title.text = "Select Brands"

                    val brandlistlayout =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recycler_brands.layoutManager = brandlistlayout
                    recycler_brands.setHasFixedSize(true)
                    recycler_brands.isNestedScrollingEnabled = false

                    brandlistAdapter = BrandlistAdapter(
                        this,
                        viewItems
                    )
                    recycler_brands.adapter = brandlistAdapter

                    img_close1.setOnClickListener {
                        dialog1.dismiss()
                    }

                    dialog1.setContentView(view1)
                    dialog1.show()
                }

            }

            rl_select_location.setOnClickListener {
                if (view.parent != null) {
                    (view.parent as ViewGroup).removeView(view)
                    dialog.dismiss()
                    val dialog1 =
                        BottomSheetDialog(this, com.taskappictest.R.style.AppBottomSheetDialogTheme)

                    val view1 =
                        layoutInflater.inflate(com.taskappictest.R.layout.bottomsheet_brand, null)
                    val img_close1 = view1.findViewById<ImageView>(com.taskappictest.R.id.img_close)
                    val recycler_brands =
                        view1.findViewById<RecyclerView>(com.taskappictest.R.id.recycler_brands)
                    val txt_title =
                        view1.findViewById<TextView>(com.taskappictest.R.id.txt_title)
                    txt_title.text = "Select Locations"
                    val txt_add_to_filter =
                        view1.findViewById<TextView>(R.id.txt_add_to_filter)
                    txt_add_to_filter.setOnClickListener {
                        Log.e("checkfiltfsd", strallcount.toString())
                        dialog.setContentView(view)
                        dialog.show()
                        dialog1.dismiss()
                    }
                    val brandlistlayout =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recycler_brands.layoutManager = brandlistlayout
                    recycler_brands.setHasFixedSize(true)
                    recycler_brands.isNestedScrollingEnabled = false

                    brandlistAdapter = BrandlistAdapter(
                        this,
                        viewItems1
                    )
                    recycler_brands.adapter = brandlistAdapter

                    img_close1.setOnClickListener {
                        dialog1.dismiss()
                    }

                    dialog1.setContentView(view1)
                    dialog1.show()
                }

            }

            dialog.setContentView(view)
            dialog.show()
        }
    }


    private fun addItemsFromJSON() {
        try {
            val jsonDataString = readJSONDataFromFile()
            val itemObj = JSONObject(jsonDataString)
            val filterData = itemObj.getJSONArray("filterData")
            for (i in 0 until filterData.length()) {
                val itemObj = filterData.getJSONObject(i)
                val companyName = itemObj.getString("companyName")
                val hierarchy = itemObj.getJSONArray("hierarchy")

                for (j in 0 until hierarchy.length()) {
                    val itemObjH = hierarchy.getJSONObject(j)
                    val brandNameList = itemObjH.getJSONArray("brandNameList")
                    for (k in 0 until brandNameList.length()) {
                        val itemObjB = brandNameList.getJSONObject(k)
                        val brandName = itemObjB.getString("brandName")
                        val locationNameList = itemObjB.getJSONArray("locationNameList")
                        viewItems.add(brandName)
                        strallcount = viewItems.size.toString()
                        for (l in 0 until locationNameList.length()) {
                            val itemObjL = locationNameList.getJSONObject(l)
                            val locationName = itemObjL.getString("locationName")
                            viewItems1.add(locationName)
                            strallcount1 = viewItems1.size.toString()

                        }
                    }

                }

                strcompanyname = companyName
            }

        } catch (e: JSONException) {
            Log.e("jsonexception: ", e.message.toString())
        } catch (e: IOException) {
            Log.e("ioexception: ", e.message.toString())
        }
    }


    @Throws(IOException::class)
    private fun readJSONDataFromFile(): String? {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonString: String? = null
            inputStream = resources.openRawResource(com.taskappictest.R.raw.jsondataa)
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }


    class BrandlistAdapter(private val context: Context, var all: ArrayList<String>) :

        RecyclerView.Adapter<BrandlistAdapter.MultiViewHolder>() {
        var lstChk: ArrayList<String?>? = ArrayList()

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MultiViewHolder {
            val view = LayoutInflater.from(context)
                .inflate(com.taskappictest.R.layout.row_list, viewGroup, false)
            return MultiViewHolder(view)
        }

        override fun onBindViewHolder(multiViewHolder: MultiViewHolder, position: Int) {

            multiViewHolder.txt_data.text = all[position]

        }

        override fun getItemCount(): Int {
            return all.size
        }

        inner class MultiViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

            val txt_data: TextView
            val checkboxx: CheckBox

            init {
                txt_data = itemView.findViewById(com.taskappictest.R.id.txt_data)
                checkboxx = itemView.findViewById(com.taskappictest.R.id.checkboxx)
            }
        }

    }

}