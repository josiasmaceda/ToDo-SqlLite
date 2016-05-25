package br.com.maceda.todo.service;

import android.app.IntentService;
import android.content.Intent;

import br.com.maceda.todo.dao.RepositorioDao;
import br.com.maceda.todo.vo.Repositorio;

/**
 * Created by josias on 03/04/2016.
 */
public class NotificacaoIntentService extends IntentService {

    private static final boolean ATUALIZAR = true;

    public NotificacaoIntentService() {
        super("NotificacaoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Repositorio repositorio = (Repositorio) intent.getSerializableExtra("NOTIFICACAO");
            RepositorioDao dao = RepositorioDao.getInstance(this);
            dao.delete(repositorio);

            Intent intentAtualizar = new Intent("ATUALIZAR");
            sendBroadcast(intentAtualizar);

            stopSelf();
        }
    }
}
