package com.example.traveler

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Toast

class OrderPage : Fragment() {

    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var loginManager: LoginManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_page, container, false)

        // Initialize RecyclerView
        orderRecyclerView = view.findViewById(R.id.recycler_view_order)
        dbHelper = DatabaseHelper(requireContext())
        dbHelper = DatabaseHelper(requireContext())
        loginManager = LoginManager(requireContext())


        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)
        orderRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPixels))


        val userId = getCurrentUserId()
        if (userId != -1) {
            val bookings = dbHelper.getAllBookingsForUser(userId)
            orderAdapter = OrderAdapter(bookings){ bookingId ->
                // Handle cancel click
                cancelBooking(bookingId)
            }
            orderRecyclerView.adapter = orderAdapter
            orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            // Handle the case where user ID is not available
            Log.e("OrderPage", "No user is logged in")
            // Optionally, you might want to redirect to the login page:
            // startActivity(Intent(requireContext(), LoginActivity::class.java))
            // requireActivity().finish() // Optional: Close the current activity if applicable
        }

        return view
    }

    private fun getCurrentUserId(): Int {
        val loggedInEmail = loginManager.getLoggedInEmail()
        return if (loggedInEmail != null) {
            dbHelper.getUserIdByEmail(loggedInEmail) // Fetch the user ID using the email
        } else {
            -1 // No user is logged in
        }
    }
    private fun cancelBooking(bookingId: Int) {
        val result = dbHelper.cancelBooking(bookingId)
        if (result) {
            // Refresh the RecyclerView
            val userId = getCurrentUserId()
            if (userId != -1) {
                val bookings = dbHelper.getAllBookingsForUser(userId)
                orderAdapter = OrderAdapter(bookings) { id ->
                    cancelBooking(id)
                }
                orderRecyclerView.adapter = orderAdapter
            }
            Toast.makeText(requireContext(), "Booking canceled successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show()
        }
    }


}
