package com.example.traveler


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
            setSelectedTabIndicatorColor(ContextCompat.getColor(this@WelcomePage, R.color.selected_tablayout_color))
        }
    }
    private fun setupTabIcons() {
        tabLayout.getTabAt(0)?.apply {
            setIcon(R.drawable.baseline_home_24)  // Set your home icon
            text = "HOME"
            contentDescription = "Tab 1"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(1)?.apply {
            setIcon(R.drawable.baseline_menu_24)  // Set your categories icon
            text = ""
            contentDescription = "Tab 2"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(2)?.apply {
            setIcon(R.drawable.baseline_shopping_cart_24)  // Set your order icon
            text = ""
            contentDescription = "Tab 3"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.getTabAt(3)?.apply {
            setIcon(R.drawable.baseline_person_2)  // Set your profile icon
            text = ""
            contentDescription = "Tab 4"
            icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.selected_tabicon_color))
                tab?.view?.findViewById<TextView>(android.R.id.text1)?.setTextColor(ContextCompat.getColor(this@WelcomePage, R.color.selected_tabicon_color))

                tab?.text = when (tab?.position) {
                    0 -> "Home"
                    1 -> "Categories"
                    2 -> "Order"
                    3 -> "Profile"
                    else -> ""
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.setTint(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))
                tab?.view?.findViewById<TextView>(android.R.id.text1)?.setTextColor(ContextCompat.getColor(this@WelcomePage, R.color.unselected_tab_color))

                tab?.text = ""
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Optional: Handle reselection if needed
            }
        })
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            // Show the confirmation dialog only if there are no fragments in the back stack
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit the app?")

        // Positive button - Exits the app
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            finishAffinity()  // Exit the app
        }

        // Negative button - Dismisses the dialog and does nothing
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        // Display the dialog
        builder.create().show()
    }

}
