package com.example.kanban.helper;

import com.example.kanban.model.Quadro;

import java.util.List;

public interface iQuadroDAO {

    public boolean salvar(Quadro quadro);
    public boolean atualizar(Quadro quadro);
    public boolean deletar(Quadro quadro);
    public List<Quadro> listar();

}
