package br.com.maceda.todo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.maceda.todo.adapter.RepositorioAdapter;
import br.com.maceda.todo.broadcast.NotificacaoReceiver;
import br.com.maceda.todo.dao.RepositorioDao;
import br.com.maceda.todo.util.Util;
import br.com.maceda.todo.vo.Repositorio;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private List<Repositorio> lista;
    private EditText edtDescricao;
    private RepositorioDao repositorioDao;
    private RecyclerView recyclerView;
    private RepositorioAdapter repositorioAdapter;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDataListener;

    public BroadcastReceiver atualizarReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            carregarDados();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        FloatingActionButton btnAdicionar = (FloatingActionButton) findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(onClickAdicionar());

        repositorioDao = RepositorioDao.getInstance(this);

        registerReceiver(atualizarReceiver, new IntentFilter("ATUALIZAR"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(atualizarReceiver);
    }

    private void carregarDados() {
        lista = repositorioDao.findAll();
        setAdapterRecyclerView(lista);
    }

    private void setAdapterRecyclerView(List<Repositorio> lista) {
        repositorioAdapter = new RepositorioAdapter(this, lista, onClickRecyclerView());
        recyclerView.setAdapter(repositorioAdapter);
    }

    private View.OnClickListener onClickAdicionar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDescricao.getText().toString().equals("")) {
                    edtDescricao.setError(getString(R.string.informe_descricao_do_lembrete));
                    edtDescricao.requestFocus();
                    return;
                }
                final Repositorio repositorio = new Repositorio(edtDescricao.getText().toString());

                onDataListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        repositorio.setDataHoraLong(calendar.getTimeInMillis());

                        TimePickerDialog dialogTime = new TimePickerDialog(MainActivity.this,
                                onTimeSetListener,
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE), true);
                        dialogTime.show();

                    }
                };

                onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = new Date(repositorio.getDataHoraLong());
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        repositorio.setDataHoraLong(calendar.getTimeInMillis());
                        repositorio.setDataHora(Util.getDateTime(repositorio.getDataHoraLong()));

                        salvarTodo(repositorio);
                    }
                };

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialogDate = new DatePickerDialog(MainActivity.this,
                        onDataListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialogDate.show();

            }
        };
    }

    private void salvarTodo(Repositorio repositorio) {
        long id = repositorioDao.save(repositorio);
        repositorio.setId(id);
        lista.add(repositorio);
        recyclerView.getAdapter().notifyDataSetChanged();
        agendarAlarme(repositorio);
        edtDescricao.setText("");
    }

    private void agendarAlarme(Repositorio repositorio) {
        Intent intentBroadCast = new Intent(this, NotificacaoReceiver.class);
        intentBroadCast.putExtra("NOTIFICACAO", repositorio);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) repositorio.getId(), intentBroadCast, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, repositorio.getDataHoraLong(), pendingIntent);

    }

    @NonNull
    private RepositorioAdapter.OnItemListener onClickRecyclerView() {
        return new RepositorioAdapter.OnItemListener() {
            @Override
            public void onClick(View view, int indice) {
                Repositorio repositorio = lista.get(indice);
                deletarTodo(repositorio);
            }
        };
    }

    private void deletarTodo(Repositorio repositorio) {
        repositorioDao.delete(repositorio);
        lista.remove(repositorio);
        recyclerView.getAdapter().notifyDataSetChanged();

        Intent intentBroadCastRemover = new Intent(this, NotificacaoReceiver.class);
        PendingIntent pendingIntentRemover = PendingIntent.getBroadcast(this, (int) repositorio.getId(), intentBroadCastRemover, 0);
        AlarmManager alarmManagerDeletar = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManagerDeletar.cancel(pendingIntentRemover);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                lista = repositorioDao.findAll();
                setAdapterRecyclerView(lista);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lista = repositorioDao.findByDescricao(query);
                setAdapterRecyclerView(lista);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
