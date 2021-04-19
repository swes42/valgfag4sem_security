import React, { useState,useEffect } from "react"
import facade from "./apiFacade";
import "./App.css"
import Home from "./components/Home";
import NoMatch from "./components/NoMatch";
import Header from "./components/Header";
import Register from "./components/Register";
import Edit from "./components/Edit";
import Admin from "./components/Admin";
import { LogIn, LoggedIn } from "./components/Login.js";


import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
  

function App() {
  const [loggedIn, setLoggedIn] = useState(false)
  const [roles, setRoles] = useState([]);

  const logout = () => {    
    facade.logout()
    setLoggedIn(false) } 

  const login = (user, pass) => { 
    facade.login(user,pass,setRoles)
    .then(res =>setLoggedIn(true))
    .catch(err => {
      if (err.status) {
        err.fullError.then(e => {
          console.log(e.message)
          alert(e.message)
          })
      } 
      else { alert("Network error"); }
    })
   } 
 
    return(
      <div>
  <Header />
  <Switch>
    <Route exact path="/">
      <Home />
    </Route>
    <Route path="/login">
    {!loggedIn ? (<LogIn login={login} />) :
   (<div>
   <LoggedIn roles={roles}/>
   <button onClick={logout}>Logout</button>
    </div>)}
    </Route>
    <Route path ="/register">
      <Register />
    </Route>
    <Route path ="/edit">
      <Edit />
    </Route>
    <Route>
      <Admin />
    </Route>
    <Route>
      <NoMatch/>
    </Route>
  </Switch>
  </div>
    );
}

export default App;
