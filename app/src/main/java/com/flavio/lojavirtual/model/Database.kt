package com.flavio.lojavirtual.model

import android.util.Log
import android.widget.TextView
import com.flavio.lojavirtual.adapter.AdapterProduto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Database {
    fun salvarDadosUsuario(nome: String){

        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf(
            "nome" to nome
        )

        val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documentReference.set(usuarios).addOnSuccessListener {
            Log.d("DB", "Sucesso ap salvar os dados!!")
        }.addOnFailureListener { erro ->
            Log.d("DB_ERROR", "Erro ao salvar os dados !  ${erro.printStackTrace()}")
        }
    }

    fun recuperarDadosUsuarioPerfil(nomeUsuario: TextView, emailUsuario: TextView){
        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid
        val email = FirebaseAuth.getInstance().currentUser!!.email
        val db = FirebaseFirestore.getInstance()

        val documentReference: DocumentReference = db.collection("Usuarios").document(usuarioID)
        documentReference.addSnapshotListener { documento, error ->
            if (documento != null){
                nomeUsuario.text = documento.getString("nome")
                emailUsuario.text = email
            }
        }
    }

    fun obterListaDeProdutos(lista_produtos: MutableList<Produto>, adapter_produto: AdapterProduto){
        val db = FirebaseFirestore.getInstance()
        db.collection("Produtos").get()
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful){
                    for (documento in tarefa.result!!){
                        val produtos = documento.toObject(Produto::class.java)
                        lista_produtos.add(produtos)
                        adapter_produto.notifyDataSetChanged()
                    }
                }
            }

    }
}