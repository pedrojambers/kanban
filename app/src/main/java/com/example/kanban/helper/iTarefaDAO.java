package com.example.kanban.helper;

import com.example.kanban.model.Tarefa;

import java.util.List;

public interface iTarefaDAO {

    public boolean salvar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public List<Tarefa> listarTarefasPorSituacao(long idQuadro, String situacao);

}
