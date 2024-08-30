package com.example.traveler


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class WelcomePage : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        viewPager = findViewById(R.id.v1)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager.adapter = ViewPageAdapter(supportFragmentManager)

        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()

        val tabIconColor = ContextCompat.getColorStateList(this, R.color.tab_icon_color)
        val tabTextColor = ContextCompat.getColorStateList(this, R.color.tab_text_color)

        tabLayout.apply {
            tabIconTint = tabIconColor
            setTabTextColors(tabTextColor)
            setSelectedTabIndicatorColor(ContextCompat.getColor(this@WelcomePage, R.color.selected_tab_color))
        }
    }
    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.apply {
            setIcon(R.drawable.baseline_home_24)  // Set your home icon
            text = "Home"
            contentDescription = "Tab 1"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(1)?.apply {
            setIcon(R.drawable.baseline_menu_24)  // Set your categories icon
            text = "Categories"
            contentDescription = "Tab 2"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(2)?.apply {
            setIcon(R.drawable.baseline_shopping_cart_24)  // Set your order icon
            text = "Order"
            contentDescription = "Tab 3"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(3)?.apply {
            setIcon(R.drawable.baseline_person_2)  // Set your profile icon
            text = "Profile"
            contentDescription = "Tab 4"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.selected_tab_color))
                tab?.view?.findViewById<TextView>(android.R.id.text1)?.setTextColor(ContextCompat.getColor(this@WelcomePage, R.color.selected_tab_color))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
                tab?.view?.findViewById<TextView>(android.R.id.text1)?.setTextColor(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional: Handle reselection if needed
            }
        })
    }


}
