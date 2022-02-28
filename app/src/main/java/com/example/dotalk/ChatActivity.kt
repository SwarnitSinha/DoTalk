package com.example.dotalk

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.example.dotalk.R
import androidx.core.content.FileProvider
import com.example.dotalk.databinding.ActivityChatBinding
import com.example.dotalk.databinding.ActivityMainBinding
import java.security.AccessController.getContext


class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
//    private lateinit var camButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var binding: ActivityChatBinding


//    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var mDbRef: DatabaseReference

    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("tag","FLow comes to chat activity")
        //Extras from intent of User -> Chat
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

         val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        recieverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = binding.chatRecyclerView //findViewById(R.id.chatRecyclerView)
        messageBox = binding.messageBox //findViewById(R.id.messageBox)
        sendButton = binding.sendBtn //findViewById(R.id.sendBtn)

//        camButton = findViewById(R.id.cameraBtn)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)


        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        /*
        //result of open camera
        // this is new way to handle intent
        // onActivityResult is deprecated now
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    handleCameraImage(result.data)
                }
            }

        camButton.setOnClickListener {

            //intent to open camera app
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)

        }
*/


        //logic for adding data to Recycler View
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding the message to database
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)


            //created a node for messages
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            messageBox.setText("")
        }

    }
/*
    private fun handleCameraImage(intent: Intent?) {

        val bitmap = intent?.extras?.get("data") as Bitmap
        //created a node for messages
        mDbRef.child("chats").child(senderRoom!!).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(recieverRoom!!).child("messages").push()
                    .setValue(messageObject)
            }
//        ivPhoto.setImageBitmap(bitmap)

    }

*/


}