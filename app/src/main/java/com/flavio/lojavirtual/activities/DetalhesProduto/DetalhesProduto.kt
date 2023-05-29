package com.flavio.lojavirtual.activities.DetalhesProduto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.flavio.lojavirtual.R
import com.flavio.lojavirtual.databinding.ActivityDetalhesProdutoBinding

class DetalhesProduto : AppCompatActivity() {

    lateinit var binding: ActivityDetalhesProdutoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foto = intent.extras?.getString("foto")
        val nome = intent.extras?.getString("nome")
        val preco = intent.extras?.getString("preco")

        Glide.with(this).load(foto).into(binding.dtFotoProduto)
        binding.dtNomeProduto.text = nome
        binding.dtPrecoproduto.text = "R$ ${preco}"
    }
}