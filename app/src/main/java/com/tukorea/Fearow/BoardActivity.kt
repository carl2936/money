package com.tukorea.Fearow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.tukorea.Fearow.databinding.BulletinBoardBinding

data class Post(
    val postId: Int = 0,
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val price: Int = 0
)


class BoardActivity : AppCompatActivity() {
    private lateinit var binding: BulletinBoardBinding
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BulletinBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val postsRef = database.getReference("posts")

        binding.buttonWritePost.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Setup RecyclerView
        postAdapter = PostAdapter(postList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postAdapter

        // Fetch posts from Firebase
        postsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (postSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                }
                // 최신 게시글이 최상단에 오도록 리스트를 역순으로 정렬
                postList.reverse()
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BoardActivity, "Failed to read posts", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
