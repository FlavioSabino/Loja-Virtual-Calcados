package com.flavio.lojavirtual.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat.startActivityForResult
import com.flavio.lojavirtual.activities.FormLogin.FormLogin
import com.flavio.lojavirtual.databinding.DialogPerfilUsuarioBinding
import com.flavio.lojavirtual.model.Database
import com.google.firebase.auth.FirebaseAuth

class DialogPerfilUsuario(private val activity: Activity) {

    lateinit var dialog: AlertDialog
    lateinit var binding: DialogPerfilUsuarioBinding
    lateinit var selectdImg : Uri


    fun iniciarPerfilUsuario(){
        val builder = AlertDialog.Builder(activity)
        binding = DialogPerfilUsuarioBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
    }

    fun recuperarDadosUsuarioBanco(){
        val nomeUsuario = binding.txtNomeUsuario
        val emailUsuario = binding.txtEmailUsuario
        val db = Database()
        db.recuperarDadosUsuarioPerfil(nomeUsuario,emailUsuario)

        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity,FormLogin::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

}