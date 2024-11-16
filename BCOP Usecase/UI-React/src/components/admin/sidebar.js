import React, { useState } from "react";
import "./sidebar.css";
import Dashboard from "./dashboard";
import Customers from "./customers";
import AccountRequests from "./accountrequests";
import Transactions from "./transactions";
import auth from "../../service/adminauth";
import RejectedAccounts from "./rejectedaccounts";
import { Button, Modal } from "react-bootstrap";

export default function Sidebar(props) {
  console.log(props.location.state);
  const [temp, setTemp] = useState({
    component: ""
  });

  const [modalShow, setModalShow] = useState(false);

  function renderComponent(component) {
    setTemp({ ...temp, component: component });
  }

  function renderSwitch(component) {
    switch (component) {
      case "dashboard":
        return <Dashboard customer={props.location.state.customer} />;
      case "customers":
        return <Customers />;
      case "accountrequests":
        return <AccountRequests />;
      case "rejectedaccounts":
        return <RejectedAccounts />;
      case "transactions":
        return <Transactions />;
      default:
        return <Dashboard customer={props.location.state.customer} />;
    }
  }

  function logout() {
    auth.logout(() => {
      auth.setHeader("", "");
      props.history.push("/admin/login");
    });
  }

  function MyVerticallyCenteredModal(props) {
    return (
      <Modal
        {...props}
        //size="lg"
        aria-labelledby="contained-modal-title-vcenter"
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            See you soon !!
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Confirm Logout ?</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={props.onHide}>
            Cancel
          </Button>
          <Button variant="success" onClick={logout}>
            Logout
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  return (
    <div className="d-flex" id="wrapper">
      <div className="bg-light border-right" id="sidebar-wrapper">
        <div className="sidebar-heading">
          Admin Panel<i class="fa fa-lock float-right iconsize"></i>
        </div>
        <div className="list-group list-group-flush">
          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => renderComponent("dashboard")}
          >
            Dashboard<i className="fa fa-user float-right iconsize"></i>
          </button>
          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => renderComponent("customers")}
          >
            Customers<i class="fa fa-users iconsize float-right"></i>
          </button>
          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => renderComponent("accountrequests")}
          >
            Account Requests<i class="fa fa-bell iconsize float-right"></i>
          </button>

          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => renderComponent("rejectedaccounts")}
          >
            Rejected Accounts<i class="fa fa-times iconsize float-right"></i>
          </button>

          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => renderComponent("transactions")}
          >
            Transaction Reports
            <i class="fas fa-american-sign-language-interpreting iconsize float-right"></i>
          </button>
          <button
            className="list-group-item list-group-item-action bg-light"
            onClick={() => setModalShow(true)}
          >
            Logout<i class="fas fa-sign-out-alt iconsize float-right"></i>
          </button>
          <MyVerticallyCenteredModal
            show={modalShow}
            onHide={() => setModalShow(false)}
          />
        </div>
      </div>

      <div id="page-content-wrapper">
        <div className="container-fluid">{renderSwitch(temp.component)}</div>
      </div>
    </div>
  );
}
