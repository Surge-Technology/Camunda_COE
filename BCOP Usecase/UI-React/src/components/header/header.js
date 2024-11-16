import ewallet from "../../assets/images/ewallet.png";
import React from "react";
import "../header/header.css";

export default function Header(props) {
  return (
    <div className="jumbotron display-4 text-center header">
      <img
        width="400px"
        height="200px"
        src={ewallet}
        style={{
          left: "2%",
          padding: "0px",
          position: "absolute"
        }}
        alt="ewallet"
      ></img>{" "}
      <div style={{ position: "relative", bottom: "1rem" }}>
        <strong> {props.message} </strong>{" "}
      </div>
    </div>
  );
}
