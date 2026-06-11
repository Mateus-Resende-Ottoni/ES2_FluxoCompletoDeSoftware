import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

// ========== Atendimentos ==========
export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  criar: (atendimento) => api.post('/atendimentos', atendimento),
  atualizar: (id, atendimento) => api.put(`/atendimentos/${id}`, atendimento),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

// ========== ExamesLab ==========
export const exameLabService = {
  listar: () => api.get('/examesLab'),
  buscar: (id) => api.get(`/examesLab/${id}`),
  criar: (exameLab) => api.post('/examesLab', exameLab),
  atualizar: (id, exameLab) => api.put(`/examesLab/${id}`, exameLab),
  deletar: (id) => api.delete(`/examesLab/${id}`)
};

// ========== ProfissionaisDeSaude ==========
export const profissionalService = {
  listar: () => api.get('/profissionaisDeSaude'),
  buscar: (id) => api.get(`/profissionaisDeSaude/${id}`),
  criar: (profissional) => api.post('/profissionaisDeSaude', profissional),
  atualizar: (id, profissional) => api.put(`/profissionaisDeSaude/${id}`, profissional),
  deletar: (id) => api.delete(`/profissionaisDeSaude/${id}`)
};

export default api;
