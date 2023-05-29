package com.flavio.lojavirtual.activities.FormCadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.flavio.lojavirtual.R
import com.flavio.lojavirtual.activities.FormLogin.FormLogin
import com.flavio.lojavirtual.databinding.ActivityFormCadastroBinding
import com.flavio.lojavirtual.model.Database
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormCadastro : AppCompatActivity() {

    lateinit var binding: ActivityFormCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        val database = Database()


        binding.btCadastrar.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(it,"Preencha todos campos!",Snackbar.LENGTH_SHORT)
                snackbar.setTextColor(Color.WHITE)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener {tarefa ->
                    if(tarefa.isSuccessful){
                        database.salvarDadosUsuario(nome)
                        val snackbar = Snackbar.make(it,"Cadastro realizado com sucesso!",Snackbar.LENGTH_SHORT)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.setBackgroundTint(ContextCompat.getColor(this,R.color.verde_800))
                        snackbar.show()
                    }
                }.addOnFailureListener { erroCadastro ->
                    val mensagemErro = when(erroCadastro){
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres"
                        is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada"
                        is FirebaseNetworkException -> "Sem conexão com a internet"
                        else -> "Erro ao cadastrar usuário"
                    }
                    val snackbar = Snackbar.make(it, mensagemErro,Snackbar.LENGTH_SHORT)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()

                }


            }

        }
    }
}