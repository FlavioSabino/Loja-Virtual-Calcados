package com.flavio.lojavirtual.activities.Pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.flavio.lojavirtual.R
import com.flavio.lojavirtual.adapter.AdapterPedido
import com.flavio.lojavirtual.databinding.ActivityPedidosBinding
import com.flavio.lojavirtual.model.Database
import com.flavio.lojavirtual.model.Pedido

class Pedidos : AppCompatActivity() {

    lateinit var binding:ActivityPedidosBinding
    lateinit var adapterPedidos: AdapterPedido
    val lista_pedidos: MutableList<Pedido> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler_pedidos = binding.recyclerPedidos
        recycler_pedidos.layoutManager = LinearLayoutManager(this)
        recycler_pedidos.setHasFixedSize(true)
        adapterPedidos = AdapterPedido(this,lista_pedidos)
        recycler_pedidos.adapter = adapterPedidos

        val db = Database()
        db.obterListaPedidos(lista_pedidos,adapterPedidos)
    }
}