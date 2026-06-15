import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalDeSaudeList() {
  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showNameFilter, setShowNameFilter] = useState(false);
  const [nameFilter, setNameFilter] = useState('');
  const [showCategoryFilter, setShowCategoryFilter] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState([]);

  useEffect(() => {
    carregarProfissionais();
  }, []);

  const carregarProfissionais = async () => {
    try {
      const response = await profissionalService.listar();
      setProfissionais(Array.isArray(response.data) ? response.data : []);
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

  const categories = ['Psicólogo', 'Fisioterapeuta', 'Médico'];

  const aplicarFiltros = (list) => {
    return list.filter(p => {
      const nomeOk = nameFilter.trim() === '' || (p.nome || '').toLowerCase().includes(nameFilter.trim().toLowerCase());
      const categoria = p.categoria || [];
      const categoriasArray = Array.isArray(categoria) ? categoria : [categoria];
      const categoriaOk = categoryFilter.length === 0 || categoriasArray.some(c => categoryFilter.includes(c));
      return nomeOk && categoriaOk;
    });
  };

  const displayed = aplicarFiltros(Array.isArray(profissionais) ? profissionais : []);

  return (
    <div>
      <div className="header">
        <h2>Profissionais</h2>
        <Link to="/profissionaisDeSaude/novo" className="btn btn-primary">+ Novo Profissional</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>
              <div style={{display: 'flex', alignItems: 'center', gap: 8}}>
                <span>Nome</span>
                <button type="button" className="btn btn-sm" onClick={() => setShowNameFilter(s => !s)}>🔎</button>
              </div>
              {showNameFilter && (
                <div style={{marginTop: 6}}>
                  <input
                    type="text"
                    placeholder="Pesquisar nome..."
                    value={nameFilter}
                    onChange={e => setNameFilter(e.target.value)}
                  />
                  <button type="button" className="btn btn-sm" onClick={() => setNameFilter('')}>Limpar</button>
                </div>
              )}
            </th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>
              <div style={{display: 'flex', alignItems: 'center', gap: 8}}>
                <span>Categoria</span>
                <button type="button" className="btn btn-sm" onClick={() => setShowCategoryFilter(s => !s)}>🔎</button>
              </div>
              {showCategoryFilter && (
                <div style={{marginTop: 6}}>
                  {categories.map(option => (
                    <label key={option} style={{display: 'block'}}>
                      <input
                        type="checkbox"
                        value={option}
                        checked={categoryFilter.includes(option)}
                        onChange={e => {
                          const checked = e.target.checked;
                          setCategoryFilter(prev => checked ? [...prev, option] : prev.filter(x => x !== option));
                        }}
                      /> {option}
                    </label>
                  ))}
                  <div style={{marginTop:6}}>
                    <button type="button" className="btn btn-sm" onClick={() => setCategoryFilter([])}>Limpar</button>
                  </div>
                </div>
              )}
            </th>
          </tr>
        </thead>
        <tbody>
          {displayed.map(comp => (
            <tr key={comp.id}>
              <td>{comp.nome}</td>
              <td>{comp.telefone}</td>
              <td>{comp.endereco}</td>
              <td>{Array.isArray(comp.categoria) ? comp.categoria.join(', ') : comp.categoria}</td>
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
