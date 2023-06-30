package com.flavio.lojavirtual.activities.RecuperarSenha

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.flavio.lojavirtual.R
import com.flavio.lojavirtual.databinding.ActivityRecuperarSenhaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RecuperarSenha : AppCompatActivity() {

    lateinit var binding: ActivityRecuperarSenhaBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btRecuperarSenha.setOnClickListener { view ->

            val email = binding.editEmail.text.toString()

            if (email.isEmpty()){
                val snackbar = Snackbar.make(view, "Preencha o campo de email!", Snackbar.LENGTH_SHORT)
                snackbar.setTextColor(Color.WHITE)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener { redefinirSenha ->
                    if (redefinirSenha.isSuccessful){
                        val snackbar = Snackbar.make(view,"Você vai receber no seu email um link para redefinir a sua senha!",Snackbar.LENGTH_SHORT)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.verde_800))
                        snackbar.show()
                    }
                }.addOnFailureListener { exception ->
                    val mensagemErro = when(exception){
                        is FirebaseAuthInvalidCredentialsException ->"Digite um email Válido!"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao recuperar a senha!"
                    }
                    val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }
            }
        }
    }
}