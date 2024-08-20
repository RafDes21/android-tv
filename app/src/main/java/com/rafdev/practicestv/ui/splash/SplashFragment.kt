package com.rafdev.practicestv.ui.splash

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafdev.practicestv.R
import com.rafdev.practicestv.databinding.FragmentSplahBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplahBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        populateRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ItemAdapter(createItemList())
        recyclerView.adapter = adapter
    }

    private fun createItemList(): List<String> {
        val itemList = mutableListOf<String>()
        for (i in 1..50) {
            itemList.add("Item $i")
        }
        return itemList
    }

    private fun populateRecyclerView() {
        adapter.notifyDataSetChanged()
    }
}

class ItemAdapter(private val itemList: List<String>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var lastFocusedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)

        // Agregar un listener de foco al itemView
        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            val context = holder.itemView.context
            if (hasFocus) {
                holder.itemView.setBackgroundColor(Color.WHITE)

//                // Verificar si es la primera vez que obtiene foco
//                if (lastFocusedPosition == RecyclerView.NO_POSITION) {
//                    lastFocusedPosition = position
//                } else {
//                    // Determinar la dirección del desplazamiento del foco
//                    val direction = if (position > lastFocusedPosition) {
//                        Log.d("FocusDirection", "abajo")
//                    } else {
//                        Log.d("FocusDirection", "arriba")
//                    }
//
//                    // Guardar la posición actual como la última posición con foco
//                    lastFocusedPosition = position
//
//                    // Imprimir la dirección del desplazamiento (para demostración)
//                    Log.d("FocusDirection", "Foco entró desde $direction")
//                }

                // Verificar si la posición actual es la 6
                if (position == 5) { // posición 6 (considerando el índice base 0)
                    // Obtener el RecyclerView desde el contexto del itemView
                    val recyclerView = holder.itemView.parent as RecyclerView

                    // Calcular el desplazamiento requerido en dp (-20dp)
                    val offsetDp = -20

                    // Llamar al método para desplazar el RecyclerView
                    scrollToPositionWithOffset(recyclerView, position, offsetDp)
                }
            } else {
                val focusColor = ContextCompat.getColor(context, R.color.test)
                holder.itemView.setBackgroundColor(focusColor)
            }
        }
    }

    private fun scrollToPositionWithOffset(recyclerView: RecyclerView, position: Int, offsetDp: Int) {
        // Convertir dp a píxeles
        val pixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            offsetDp.toFloat(),
            recyclerView.resources.displayMetrics
        ).toInt()

        // Obtener el LinearLayoutManager del RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        // Calcular la posición del elemento superior visible
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        // Obtener el desplazamiento actual del elemento superior
        val topView = layoutManager.findViewByPosition(firstVisibleItemPosition)
        val currentScrollY = topView?.top ?: 0

        // Calcular el desplazamiento objetivo
        val targetScrollY = currentScrollY + pixels

        // Realizar el desplazamiento con una animación suave
        recyclerView.smoothScrollBy(0, targetScrollY)
    }

    override fun getItemCount(): Int = itemList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            itemView.findViewById<TextView>(R.id.texto).text = item
        }
    }
}
