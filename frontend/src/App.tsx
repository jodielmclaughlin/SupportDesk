import './App.css'
import {HashRouter, Route, Routes} from "react-router-dom";
import TicketListPage from "./pages/TicketListPage.tsx";

function App() {


  return (
      <HashRouter>
    <div>
      <Routes>
        <Route path="/" element={<TicketListPage />} />
      </Routes>
    </div>
      </HashRouter>
  )
}

export default App
