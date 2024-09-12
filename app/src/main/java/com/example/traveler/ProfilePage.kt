package com.example.traveler

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File

class ProfilePage : Fragment() {

    private lateinit var loginManager: LoginManager
    private lateinit var profileImageView: ImageView
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginManager = LoginManager(requireContext())

        val userEmail = loginManager.getLoggedInEmail()
        val userName = loginManager.getLoggedInUserName()

        val emailTextView: TextView = view.findViewById(R.id.profileemail)
        val nameTextView: TextView = view.findViewById(R.id.profilename)
        val aboutUs: TextView = view.findViewById(R.id.aboutus)
        val cotactUs: TextView = view.findViewById(R.id.Contactus)

        aboutUs.setOnClickListener{
            val  i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://alike-jam-bee.notion.site/About-Us-5fddc493a6b145c18465edf12b6ff78d?pvs=4")
            startActivity(i)
        }

        cotactUs.setOnClickListener{
            val  i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://alike-jam-bee.notion.site/Contact-Us-87fbd91c7bd2474d88049d7f71bfc35f?pvs=4")
            startActivity(i)
        }
        profileImageView = view.findViewById(R.id.profileImageView)

        emailTextView.text = userEmail ?: "Not available"
        nameTextView.text = userName ?: "Not available"

        val logoutButton: TextView = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        setupSocialMediaLinks(view)
        val changeImageButton: TextView = view.findViewById(R.id.changeImageButton)
        changeImageButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13+ (API 33 and above), request READ_MEDIA_IMAGES permission
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openImagePicker()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                // For older versions, use READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openImagePicker()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    handleImageSelected(imageUri)
                } else {
                    Log.e("ProfilePage", "No image URI returned.")
                }
            } else {
                Log.e("ProfilePage", "Image picker result code is not OK.")
            }
        }

        // Initialize permission launcher to request storage permission
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openImagePicker()
            } else {
                Log.e("ProfilePage", "Permission not granted.")
            }
        }

        // Load previously saved profile image
        loadProfileImage()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }
    private fun loadProfileImage() {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedImagePath = sharedPref.getString("profile_image_path", null)
        if (savedImagePath != null) {
            val imageUri = Uri.fromFile(File(savedImagePath))
            profileImageView.setImageURI(imageUri)
            profileImageView.post {
                profileImageView.setImageURI(imageUri) // Force update
            }
        } else {
            profileImageView.setImageResource(R.drawable.loading) // Set default image if no path is saved
        }
    }


    private fun clearProfileImage() {
        profileImageView.setImageResource(R.drawable.loading) // Clear the image and set a default one

        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedImagePath = sharedPref.getString("profile_image_path", null)
        if (savedImagePath != null) {
            val imageFile = File(savedImagePath)
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    Log.d("ProfilePage", "Profile image deleted successfully.")
                } else {
                    Log.e("ProfilePage", "Failed to delete the profile image.")
                }
            }
        }

        // Clear the saved image path from SharedPreferences
        with(sharedPref.edit()) {
            remove("profile_image_path")
            apply()
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri): String? {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
            inputStream?.use {
                val file = File(requireActivity().filesDir, "profile_image.jpg")
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                return file.absolutePath // Return the local path of the image
            }
        } catch (e: Exception) {
            Log.e("ProfilePage", "Error saving image: ${e.message}")
        }
        return null
    }
    private fun handleImageSelected(imageUri: Uri) {
        val localImagePath = saveImageToInternalStorage(imageUri)
        if (localImagePath != null) {
            profileImageView.setImageURI(Uri.parse(localImagePath))
            saveProfileImagePath(localImagePath) // Save the local path in SharedPreferences
        } else {
            Log.e("ProfilePage", "Failed to save image to internal storage.")
        }
    }

    private fun saveProfileImagePath(imagePath: String) {
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_image_path", imagePath)
            apply()
        }
    }



    private fun setupSocialMediaLinks(view: View) {
        val instaImageView: ImageView = view.findViewById(R.id.insta)
        instaImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.instagram.com/pushpendra840?igsh=MWVtbnB6OW5ua3hkdg==")
            startActivity(intent)
        }

        val facebookImageView: ImageView = view.findViewById(R.id.facebook)
        facebookImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.facebook.com/share/AyjaSx4wKaPoQ1fX/?mibextid=qi2Omg")
            startActivity(intent)
        }

        val twitterImageView: ImageView = view.findViewById(R.id.Twitter)
        twitterImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://x.com/Pushpendra840s?t=yyQYc9YuZ390Wd34MXpHig&s=08")
            startActivity(intent)
        }
    }
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogTheme)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            loginManager.logout()
            clearProfileImage() // Clear profile image on logout
            val intent = Intent(activity, loginpage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()


        }
        builder.create().show()

    }


    override fun onResume() {
        super.onResume()
        // Re-check login status when the fragment is resumed
        if (!loginManager.isLoggedIn()) {
            clearProfileImage()
        }
    }
}
