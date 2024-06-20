package com.tukorea.Fearow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class BoardActivity : AppCompatActivity() {
    private lateinit var buttonWritePost: Button
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        buttonWritePost = findViewById(R.id.buttonWritePost)
        postRecyclerView = findViewById(R.id.postRecyclerView)

        buttonWritePost.setOnClickListener {
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        val postRef = database.getReference("posts")

        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(mutableListOf())
        postRecyclerView.adapter = postAdapter

        postRef.orderByChild("postId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts = mutableListOf<Post>()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(Post::class.java)
                    post?.let { posts.add(it) }
                }
                posts.sortByDescending { it.postId }
                postAdapter.updatePosts(posts)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    inner class PostAdapter(private var posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

        inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val imageView: ImageView = itemView.findViewById(R.id.postImageView)
            private val titleView: TextView = itemView.findViewById(R.id.postTitleView)
            private val contentView: TextView = itemView.findViewById(R.id.postContentView)
            private val priceView: TextView = itemView.findViewById(R.id.postPriceView)

            fun bind(post: Post) {
                if (post.imageUrl.isNotEmpty()) {
                    Picasso.get().load(post.imageUrl).into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.placeholder_image) // Placeholder image
                }
                titleView.text = post.title
                contentView.text = post.content
                priceView.text = "${post.price}원"
                itemView.setOnClickListener {
                    val intent = Intent(this@BoardActivity, PostDetailActivity::class.java).apply {
                        putExtra("postId", post.postId)
                        putExtra("userId", post.userId)
                        putExtra("title", post.title)
                        putExtra("content", post.content)
                        putExtra("price", post.price)
                        putExtra("imageUrl", post.imageUrl)
                    }
                    startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
            return PostViewHolder(view)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            holder.bind(posts[position])
        }

        override fun getItemCount(): Int = posts.size

        fun updatePosts(newPosts: List<Post>) {
            posts.clear()
            posts.addAll(newPosts)
            notifyDataSetChanged()
        }
    }
}