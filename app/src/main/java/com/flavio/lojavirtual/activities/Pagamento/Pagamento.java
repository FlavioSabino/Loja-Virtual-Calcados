package com.flavio.lojavirtual.activities.Pagamento;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.flavio.lojavirtual.R;
import com.flavio.lojavirtual.databinding.ActivityPagamentoBinding;
import com.flavio.lojavirtual.interfaceMercadoPago.ComunicacaoServidorMP;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pagamento extends AppCompatActivity {

    ActivityPagamentoBinding binding;
    private String tamanho_calcado;
    private String nome;
    private String preco;

    private final String PUBLIC_KAY = "TEST-452675bf-46bb-48ca-8468-7975106b7263";
    private final String ACCESS_TOKEN = "TEST-8714136138335541-060521-3c3f2df30db5ecc461d9c72f74627c7f-286077356";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         tamanho_calcado = getIntent().getExtras().getString("tamanho_calcado");
         nome = getIntent().getExtras().getString("nome");
         preco = getIntent().getExtras().getString("preco");

        binding.btFazerPagamento.setOnClickListener(view -> {
            String bairro = binding.editBairro.getText().toString();
            String rua_numero = binding.editRuaNumero.getText().toString();
            String cidade_estado = binding.editCidadeEstado.getText().toString();
            String celular = binding.editCelular.getText().toString();

            if(bairro.isEmpty() || rua_numero.isEmpty() || cidade_estado.isEmpty() || celular.isEmpty()){
                Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos!",Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }else {
                criarJsonObject();
            }
        });
    }

    private void criarJsonObject(){
        JsonObject dados = new JsonObject();

        JsonArray item_lista = new JsonArray();
        JsonObject item;

        JsonObject email = new JsonObject();

        item = new JsonObject();
        item.addProperty("title",nome);
        item.addProperty("quantity",1);
        item.addProperty("currency_id","BRL");
        item.addProperty("unit_price",Double.parseDouble(preco));
        item_lista.add(item);

        dados.add("items",item_lista);

        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.addProperty("email",emailUsuario);
        dados.add("payer",email);

        criarPreferenciaPagamento(dados);

    }

    private  void criarPreferenciaPagamento(JsonObject dados){

        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token" + ACCESS_TOKEN;

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ComunicacaoServidorMP conexao_pagamento = retrofit.create(ComunicacaoServidorMP.class);
        Call<JsonObject> request = conexao_pagamento.enviarPagamento(url,dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String preferenceId = response.body().get("id").getAsString();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}