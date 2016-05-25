package br.com.maceda.todo.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import br.com.maceda.todo.R;
import br.com.maceda.todo.service.NotificacaoIntentService;
import br.com.maceda.todo.vo.Repositorio;

/**
 * Created by josias on 02/05/2016.
 */
public class NotificacaoReceiver extends BroadcastReceiver {

    public NotificacaoReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Repositorio repositorio = (Repositorio) intent.getSerializableExtra("NOTIFICACAO");

        Intent intentService = new Intent(context, NotificacaoIntentService.class);
        intentService.putExtra("NOTIFICACAO", repositorio);

        PendingIntent pendingIntentService = PendingIntent.getService(context, (int) repositorio.getId(), intentService, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setTicker(repositorio.getDataHora())
                .setContentText(repositorio.getDescricao())
                .setContentTitle("ToDo")
                .setSubText(repositorio.getDataHora())
                .setContentIntent(pendingIntentService)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setAutoCancel(true);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify((int) repositorio.getId(), notification.build());
    }
}
