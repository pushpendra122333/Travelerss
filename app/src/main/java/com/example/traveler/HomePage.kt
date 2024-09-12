package com.example.traveler

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class HomePage : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carAdapter: CarAdapter
    private lateinit var searchBar: EditText
    private lateinit var filterSpinner: Spinner
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        searchBar = view.findViewById(R.id.search_bar)
        filterSpinner = view.findViewById(R.id.filter_spinner)
        toolbar = view.findViewById(R.id.toolbar)


        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        recyclerView.layoutManager = LinearLayoutManager(context)


        val carList = listOf(
            Car("Suzuki Swift Dzire", R.drawable.car1, 4, 2, 4, "₹ 3,000 / Per Day"),
            Car("Toyota Innova", R.drawable.carr2, 7, 2, 4, "₹ 5,000 / Per Day"),
            Car("Maruti Brezza", R.drawable.brezza1, 5, 2, 4, "₹ 4,000 / Per Day"),
            Car("HayaBusa", R.drawable.hayabusa, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("Mercedes", R.drawable.mercedes, 20, 8, 2, "₹ 12,000 / Per Day"),
            Car("Mercedes Mini Bus", R.drawable.mercedes2, 20, 8, 2, "₹ 10,000 / Per Day"),
            Car("TVS Ntorq 125", R.drawable.tvs, 2, 0, 0, "₹ 1,000 / Per Day"),
            Car("Yamaha Ray ZR 125 Fi Hybrid", R.drawable.yahama, 2, 0, 0, "₹ 1,100 / Per Day"),
            Car("Suzuki Access 125", R.drawable.access125, 2, 0, 0, "₹ 800 / Per Day"),
            Car("Suzuki Burgman Street", R.drawable.bugman, 2, 0, 0, "₹ 1,500 / Per Day"),
            Car("Honda Activa 6G", R.drawable.blueactiva, 2, 0, 0, "₹ 600 / Per Day"),
            Car("Yamaha Aerox 155", R.drawable.aerox, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("OLA S1 Pro", R.drawable.olascooty, 2, 0, 0, "₹ 2,200 / Per Day"),
            Car("Mahindra Suv", R.drawable.mahindra_suv, 10, 3, 5, "₹ 4,000 / Per Day"),
            Car("Renault Kwid", R.drawable.renault_kwd, 6, 2, 4, "₹ 4,000 / Per Day"),
            Car("Mahindra Thar", R.drawable.thar, 4, 2, 3, "₹ 4,000 / Per Day"),
            Car("Mahindra Scorpio", R.drawable.mahindra_scorpio, 10, 2, 5, "₹ 4,000 / Per Day"),
            Car("Royal Enfield Hunter 350", R.drawable.royal_hunter, 2, 0, 0, "₹ 2,500 / Per Day"),
            Car("Hero Splendor Plus", R.drawable.splendor, 2, 0, 0, "₹ 500 / Per Day"),
            Car("Yamaha R15 V4", R.drawable.yahama_r15, 2, 0, 0, "₹ 2,000 / Per Day"),
            Car("Royal Enfield Meteor 350", R.drawable.royal_metero, 2, 0, 0, "₹ 2,800 / Per Day"),
            Car("Honda Unicorn", R.drawable.honda_unicorn, 2, 0, 0, "₹ 700 / Per Day"),
            Car("Kawasaki Ninja H2R", R.drawable.kawasaki_ninja, 2, 0, 0, "₹ 2,600 / Per Day"),
            Car("Ford Transit", R.drawable.ford_minibus, 12, 4, 2, "₹ 8,000 / Per Day"),
            Car("Volkswagen Caravelle", R.drawable.volksvagon_minibus, 9, 4, 2, "₹ 9,000 / Per Day"),
            Car("Renault Master", R.drawable.renault_master, 8, 4, 2, "₹ 6,000 / Per Day"),
            Car("Renault Trafic", R.drawable.renault_minibus, 10, 4, 2, "₹ 7,200 / Per Day"),
            Car("Ford Tourneo Custom", R.drawable.ford_turneo, 8, 4, 4, "₹ 5,900 / Per Day")

        )

        carAdapter = CarAdapter(carList) { car ->
            val context = requireContext()
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("car", car)

            startActivity(intent)
        }
        recyclerView.adapter = carAdapter



        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                carAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val filterOptions = arrayOf("Default", "Price: Low to High", "Price: High to Low", "Min. 2 Passengers", "Min. 4 Passengers", "Min. 6 Passengers")
        val adapter = CustomSpinnerAdapter(requireContext(), filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = adapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> carAdapter.resetData() // Default
                    1 -> carAdapter.sortByPriceAscending()
                    2 -> carAdapter.sortByPriceDescending()
                    3 -> carAdapter.filterByPassengerCapacity(2)
                    4 -> carAdapter.filterByPassengerCapacity(4)
                    5 -> carAdapter.filterByPassengerCapacity(6)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_page, menu)

        val feedbackItem = menu.findItem(R.id.action_feedback)
        val helpItem = menu.findItem(R.id.action_help)

        // Set the custom color for each menu item
        setMenuItemTextColor(feedbackItem, R.color.menu_item_text_color)
        setMenuItemTextColor(helpItem, R.color.menu_item_text_color)

        super.onCreateOptionsMenu(menu, inflater)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_feedback -> {
                // Handle feedback action

               val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://alike-jam-bee.notion.site/FEEDBACK-2ddf172a2229471aaf839df4c2968520?pvs=4")

                startActivity(i)
                true
            }
            R.id.action_help -> {
                // Handle help action

                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:ps3389401@gmail.com")
                }
                startActivity(Intent.createChooser(emailIntent, "Send email"))

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun setMenuItemTextColor(menuItem: MenuItem, colorResId: Int) {
        val spanString = SpannableString(menuItem.title)
        spanString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), colorResId)),
            0,
            spanString.length,
            0
        )
        menuItem.title = spanString
    }


}