import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [atendimento, setAtendimento] = useState({
    data: '', horario: '', problema_texto: '', receita_saude: [], profissional: null
  });
  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {
    profissionalService.listar().then(res => setProfissionais(Array.isArray(res.data) ? res.data : []));
    if (id) {
      atendimentoService.buscar(id).then(res => {
        const a = res.data || {};
        const normalized = {
          ...a,
          receita_saude: Array.isArray(a.receita_saude) ? a.receita_saude : (a.receita_saude ? [a.receita_saude] : []),
          profissional: a.profissional && a.profissional.id ? a.profissional.id : (typeof a.profissional === 'number' ? a.profissional : null)
        };
        setAtendimento(normalized);
      });
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...atendimento,
        profissional: atendimento.profissional ? { id: atendimento.profissional } : null
      };
      if (id) {
        await atendimentoService.atualizar(id, payload);
      } else {
        await atendimentoService.criar(payload);
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
            <div>
              {['Atividade Mental', 'Atividade Física', 'Remédio'].map(option => (
                <label key={option} style={{display: 'block'}}>
                  <input
                    type="checkbox"
                    value={option}
                    checked={atendimento.receita_saude.includes(option)}
                    onChange={e => {
                      const checked = e.target.checked;
                      const prev = atendimento.receita_saude || [];
                      const next = checked ? [...prev, option] : prev.filter(x => x !== option);
                      setAtendimento({...atendimento, receita_saude: next});
                    }}
                  /> {option}
                </label>
              ))}
            </div>
        </div>
        <div className="form-group">
          <label>Profissional vinculado</label>
          <select value={atendimento.profissional || ''}
            onChange={e => setAtendimento({...atendimento,
              profissional: e.target.value ? parseInt(e.target.value) : null})}>
            <option value="">Selecione um profissional</option>
            {profissionais.length > 0 ? (
              profissionais.map(c => (
                <option key={c.id} value={c.id}>{c.nome}</option>
              ))
            ) : (
              <option value="" disabled>Nenhum profissional cadastrado</option>
            )}
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
      </form>
    </div>
  );
}

export default AtendimentoForm;
