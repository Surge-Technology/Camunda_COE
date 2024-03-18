import "../node_modules/bootstrap/dist/css/bootstrap.css";

import Navbar from "./components/layout/Navbar";
import Inbox from "./components/pages/Inbox";
import Contact from "./components/pages/Contact";
import Home from "./components/pages/Home";
import Shop from "./components/pages/Shop";
import {BrowserRouter as Router ,Route, Routes} from "react-router-dom";
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
 




function App() {

  return (
    <Router>
    <div className="App">
     <Navbar/>
     <Routes>
     <Route exact path="/" Component={LoginPage}></Route>
          <Route exact path="/home" Component={Home}></Route>
         
          <Route exact path="/shop" Component={Shop}></Route>
          <Route exact path="/inbox" Component={Inbox}></Route>
          <Route exact path="/contact" Component={Contact}></Route>
          <Route exact path="*" Component={PageNotFound}></Route>
          <Route exact path="/user/add" Component={Adduser}></Route>
          <Route exact path="/user/edit/:product_ID" Component={Edituser}></Route>
          <Route exact path="/user/:product_ID" Component={User}></Route>
          <Route  path="/Cart/" element={<Cart/>}/>
          <Route path="/checkout" element={<Checkout/>}></Route>
          <Route path="/orderhistory" element={<OrderHistory/>}></Route>
          <Route path="/invoice" element={<Invoice/>}></Route>
          <Route path="/CartPage" element={<CartPage/>}></Route>

          {/* <Route exact path="/user" Component={User}></Route> */}
         
          

     </Routes>
     
    </div>
    </Router>
    
  );
}

export default App;
