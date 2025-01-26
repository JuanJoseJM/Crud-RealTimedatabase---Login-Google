package com.example.crudrealdatabaselogingoogle

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudrealdatabaselogingoogle.databinding.ActivityAddBinding
import com.example.crudrealdatabaselogingoogle.models.Articulo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    private var nombre = ""
    private var descripcion = ""
    private var precio = 0.0
    private var editando = false
    private var itemArticulo = Articulo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListeners()
        recogerDatos()
    }

    private fun recogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            itemArticulo = datos.getSerializable("ITEM") as Articulo
            binding.tvTitulo.text = "EDITAR REGISTRO"
            binding.btnAdd.text = "EDITAR"
            editando = true
            pintarValores()
        }
    }

    private fun pintarValores() {
        binding.etNombre.isEnabled = false // El nombre no se puede editar
        binding.etNombre.setText(itemArticulo.nombre)
        binding.etDesc.setText(itemArticulo.descripcion)
        binding.etPrecio.setText(itemArticulo.precio.toString())
    }

    //----------------------------------------------------------------------------------------------
    private fun setListeners() {
        binding.btnCancelar.setOnClickListener {
            finish()
        }
        binding.btnAdd.setOnClickListener {
            addItem()
        }
    }

    //----------------------------------------------------------------------------------------------
    private fun addItem() {
        if (!datosOk()) return

        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("agenda")
        val item = Articulo(nombre, descripcion, precio)

        val nodo = nombre.replace(".", "_")

        database.child(nodo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && !editando) {
                    Toast.makeText(
                        this@AddActivity,
                        "El artículo ya está registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    database.child(nodo).setValue(item).addOnSuccessListener {
                        Toast.makeText(
                            this@AddActivity,
                            "Artículo guardado con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Cierra la actividad después de guardar
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@AddActivity,
                            "Error al guardar el artículo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@AddActivity,
                    "Error al acceder a la base de datos: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //----------------------------------------------------------------------------------------------
    private fun datosOk(): Boolean {
        nombre = binding.etNombre.text.toString().trim()
        if (nombre.length < 3) {
            binding.etNombre.error = "El nombre debe tener al menos 3 caracteres"
            return false
        }

        descripcion = binding.etDesc.text.toString().trim()
        if (descripcion.length < 3) {
            binding.etDesc.error = "La descripción debe tener al menos 3 caracteres"
            return false
        }

        val precioTexto = binding.etPrecio.text.toString().trim()
        if (precioTexto.isEmpty()) {
            binding.etPrecio.error = "Debes ingresar un precio"
            return false
        }

        precio = try {
            precioTexto.toDouble()
        } catch (e: NumberFormatException) {
            binding.etPrecio.error = "El precio debe ser un número válido"
            return false
        }

        if (precio < 1 || precio > 100000) {
            binding.etPrecio.error = "El precio debe estar entre 1 y 100,000"
            return false
        }
        return true
    }
}
