package com.example.ladm_u3_practica2_raygozalopezmartin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn_Agregar.setOnClickListener {
            if(txt_Nombre.text.isEmpty() || txt_Domicilio.text.isEmpty() || txt_Celular.text.isEmpty() || txt_Des.text.isEmpty() ||
                    txt_Precio.text.isEmpty() || txt_Cantidad.text.isEmpty()) {
                mensaje("FAVOR DE LLENAR TODOS LOS CAMPOS")
                return@setOnClickListener
            }

            agregarPedido()
        }

        btn_Cacelar.setOnClickListener {
            finish()
        }

    }

    private fun agregarPedido() {
        var data = hashMapOf(
            "nombre" to txt_Nombre.text.toString(),
            "domicilio" to txt_Domicilio.text.toString(),
            "celular" to txt_Celular.text.toString(),
            "pedido" to hashMapOf(
                "producto" to txt_Des.text.toString(),
                "precio" to txt_Precio.text.toString().toFloat(),
                "cantidad" to txt_Cantidad.text.toString().toInt(),
                "entregado" to ckb_Entregado.isChecked
            )
        )

        baseRemota.collection("restaurante")
            .add(data as Map<String, Any>)
            .addOnSuccessListener {
                mensaje("SE CAPTURO CON EXITO")
                finish()
            }
            .addOnFailureListener {
                mensaje("ERROR AL GUARDAR INFORMACION" + it.message.toString())
            }
    }

    fun mensaje(s : String){
        Toast.makeText(this, s, Toast.LENGTH_LONG)
            .show()
    }

}
