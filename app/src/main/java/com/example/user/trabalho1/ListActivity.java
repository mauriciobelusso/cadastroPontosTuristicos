package com.example.user.trabalho1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.trabalho1.entidade.PontosTuristicos;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listPontosTuristicos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listPontosTuristicos = (ListView) findViewById(R.id.lvListaPontosTuristicos);

        //trás o item clicado na lista
        listPontosTuristicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PontosTuristicos pontosTuristicos = (PontosTuristicos) listPontosTuristicos.getItemAtPosition(i);
                Intent intentVaiProFormulario = new Intent(ListActivity.this, PontosTuristicosActivity.class);
                //mandando junto com a intent o ponto turístico para ele ser recuperado lá no formulário, sendo necessário para isso serealizar o objeto para transformá-lo em binário pois senão o java não aceita
                //enviar um objeto de uma activity para a outra
                intentVaiProFormulario.putExtra("pontoTuristico", pontosTuristicos);
                startActivity(intentVaiProFormulario);
            }
        });


        //registrando que a lista possui um menu de contexto
        registerForContextMenu(listPontosTuristicos);
    }

    private void carregarLista(){
        //cria o databaseHandler
        DatabaseHandler pontosTuristicosDatabase = new DatabaseHandler(this);
        //joga o resultado nessa lista de pontos turisticos
        List<PontosTuristicos> pontosTuristicosList = pontosTuristicosDatabase.buscaPontosTuristicos();


        //o ArrayAdapter vai converter cada um dos pontos turisticos em um textView e vai jogar na lista. Pois o listview só lê view não le string...
        ArrayAdapter<PontosTuristicos> adapter = new ArrayAdapter<PontosTuristicos>(this, android.R.layout.simple_list_item_1, pontosTuristicosList);
        listPontosTuristicos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //menu.add = além de adicionar no menu ele retorna a referência do item que ele acabou de gerar dentro do menu
        MenuItem deletar = menu.add("Excluir");
        //Menu Item se refere ao Item deletar (item do menu de contexto) não ao item da lista
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                PontosTuristicos pontosTuristicos = (PontosTuristicos) listPontosTuristicos.getItemAtPosition(info.position);

                DatabaseHandler databaseHandler = new DatabaseHandler(ListActivity.this);
                databaseHandler.deleta(pontosTuristicos);
                databaseHandler.close();

                Toast.makeText(ListActivity.this, "Deletar o ponto turístico" + pontosTuristicos.getNome(), Toast.LENGTH_SHORT).show();

                carregarLista();
                return false;
            }
        });
    }


}
