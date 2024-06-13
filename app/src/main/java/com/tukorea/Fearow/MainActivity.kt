package com.tukorea.Fearow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class User(val email: String, val nickname: String, val profilePictureUrl: String, val userLocation: String)
//data class Post(val postId: Int, val userId: String, val title: String, val content: String, val price: Int)

class MainActivity : AppCompatActivity() {
    private lateinit var titleInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleInput = findViewById(R.id.postTitleInput)
        priceInput = findViewById(R.id.priceInput)
        contentInput = findViewById(R.id.contentInput)
        buttonSubmit = findViewById(R.id.commit)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val maxPostIdRef = database.getReference("maxPostId")

        // 사용자 정보 저장
        val userEmail = "user@example.com"
        val usersRef = database.getReference("users")
        val encodedEmail = userEmail.replace(".", ",")



        val user = User(
            email = userEmail,
            nickname = "UserNickname",
            profilePictureUrl = "https://example.com/user/profile.jpg",
            userLocation = "siheung"
        )
        usersRef.child(encodedEmail).setValue(user)

        buttonSubmit.setOnClickListener {
            val titleText = titleInput.text.toString()
            val priceText = priceInput.text.toString()
            val contentText = contentInput.text.toString()

            try {
                val priceValue = priceText.toInt()

                // maxPostId 값을 읽어옴
                maxPostIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var postId = dataSnapshot.getValue(Int::class.java) ?: 0
                        postId += 1

                        // 게시글 정보 저장
                        val postRef = database.getReference("posts")

                        val post = Post(
                            postId = postId,
                            userId = userEmail,
                            title = titleText,
                            content = contentText,
                            price = priceValue
                        )
                        postRef.child(postId.toString()).setValue(post)

                        // maxPostId 업데이트
                        maxPostIdRef.setValue(postId)

                        // 게시판 화면으로 이동
                        val intent = Intent(this@MainActivity, BoardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Failed to read maxPostId", Toast.LENGTH_SHORT).show()
                    }
                })
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid number for price", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // 뒤로가기를 누르면 게시판 화면으로 이동
        val intent = Intent(this, BoardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
