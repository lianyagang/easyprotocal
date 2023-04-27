package com.example.modulea

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.protocol.core.ProtocolFactory

class AModuleActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        findViewById<View>(R.id.button).setOnClickListener {
            val data = ProtocolFactory.getInstance().invoke(TestProtocol::class.java).data

            if (data == null) {
                Toast.makeText(this, "没有找到data", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            }
        }
    }
}