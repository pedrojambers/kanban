package com.example.kanban.model;

import java.io.Serializable;

public class Tarefa implements Serializable {
    private Long id;
    private long idQuadro;
    private String mTituloTarefa;
    private String status;

    public Long getIdQuadro() {
        return idQuadro;
    }

    public void setIdQuadro(Long idQuadro) {
        this.idQuadro = idQuadro;
    }

    public Tarefa() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmTituloTarefa() {
        return mTituloTarefa;
    }

    public void setmTituloTarefa(String mTituloTarefa) {
        this.mTituloTarefa = mTituloTarefa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
