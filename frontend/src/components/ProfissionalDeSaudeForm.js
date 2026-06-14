import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalDeSaudeForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [profissional, setProfissional] = useState({
    nome: '', telefone: '', endereco: '', categoria: []
  });
  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id).then(res => setProfissional(res.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await profissionalService.atualizar(id, profissional);
      } else {
        await profissionalService.criar(profissional);
      }
      navigate('/profissionaisDeSaude');
    } catch (error) {
      console.error('Erro ao salvar profissional:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar Profissional' : 'Novo Profissional'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Nome *</label>
          <input type="text" value={profissional.nome} required
            onChange={e => setProfissional({...profissional, nome: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Telefone *</label>
          <input type="tel" value={profissional.telefone} required
            onChange={e => setProfissional({...profissional, telefone: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Endereço</label>
          <input type="text" value={profissional.endereco}
            onChange={e => setProfissional({...profissional, endereco: e.target.value})} />
        </div>
        <div className="form-group">
            <label>Categorias</label>
            <div>
              {['Psicólogo', 'Fisioterapeuta', 'Médico'].map(option => (
                <label key={option} style={{display: 'block'}}>
                  <input
                    type="checkbox"
                    value={option}
                    checked={profissional.categoria.includes(option)}
                    onChange={e => {
                      const checked = e.target.checked;
                      const prev = profissional.categoria || [];
                      const next = checked ? [...prev, option] : prev.filter(x => x !== option);
                      setProfissional({...profissional, categoria: next});
                    }}
                  /> {option}
                </label>
              ))}
            </div>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/profissionaisDeSaude')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ProfissionalDeSaudeForm;
