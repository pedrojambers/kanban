package com.example.kanban.model;

import java.io.Serializable;

public class Quadro implements Serializable {
    private Long Id;
    private String mTituloQuadro;

    public Quadro() {
    }

    public Long getId() {

        return Id;
    }

    public void setId(Long id) {

        Id = id;
    }

    public String getmTituloQuadro() {

        return mTituloQuadro;
    }

    public void setmTituloQuadro(String mTituloQuadro) {

        this.mTituloQuadro = mTituloQuadro;
    }
}
