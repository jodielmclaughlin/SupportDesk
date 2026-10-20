import './App.css'
import {HashRouter, Route, Routes} from "react-router-dom";
import TicketListPage from "./pages/TicketListPage.tsx";
import UserListPage from "./pages/UserListPage.tsx";

function App() {


  return (
      <HashRouter>
    <div>
      <Routes>
        <Route path="/" element={<TicketListPage />} />
        <Route path="/users" element={<UserListPage />} />
      </Routes>
    </div>
      </HashRouter>
  )
}

export default App
