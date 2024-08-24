import android.content.ContentValues
import android.content.Context

// Data class to represent a User
data class User(val id: Long = 0, val name: String, val email: String, val password: String)

// Function to insert a new user
fun insertUser(context: Context, user: User) {
    val dbHelper = DatabaseHelper(context)
    val db = dbHelper.writableDatabase

    val values = ContentValues().apply {
        put("name", user.name)
        put("email", user.email)
        put("password", user.password)
    }

    db.insert("users", null, values)
    db.close()
}

// Function to get all users
fun getAllUsers(context: Context): List<User> {
    val dbHelper = DatabaseHelper(context)
    val db = dbHelper.readableDatabase

    val projection = arrayOf("id", "name", "email", "password")
    val cursor = db.query("users", projection, null, null, null, null, null)

    val users = mutableListOf<User>()
    with(cursor) {
        while (moveToNext()) {
            val userId = getLong(getColumnIndexOrThrow("id"))
            val userName = getString(getColumnIndexOrThrow("name"))
            val userEmail = getString(getColumnIndexOrThrow("email"))
            val userPassword = getString(getColumnIndexOrThrow("password"))
            users.add(User(userId, userName, userEmail, userPassword))
        }
    }
    cursor.close()
    db.close()
    return users
}