package com.example.crudrealdatabaselogingoogle.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealdatabaselogingoogle.databinding.ArticuloLayoutBinding
import com.example.crudrealdatabaselogingoogle.models.Articulo

class ArticuloViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding= ArticuloLayoutBinding.bind(v)
    fun render(item: Articulo, onBorrar: (Articulo)->Unit, onEdit: (Articulo)->Unit){
        binding.tvNombre.text=item.nombre
        binding.tvDesc.text=item.descripcion
        binding.tvPrecio.text=item.precio.toString()
        binding.btnBorrar.setOnClickListener {
            onBorrar(item)
        }
        binding.btnAdd.setOnClickListener {
            onEdit(item)
        }
    }
}
