import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameLabService, atendimentoService } from '../services/api';

function ExameLabForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [exameLab, setExameLab] = useState({
    descricao: '', atendimento: null
  });
  const [atendimentos, setAtendimentos] = useState([]);

  useEffect(() => {
    atendimentoService.listar().then(res => setAtendimentos(res.data));
    if (id) {
      exameLabService.buscar(id).then(res => setExameLab(res.data));
    }
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await exameLabService.atualizar(id, exameLab);
      } else {
        await exameLabService.criar(exameLab);
      }
      navigate('/examesLab');
    } catch (error) {
      console.error('Erro ao salvar exame:', error);
    }
  };

  return (
    <div>
      <h2>{id ? 'Editar ExameLab' : 'Novo ExameLab'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Descrição</label>
          <textarea value={exameLab.descricao}
            onChange={e => setExameLab({...exameLab, descricao: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Atendimento vinculado</label>
          <select value={exameLab.atendimento?.id || ''}
            onChange={e => setExameLab({...exameLab,
              atendimento: e.target.value ? {id: parseInt(e.target.value)} : null})}>
            <option value="">Selecione um atendimento</option>
            {atendimentos.map(c => (
              <option key={c.id} value={c.id}>{c.data}-{c.horario}</option>
            ))}
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/examesLab')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ExameLabForm;
