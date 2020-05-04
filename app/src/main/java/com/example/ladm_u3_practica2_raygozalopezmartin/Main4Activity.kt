package com.example.ladm_u3_practica2_raygozalopezmartin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main4.*

class Main4Activity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        var extras = intent.extras
        id = extras!!.getString("id").toString()
        txt_ENombre.setText(extras.getString("nombre"))
        txt_EDomicilio.setText(extras.getString("domicilio"))
        txt_ECelular.setText(extras.getString("celular"))
        txt_EDes.setText(extras.getString("producto"))
        txt_EPrecio.setText(extras.getString("precio"))
        txt_ECantidad.setText(extras.getString("cantidad"))
        ckb_EEntregado.isChecked = extras.getString("entregado")!!.toBoolean()

        btn_Editar.setOnClickListener {
            baseRemota.collection("restaurante")
                .document(id)
                .update("nombre", txt_ENombre.text.toString(),
                    "domicilio", txt_EDomicilio.text.toString(),
                    "celular", txt_ECelular.text.toString(), "pedido.producto", txt_EDes.text.toString(),
                    "pedido.precio", txt_EPrecio.text.toString().toFloat(), "pedido.cantidad", txt_ECantidad.text.toString().toInt(),
                    "pedido.entregado", ckb_EEntregado.isChecked)
                .addOnSuccessListener {
                    Toast.makeText(this, "ACTUALIZACION REALIZADA", Toast.LENGTH_LONG)
                        .show()
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "ERROR! NO SE PUDO ACTUALIZAR", Toast.LENGTH_LONG)
                        .show()
                }
        }

        btn_ECancelar.setOnClickListener {
            finish()
        }
    }
}
