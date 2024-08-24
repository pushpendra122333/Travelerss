package com.example.traveler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments = listOf(
        HomePage(),
        CategoriesPage(),
        OrderPage(),
        ProfilePage()
    )
    private val fragmentTitles = listOf(
        "Home",
        "Categories",
        "Order",
        "Profile"
    )

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }
}
