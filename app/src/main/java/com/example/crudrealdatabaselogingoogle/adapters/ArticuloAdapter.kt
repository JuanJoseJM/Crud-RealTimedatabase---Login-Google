package com.example.crudrealdatabaselogingoogle.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealdatabaselogingoogle.R
import com.example.crudrealdatabaselogingoogle.models.Articulo

class ArticuloAdapter (
    var lista: MutableList<Articulo>,
    private var onBorrar: (Articulo) -> Unit,
    private var onEditar: (Articulo) -> Unit,
): RecyclerView.Adapter<ArticuloViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.articulo_layout, parent, false)
        return ArticuloViewHolder(view)
    }

    override fun getItemCount()=lista.size

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.render(lista[position], onBorrar, onEditar)
    }
}