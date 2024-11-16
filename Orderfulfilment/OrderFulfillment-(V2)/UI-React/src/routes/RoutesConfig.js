import React from 'react';
import { Routes, Route } from 'react-router-dom';
import DefaultLayout from '../components/layout/DefaultLayout';
import LoginPage from '../components/pages/LoginPage';
import Home from '../components/pages/Home';
import Shop from '../components/pages/Shop';
import Inbox from '../components/pages/Inbox';
import Contact from '../components/pages/Contact';
import PageNotFound from '../components/pages/PageNotFound';
import Adduser from '../components/users/Adduser';
import Edituser from '../components/users/Edituser';
import User from '../components/users/User';
import Cart from '../components/pages/Cart';
import Checkout from '../components/pages/Checkout';
import OrderHistory from '../components/pages/OrderHistory';
import Invoice from '../components/pages/Invoice';
import CartPage from '../components/pages/CartPage';

const RoutesConfig = () => (
  <Routes>
    <Route path="/" element={<LoginPage />} />
    <Route element={<DefaultLayout />}>
      <Route path="/home" element={<Home />} />
      <Route path="/shop" element={<Shop />} />
      <Route path="/inbox" element={<Inbox />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/user/add" element={<Adduser />} />
      <Route path="/user/edit/:product_ID" element={<Edituser />} />
      <Route path="/user/:product_ID" element={<User />} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/payment" element={<Checkout />} />
      <Route path="/orderhistory" element={<OrderHistory />} />
      <Route path="/invoice" element={<Invoice />} />
      <Route path="/cartpage" element={<CartPage />} />
    </Route>
    <Route path="*" element={<PageNotFound />} />
  </Routes>
);

export default RoutesConfig;
