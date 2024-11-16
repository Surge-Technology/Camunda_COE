import React, { useState } from 'react';

 

 

function customerInbox() {

  // eslint-disable-next-line react-hooks/rules-of-hooks
  const [selectAll, setSelectAll] = useState(false);

 

  const handleSelectAll = () => {

    setSelectAll(!selectAll);

    // Logic to toggle all checkboxes

  }

  return (

    <div className="container">

      <div className="row">

        {/* BEGIN INBOX */}

        <div className="col-md-12">

          <div className="grid email">

            <div className="grid-body">

              <div className="row">

                {/* BEGIN INBOX MENU */}

                <div className="col-md-3">

                  <h2 className="grid-title">

                    <i className="fa fa-inbox"></i> Inbox

                  </h2>

                  <button

                    className="btn btn-block btn-primary"

                    data-toggle="modal"

                    data-target="#compose-modal"

                  >

                    <i className="fa fa-pencil"></i>&nbsp;&nbsp;NEW MESSAGE

                  </button>

 

                  <hr />

 

                  <div>

                    <ul className="nav nav-pills nav-stacked">

                      <li className="header">Folders</li>

                      <li className="active">

                        <a href="#">

                          <i className="fa fa-inbox"></i> Inbox (14)

                        </a>

                      </li>

                      <li>

                        <a href="#">

                          <i className="fa fa-star"></i> Starred

                        </a>

                      </li>

                      {/* ... Other menu items */}

                    </ul>

                  </div>

                </div>

                {/* END INBOX MENU */}

 

                {/* BEGIN INBOX CONTENT */}

                <div className="col-md-9">

                  <div className="row">

                    <div className="col-sm-6">

                      <label style={{ marginRight: '8px' }} className="">

                        <div className="icheckbox_square-blue" style={{ position: 'relative' }}>

                          <input

                            type="checkbox"

                            id="check-all"

                            className="icheck"

                            style={{

                              position: 'absolute',

                              top: '-20%',

                              left: '-20%',

                              display: 'block',

                              width: '140%',

                              height: '140%',

                              margin: '0px',

                              padding: '0px',

                              border: '0px',

                              opacity: 0,

                              background: 'rgb(255, 255, 255)',

                            }}

                          />

                          <ins

                            className="iCheck-helper"

                            style={{

                              position: 'absolute',

                              top: '-20%',

                              left: '-20%',

                              display: 'block',

                              width: '140%',

                              height: '140%',

                              margin: '0px',

                              padding: '0px',

                              border: '0px',

                              opacity: 0,

                              background: 'rgb(255, 255, 255)',

                            }}

                          ></ins>

                        </div>

                      </label>

                      <div className="btn-group">

                        <button type="button" className="btn btn-default dropdown-toggle" data-toggle="dropdown">

                          Action <span className="caret"></span>

                        </button>

                        <ul className="dropdown-menu" role="menu">

                          <li>

                            <a href="#">Mark as read</a>

                          </li>

                          <li>

                            <a href="#">Mark as unread</a>

                          </li>

                          {/* ... Other dropdown items */}

                        </ul>

                      </div>

                    </div>

 

                    <div className="col-md-6 search-form">

                      <form action="#" className="text-right">

                        <div className="input-group">

                          <input type="text" className="form-control input-sm" placeholder="Search" />

                          <span className="input-group-btn">

                            <button type="submit" name="search" className="btn_ btn-primary btn-sm search">

                              <i className="fa fa-search"></i>

                            </button>

                          </span>

                        </div>

                      </form>

                    </div>

                  </div>

 

                  <div className="padding"></div>

 

                  <div className="table-responsive">

                    <table className="table">

                      <tbody>

                        <tr>

                          <td className="action">

                            <input type="checkbox" />

                          </td>

                          {/* ... Other table cells */}

                        </tr>

                        {/* ... Other table rows */}

                      </tbody>

                    </table>

                  </div>

 

                  <ul className="pagination">

                    <li className="disabled">

                      <a href="#">«</a>

                    </li>

                    <li className="active">

                      <a href="#">1</a>

                    </li>

                    {/* ... Other pagination items */}

                  </ul>

                </div>

                {/* END INBOX CONTENT */}

 

                {/* BEGIN COMPOSE MESSAGE */}

                <div className="modal fade" id="compose-modal" tabIndex="-1" role="dialog" aria-hidden="true">

                  <div className="modal-wrapper">

                    <div className="modal-dialog">

                      <div className="modal-content">

                        <div className="modal-header bg-blue">

                          <button type="button" className="close" data-dismiss="modal" aria-hidden="true">

                            ×

                          </button>

                          <h4 className="modal-title">

                            <i className="fa fa-envelope"></i> Compose New Message

                          </h4>

                        </div>

                        <form action="#" method="post">

                          <div className="modal-body">

                            <div className="form-group">

                              <input name="to" type="email" className="form-control" placeholder="To" />

                            </div>

                            {/* ... Other form inputs */}

                          </div>

                          <div className="modal-footer">

                            <button type="button" className="btn btn-default" data-dismiss="modal">

                              <i className="fa fa-times"></i> Discard

                            </button>

                            <button type="submit" className="btn btn-primary pull-right">

                              <i className="fa fa-envelope"></i> Send Message

                            </button>

                          </div>

                        </form>

                      </div>

                    </div>

                  </div>

                </div>

                {/* END COMPOSE MESSAGE */}

              </div>

            </div>

          </div>

        </div>

        {/* END INBOX */}

      </div>

    </div>

  );

}

 

export default customerInbox;