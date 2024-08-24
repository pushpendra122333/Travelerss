package com.example.traveler


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.apply {
            setIcon(R.drawable.baseline_home_24)  // Set your home icon
            text = "Home"
            contentDescription = "Tab 1"
        }
        tabLayout.getTabAt(1)?.apply {
            setIcon(R.drawable.baseline_menu_24)  // Set your categories icon
            text = "Categories"
            contentDescription = "Tab 2"
        }
        tabLayout.getTabAt(2)?.apply {
            setIcon(R.drawable.baseline_shopping_cart_24)  // Set your order icon
            text = "Order"
            contentDescription = "Tab 3"
        }
        tabLayout.getTabAt(3)?.apply {
            setIcon(R.drawable.baseline_person_2)  // Set your profile icon
            text = "Profile"
            contentDescription = "Tab 4"
        }
    }

}
