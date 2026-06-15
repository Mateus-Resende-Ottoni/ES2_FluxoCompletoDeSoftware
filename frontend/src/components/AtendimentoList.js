import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { atendimentoService } from '../services/api';

function AtendimentoList() {
  const [atendimentos, setAtendimentos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showDateFilter, setShowDateFilter] = useState(false);
  const [dateFilter, setDateFilter] = useState('');

  useEffect(() => {
    carregarAtendimentos();
  }, []);

  const carregarAtendimentos = async () => {
    try {
      const response = await atendimentoService.listar();
      setAtendimentos(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      console.error('Erro ao carregar atendimentos:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletarAtendimento = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este atendimento?')) {
      try {
        await atendimentoService.deletar(id);
        carregarAtendimentos();
      } catch (error) {
        console.error('Erro ao deletar atendimento:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  const aplicarFiltros = (list) => {
    return list.filter(a => {
      if (!dateFilter) return true;
      const itemDate = (a.data || '').split('T')[0];
      return itemDate === dateFilter;
    });
  };

  const displayed = aplicarFiltros(Array.isArray(atendimentos) ? atendimentos : []);

  return (
    <div>
      <div className="header">
        <h2>Atendimentos</h2>
        <Link to="/atendimentos/novo" className="btn btn-primary">+ Novo Atendimento</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>
              <div style={{display: 'flex', alignItems: 'center', gap: 8}}>
                <span>Data</span>
                <button type="button" className="btn btn-sm" onClick={() => setShowDateFilter(s => !s)}>🔎</button>
              </div>
              {showDateFilter && (
                <div style={{marginTop:6}}>
                  <input type="date" value={dateFilter} onChange={e => setDateFilter(e.target.value)} />
                  <button type="button" className="btn btn-sm" onClick={() => setDateFilter('')}>Limpar</button>
                </div>
              )}
            </th>
            <th>Horário</th>
            <th>Problema</th>
            <th>Receita</th>
            <th>Profissional</th>
          </tr>
        </thead>
        <tbody>
          {displayed.map(comp => (
            <tr key={comp.id}>
              <td>{comp.data}</td>
              <td>{comp.horario}</td>
              <td>{comp.problema_texto}</td>
              <td>{Array.isArray(comp.receita_saude) ? comp.receita_saude.join(', ') : comp.receita_saude}</td>
              <td>{comp.profissional?.nome || '-'}</td>
              <td>
                <Link to={`/atendimentos/editar/${comp.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletarAtendimento(comp.id)} className="btn btn-danger btn-sm">
                  Excluir
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {atendimentos.length === 0 && <p className="empty">Nenhum atendimento cadastrado.</p>}
    </div>
  );
}

export default AtendimentoList;
