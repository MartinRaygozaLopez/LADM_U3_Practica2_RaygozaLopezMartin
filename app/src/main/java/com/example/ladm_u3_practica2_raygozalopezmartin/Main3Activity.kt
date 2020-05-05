package com.example.ladm_u3_practica2_raygozalopezmartin

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main3.*

class Main3Activity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        btn_Bus.setOnClickListener {
            construirDialogo()
        }

        baseRemota.collection("restaurante")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    Toast.makeText(this, "ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG)
                        .show()
                    return@addSnapshotListener
                }
                dataLista.clear()
                listaID.clear()
                for(document in querySnapshot!!){
                    var cadena = "Nombre: " + document.getString("nombre") + "\nDomicilio: " + document.getString("domicilio") +
                            "\nCelular: " + document.getString("celular") + "\nProducto: " + document.get("pedido.producto") +
                            "\nEntregado: " + document.get("pedido.entregado")
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }
                if(dataLista.size == 0){
                    dataLista.add("NO HAY DATA")
                }
                var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
                lista.adapter = adapter
            }

        lista.setOnItemClickListener { parent, view, position, id ->
            if(listaID.size == 0){
                return@setOnItemClickListener
            }

            alertaEliminaActualizar(position)
        }
    }

    private fun construirDialogo() {
        var dialogo = Dialog(this)
        dialogo.setContentView(R.layout.consulta)

        //Declarar objetos
        var valor = dialogo.findViewById<EditText>(R.id.valor)
        var posicion = dialogo.findViewById<Spinner>(R.id.clave)
        var buscar = dialogo.findViewById<Button>(R.id.Buscar)
        var cerrar = dialogo.findViewById<Button>(R.id.cerrar)
        var resultado = dialogo.findViewById<TextView>(R.id.resultado)

        dialogo.show()

        cerrar.setOnClickListener {
            dialogo.dismiss()
        }

        buscar.setOnClickListener {
            if(valor.text.isEmpty()){
                Toast.makeText(this, "DEBES PONER VALOR PARA BUSCAR", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            var valor = valor.text.toString()
            var clave = posicion.selectedItemPosition

            when(clave){
                0->{
                    baseRemota.collection("restaurante")
                        .whereEqualTo("nombre", valor)
                        .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                resultado.setText("ERROR, NO HAY CONEXION")
                                return@addSnapshotListener
                            }

                            var res = ""
                            for(document in  querySnapshot!!){
                                res += "ID: " + document.id + "\nNombre: " + document.getString("nombre") +
                                        "\nCelular: " + document.getString("celular") + "\nDomicilio: " + document.getString("domicilio") +
                                        "\nProducto: " + document.get("pedido.producto") + "\nPrecio: " + document.get("pedido.precio")+
                                        "\nCantidad: " + document.get("pedido.cantidad") + "\nEntregado: " + document.get("pedido.entregado") + "\n\n"
                            }
                            resultado.setText(res)
                        }
                }
                1->{
                    baseRemota.collection("restaurante")
                        .whereEqualTo("celular", valor)
                        .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                            if(firebaseFirestoreException != null){
                                resultado.setText("ERROR, NO HAY CONEXION")
                                return@addSnapshotListener
                            }

                            var res = ""
                            for(document in  querySnapshot!!){
                                res += "ID: " + document.id + "\nNombre: " + document.getString("nombre") +
                                        "\nCelular: " + document.getString("celular") + "\nDomicilio: " + document.getString("domicilio") +
                                        "\nProducto: " + document.get("pedido.producto") + "\nPrecio: " + document.get("pedido.precio")+
                                        "\nCantidad: " + document.get("pedido.cantidad") + "\nEntregado: " + document.get("pedido.entregado") + "\n\n"
                            }
                            resultado.setText(res)
                        }
                }
                2->{baseRemota.collection("restaurante")
                    .whereEqualTo("producto", valor)
                    .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                        if(firebaseFirestoreException != null){
                            resultado.setText("ERROR, NO HAY CONEXION")
                            return@addSnapshotListener
                        }

                        var res = ""
                        for(document in  querySnapshot!!){
                            res += "ID: " + document.id + "\nNombre: " + document.getString("nombre") +
                                    "\nCelular: " + document.getString("celular") + "\nDomicilio: " + document.getString("domicilio") +
                                    "\nProducto: " + document.get("pedido.producto") + "\nPrecio: " + document.get("pedido.precio")+
                                    "\nCantidad: " + document.get("pedido.cantidad") + "\nEntregado: " + document.get("pedido.entregado") + "\n\n"
                        }
                        resultado.setText(res)
                    }}
            }

        }
    }

    private fun alertaEliminaActualizar(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("¿Qué deseas hacer con\n ${dataLista[position]}?")
            .setPositiveButton("Eliminar"){d, w ->
                eliminar(listaID[position])
            }
            .setNegativeButton("Actualizar"){d, w ->
                llamarVentanaActualizar(listaID[position])
            }
            .setNeutralButton("Cancelar"){d, w -> }
            .show()
    }

    private fun llamarVentanaActualizar(idActualizar : String) {
        baseRemota.collection("restaurante")
            .document(idActualizar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this, Main4Activity::class.java)
                v.putExtra("id", idActualizar)
                v.putExtra("nombre", it.getString("nombre"))
                v.putExtra("domicilio", it.getString("domicilio"))
                v.putExtra("celular", it.getString("celular"))
                v.putExtra("producto", it.get("pedido.producto").toString())
                v.putExtra("precio", it.get("pedido.precio").toString())
                v.putExtra("cantidad", it.get("pedido.cantidad").toString())
                v.putExtra("entregado", it.get("pedido.entregado").toString())
                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR! NO HAY CONEXION DE RED", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun eliminar(idEliminar : String) {
        baseRemota.collection("restaurante")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "SE ELIMINO CON EXITO", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "NO SE PUDO ELIMINAR", Toast.LENGTH_LONG)
                    .show()
            }
    }
}
