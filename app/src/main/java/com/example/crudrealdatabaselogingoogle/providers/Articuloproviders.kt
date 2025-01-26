package com.example.crudrealdatabaselogingoogle.providers

import com.example.crudrealdatabaselogingoogle.models.Articulo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Articuloproviders {
    private val database= FirebaseDatabase.getInstance().getReference("agenda")
    fun getDatos(datosAgenda: (MutableList<Articulo>) -> Unit) {
        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listado= mutableListOf<Articulo>()
                for (item in snapshot.children){
                    val valor = item.getValue(Articulo::class.java)
                    if (valor!=null){
                        listado.add(valor)
                    }
                }
                listado.sortBy { it.nombre }
                datosAgenda(listado)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al leer realtime: ${error.message}")
            }
        })
    }
}