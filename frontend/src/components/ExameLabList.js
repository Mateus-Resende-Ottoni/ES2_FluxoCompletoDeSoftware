import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { exameLabService } from '../services/api';

function ExameLabList() {
  const [exames, setExamesLab] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarExamesLab();
  }, []);

  const carregarExamesLab = async () => {
    try {
      const response = await exameLabService.listar();
      setExamesLab(response.data);
    } catch (error) {
      console.error('Erro ao carregar exames:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletarExameLab = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este exameLab?')) {
      try {
        await exameLabService.deletar(id);
        carregarExamesLab();
      } catch (error) {
        console.error('Erro ao deletar exameLab:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>📅 ExamesLab</h2>
        <Link to="/examesLab/novo" className="btn btn-primary">+ Novo ExameLab</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Descrição</th>
            <th>Atendimento</th>
          </tr>
        </thead>
        <tbody>
          {exames.map(comp => (
            <tr key={comp.id}>
              <td>{comp.descricao}</td>
              <td>{comp.atendimento?.data || '-'} _ {comp.atendimento?.horario || '-'}</td>
              <td>
                <Link to={`/examesLab/editar/${comp.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletarExameLab(comp.id)} className="btn btn-danger btn-sm">
                  Excluir
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {exames.length === 0 && <p className="empty">Nenhum exame cadastrado.</p>}
    </div>
  );
}

export default ExameLabList;
