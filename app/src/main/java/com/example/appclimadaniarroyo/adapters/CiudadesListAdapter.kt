package com.example.customadapterlistviewexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appclimadaniarroyo.R
import com.example.appclimadaniarroyo.database.Ciudad


class CiudadesListAdapter(
    var listaCiudades: MutableList<Ciudad>,
    private val listener: ICiudadRecycler
) : RecyclerView.Adapter<CiudadesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_ciudad, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(listaCiudades[position], listener, position)
    }

    override fun getItemCount(): Int {
        return listaCiudades.size
    }

    fun setData(listaCiudades: MutableList<Ciudad>) {
        this.listaCiudades = listaCiudades
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(ciudad: Ciudad, listener: ICiudadRecycler, position: Int) {
            val textViewCiudad = itemView.findViewById<TextView>(R.id.textViewCiudad)
            val btnBorrarCiudad = itemView.findViewById<Button>(R.id.btnBorrarCiudad)
            val btnEditarCiudad = itemView.findViewById<Button>(R.id.btnEditarCiudad)
            textViewCiudad.text = ciudad.ciudad
            itemView.setOnClickListener {
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCiudadClick(ciudad)
                }
            }
            btnBorrarCiudad.setOnClickListener {
                listener.borraCiudad(ciudad, position)
            }

            btnEditarCiudad.setOnClickListener {
                listener.editarCiudad(ciudad, position)
            }

        }
    }

    interface ICiudadRecycler {
        fun onCiudadClick(ciudad: Ciudad)
        fun borraCiudad(ciudad: Ciudad, position: Int)
        fun editarCiudad(ciudad: Ciudad, position: Int)
    }
}