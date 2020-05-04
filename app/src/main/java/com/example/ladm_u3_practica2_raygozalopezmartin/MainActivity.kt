package com.example.ladm_u3_practica2_raygozalopezmartin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_Pedido.setOnClickListener {
            var otraVentana = Intent(this, Main2Activity::class.java)
            startActivity(otraVentana)
        }

        btn_Buscar.setOnClickListener {
            var otraVentana = Intent(this, Main3Activity::class.java)
            startActivity(otraVentana)
        }

    }
}
