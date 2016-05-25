package br.com.maceda.todo.vo;

import java.io.Serializable;

/**
 * Created by josias on 30/04/2016.
 */
public class Repositorio implements Serializable {

    private long id;
    private String descricao;
    private String dataHora;
    private long dataHoraLong;

    public Repositorio(String descricao) {
        this.descricao = descricao;
    }

    public Repositorio() {}

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHoraLong(long dataHoraLong) {
        this.dataHoraLong = dataHoraLong;
    }

    public long getDataHoraLong() {
        return dataHoraLong;
    }
}
