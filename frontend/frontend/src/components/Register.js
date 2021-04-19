import facade from "../apiFacade";
import React, { useState, useEffect } from "react";

export default function Register() {
  const [user, setUser] = useState({ username: "", password: "" });
  const [repeatPassword, setRepeatPassword] = useState(""); //Separate usestate for checking the passwords match
  const [message, setMessage] = useState("");

  function addUser() {
    if (user.password === repeatPassword) {
      facade
        .addUser(user)
        .then((data) => {
          console.log("User created.");
          setMessage("User created!");
        })
        .catch((err) => {
          if (err.status) {
            err.fullError.then((e) => {
              console.log(e.message);
              setMessage(e.message);
            });
          } else {
            console.log("Error occurred!");
            setMessage("Error occurred!");
          }
        });
    } else {
        setMessage("The two passwords do not match! Please try again");
    }
  }

  const onChange = (evt) => {
    setUser({ ...user, [evt.target.id]: evt.target.value });//Uses the spread operator, takes the properties already in user(Right now empty strings) (or changes them if the name of the property is the same) + the values of the event's target id (they need to match the value eg. username and password.)
    //In Danish just cause:
    //den tager alle properties fra user, og tilføjer en ny , eller erstatter en hvis navn af property er den samme
    //så evt.target.id er et navn af property, som i vores tilfælde username eller password og evn.target.value er value af den property



  };

  const onChangeRepeatPassword = (evt) => {
    setRepeatPassword(evt.target.value);
  };
  return (
    <div>
      <h2>Register a user!</h2>
      <label for="username">Username</label>
      <br></br>
      <input
        type="text"
        className="textInputField"
        id="username"
        onChange={onChange}
      />
      <br></br>

      <label for="password">Password</label>
      <br></br>
      <input
        type="password"
        className="textInputField"
        id="password"
        onChange={onChange}
      />
      <br></br>

      <label for="password2">Re-enter password</label>
      <br></br>
      <input
        type="password"
        className="textInputField"
        id="password2"
        onChange={onChangeRepeatPassword}
      />
      <button onClick={addUser}>Register!</button>
      <p>{message}</p>
    </div>
  );
}
