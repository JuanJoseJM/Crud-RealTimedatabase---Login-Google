package com.example.crudrealdatabaselogingoogle

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudrealdatabaselogingoogle.adapters.ArticuloAdapter
import com.example.crudrealdatabaselogingoogle.databinding.ActivityPrincipalBinding
import com.example.crudrealdatabaselogingoogle.models.Articulo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding
    var adapter = ArticuloAdapter(mutableListOf<Articulo>(), { item -> borrarItem(item) }, { item -> editarItem(item) })
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("agenda")
        setRecycler()
        setListeners()
        setMenuLateral()
    }

    private fun setMenuLateral() {
        binding.navigationview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.item_salir -> {
                    finishAffinity()
                    true
                }
                R.id.item_borrar -> {
                    borrarTodo()
                    true
                }
                else -> false
            }
        }
    }

    private fun borrarTodo() {
        database.removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Todos los artículos fueron eliminados", Toast.LENGTH_SHORT).show()
                recuperarDatosAgenda()
            } else {
                Toast.makeText(this, "Hubo un problema al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recagenda.layoutManager = layoutManager

        binding.recagenda.adapter = adapter
        recuperarDatosAgenda()
    }

    private fun recuperarDatosAgenda() {
        database.get().addOnSuccessListener { snapshot ->
            val todosLosRegistros = snapshot.children.mapNotNull { it.getValue(Articulo::class.java) }.toMutableList()
            adapter.lista = todosLosRegistros
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Error al recuperar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        binding.floatingActionButton.setOnClickListener {
            irActivityAdd()
        }
    }

    private fun irActivityAdd(bundle: Bundle? = null) {
        val i = Intent(this, AddActivity::class.java)
        if (bundle != null) {
            i.putExtras(bundle)
        }
        startActivity(i)
    }

    private fun borrarItem(item: Articulo) {
        val itemKey = item.nombre.replace(" ", "_")
        database.child(itemKey).removeValue()
            .addOnSuccessListener {
                val position = adapter.lista.indexOf(item)
                if (position != -1) {
                    adapter.lista.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Toast.makeText(this, "Artículo borrado", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al borrar el artículo", Toast.LENGTH_SHORT).show()
            }
    }

    private fun editarItem(item: Articulo) {
        val b = Bundle().apply {
            putSerializable("ITEM", item)
        }
        irActivityAdd(b)
    }

    override fun onResume() {
        super.onResume()
        recuperarDatosAgenda()
    }
}