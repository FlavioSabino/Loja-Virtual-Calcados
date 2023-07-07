package com.flavio.lojavirtual.activities.formlogin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.flavio.lojavirtual.R
import com.flavio.lojavirtual.activities.FormCadastro.FormCadastro
import com.flavio.lojavirtual.activities.RecuperarSenha.RecuperarSenha
import com.flavio.lojavirtual.activities.TelaPrincipalProdutos.TelaPrincipalProdutos
import com.flavio.lojavirtual.databinding.ActivityFormLoginBinding
import com.flavio.lojavirtual.dialog.DialogCarregando
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FormLogin : AppCompatActivity() {

    lateinit var binding: ActivityFormLoginBinding
    lateinit var clienteLoginGoogle: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()

    val dialogCarregando = DialogCarregando(this)

    private val resultadoDoLoginGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val tarefa = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val conta = tarefa.getResult(ApiException::class.java)
                autenticarUsuarioGoogle(conta.idToken!!)
            }


        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleSignInOption()


        supportActionBar!!.hide()


        binding.btEntrar.setOnClickListener { view ->

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { tarefa ->
                        if (tarefa.isSuccessful) {
                            dialogCarregando.iniciarCarregamentoAlertDialog()
                            Handler(Looper.getMainLooper()).postDelayed({
                                irParaTelaProdutos()
                                dialogCarregando.liberarAlertDialog()
                            }, 3000)
                        }

                    }.addOnFailureListener {
                        val snackbar =
                            Snackbar.make(view, "Erro ao fazer o login!", Snackbar.LENGTH_SHORT)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()
                    }
            }
        }

        binding.txtTelaCadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }


        binding.txtRecuperarSenha.setOnClickListener {
            val intent = Intent(this, RecuperarSenha::class.java)
            startActivity(intent)
        }

        binding.btEntrarGoogle.setOnClickListener {
            loginGoogle()
        }

    }


    private fun googleSignInOption() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.oauth_client))
            .requestEmail()
            .build()

        clienteLoginGoogle = GoogleSignIn.getClient(this, gso)
    }

    private fun loginGoogle() {
        val intentEntrar = clienteLoginGoogle.signInIntent
        resultadoDoLoginGoogle.launch(intentEntrar)
    }

    private fun autenticarUsuarioGoogle(idToken: String) {

        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credencial).addOnCompleteListener { autenticacao ->
            if (autenticacao.isSuccessful) {
                dialogCarregando.iniciarCarregamentoAlertDialog()
                Handler(Looper.getMainLooper()).postDelayed({
                    irParaTelaProdutos()
                    dialogCarregando.liberarAlertDialog()
                }, 1000)
            }
        }
    }

    private fun irParaTelaProdutos() {
        val intent = Intent(this, TelaPrincipalProdutos::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null) {
            irParaTelaProdutos()
        }
    }
}