import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [atendimento, setAtendimento] = useState({
    data: '', horario: '', problema_texto: '', receita_saude: '', profissional: null
  });
  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {
    profissionalService.listar().then(res => setProfissionais(res.data));
    if (id) {
      atendimentoService.buscar(id).then(res => setAtendimento(res.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await atendimentoService.atualizar(id, atendimento);
      } else {
        await atendimentoService.criar(atendimento);
      }
      navigate('/atendimentos');
    } catch (error) {
      console.error('Erro ao salvar atendimento:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Atendimento' : 'Novo Atendimento'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Data *</label>
          <input type="date" value={atendimento.data} required
            onChange={e => setAtendimento({...atendimento, data: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Horário *</label>
          <input type="time" value={atendimento.horario} required
            onChange={e => setAtendimento({...atendimento, horario: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Problema Texto</label>
          <textarea value={atendimento.problema_texto}
            onChange={e => setAtendimento({...atendimento, problema_texto: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Receita Saúde</label>
          <textarea value={atendimento.receita_saude} // Mudar esse aqui
            onChange={e => setAtendimento({...atendimento, receita_saude: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Profissional vinculado</label>
          <select value={atendimento.profissional?.id || ''}
            onChange={e => setAtendimento({...atendimento,
              profissional: e.target.value ? {id: parseInt(e.target.value)} : null})}>
            <option value="">Selecione um profissional</option>
            {profissionais.map(c => (
              <option key={c.id} value={c.id}>{c.nome}</option>
            ))}
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
      </form>
    </div>
  );
}

export default AtendimentoForm;
