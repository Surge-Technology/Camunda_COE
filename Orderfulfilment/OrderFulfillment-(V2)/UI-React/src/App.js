
import {BrowserRouter as Router ,Route, Routes} from "react-router-dom";

import "../node_modules/bootstrap/dist/css/bootstrap.css";
 
import Navbar from "./components/layout/Navbar";
import Inbox from "./components/pages/Inbox";
import Contact from "./components/pages/Contact";
import Home from "./components/pages/Home";
import Shop from "./components/pages/Shop";
import PageNotFound from "./components/pages/PageNotFound";
import Adduser from "./components/users/Adduser";
import Edituser from "./components/users/Edituser";
import User from "./components/users/User";
import LoginPage from "./components/pages/LoginPage";
import Cart from "./components/pages/Cart";
import Checkout from "./components/pages/Checkout";
import OrderHistory from "./components/pages/OrderHistory";
import Invoice from "./components/pages/Invoice";
import CartPage from "./components/pages/CartPage";
import RoutesConfig from "./routes/RoutesConfig";
 
 
 
 
 
function App() {
 
  return (
    <Router>
      <div className="App">
        <RoutesConfig />
      </div>
    </Router>
   
  );
}
 
export default App;