import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalDeSaudeList() {
  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarProfissionais();
  }, []);

  const carregarProfissionais = async () => {
    try {
      const response = await profissionalService.listar();
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletarProfissional = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este profissional?')) {
      try {
        await profissionalService.deletar(id);
        carregarProfissionais();
      } catch (error) {
        console.error('Erro ao deletar profissional:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>📅 Profissionais</h2>
        <Link to="/profissionaisDeSaude/novo" className="btn btn-primary">+ Novo Profissional</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>Categoria</th>
          </tr>
        </thead>
        <tbody>
          {profissionais.map(comp => (
            <tr key={comp.id}>
              <td>{comp.nome}</td>
              <td>{comp.telefone}</td>
              <td>{comp.endereco}</td>
              <td>{comp.categoria}</td>
              <td>
                <Link to={`/profissionaisDeSaude/editar/${comp.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletarProfissional(comp.id)} className="btn btn-danger btn-sm">
                  Excluir
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {profissionais.length === 0 && <p className="empty">Nenhum profissional cadastrado.</p>}
    </div>
  );
}

export default ProfissionalDeSaudeList;
