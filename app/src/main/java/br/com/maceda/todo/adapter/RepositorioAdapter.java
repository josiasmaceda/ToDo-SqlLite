package br.com.maceda.todo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.maceda.todo.R;
import br.com.maceda.todo.vo.Repositorio;

/**
 * Created by josias on 30/04/2016.
 */
public class RepositorioAdapter extends RecyclerView.Adapter<RepositorioAdapter.ItemViewHolder> {

    private final OnItemListener onItemListener;
    private List<Repositorio> lista;
    private Context context;

    public RepositorioAdapter(Context context, List<Repositorio> lista, OnItemListener onItem) {
        this.context = context;
        this.lista = lista;
        this.onItemListener = onItem;
    }

    @Override
    public RepositorioAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_lista, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RepositorioAdapter.ItemViewHolder holder, final int position) {
        Repositorio repositorio = lista.get(position);
        holder.txtDescricao.setText(repositorio.getDescricao());
        holder.txtDataHora.setText(repositorio.getDataHora());
        if (onItemListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemListener.onClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDescricao;
        private TextView txtDataHora;

        public ItemViewHolder(View itemView) {
            super(itemView);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtItem);
            txtDataHora = (TextView) itemView.findViewById(R.id.txtDataHora);
        }
    }

    public interface OnItemListener {
        void onClick(View view, int indice);
    }
}
