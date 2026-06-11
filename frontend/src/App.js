import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import AtendimentoList from './components/AtendimentoList';
import AtendimentoForm from './components/AtendimentoForm';
import ExameLabList from './components/ExameLabList';
import ExameLabForm from './components/ExameLabForm';
import ProfissionalList from './components/ProfissionalDeSaudeList';
import ProfissionalForm from './components/ProfissionalDeSaudeForm';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1> | Clínica Web | </h1>
          <div className="nav-links">
            <Link to="/atendimentos">Atendimentos</Link>
            <Link to="/examesLab">Exames</Link>
            <Link to="/profissionaisDeSaude">Profissionais</Link>
          </div>
        </nav>

        <main className="container">
          <Routes>
            <Route path="/" element={<AtendimentoList />} />
            <Route path="/atendimentos" element={<AtendimentoList />} />
            <Route path="/atendimentos/novo" element={<AtendimentoForm />} />
            <Route path="/atendimentos/editar/:id" element={<AtendimentoForm />} />
            <Route path="/examesLab" element={<ExameLabList />} />
            <Route path="/examesLab/novo" element={<ExameLabForm />} />
            <Route path="/examesLab/editar/:id" element={<ExameLabForm />} />
            <Route path="/profissionaisDeSaude" element={<ProfissionalList />} />
            <Route path="/profissionaisDeSaude/novo" element={<ProfissionalForm />} />
            <Route path="/profissionaisDeSaude/editar/:id" element={<ProfissionalForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
