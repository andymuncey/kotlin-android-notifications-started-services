package com.tinyappco.notificationdemo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tinyappco.notificationdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupPermissions()

        binding.button.setOnClickListener { scheduleAlert() }
    }

    private fun setupPermissions(){
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                permitted: Boolean ->
            if (!permitted){
                Toast.makeText(this,"Permission not granted, this app will not function", Toast.LENGTH_LONG).show()
            }
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun scheduleAlert(){
        val message = binding.etMessage.text.toString()
        val hours = binding.etHours.text.toString().toLongOrNull() ?: 0
        val mins = binding.etMinutes.text.toString().toLongOrNull() ?: 0
        val delay = (hours * 60) + mins

//        val notifier = Notifier(this)
//        notifier.sendNotification(message,"")

        val intent = Intent(this, NotificationService::class.java)

        intent.putExtra("title",message)
        intent.putExtra("delay", delay)
        startService(intent)

        binding.etHours.text.clear()
        binding.etMinutes.text.clear()
        binding.etMessage.text.clear()
        Toast.makeText(this,"Reminder to $message set in $delay minutes",Toast.LENGTH_SHORT).show()
    }
}