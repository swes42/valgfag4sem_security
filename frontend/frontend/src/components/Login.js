import React, { useState,useEffect } from "react"
import facade from "../apiFacade";

export function LogIn({ login }) {
    const init = { username: "", password: "" };
    const [loginCredentials, setLoginCredentials] = useState(init);
   
    const performLogin = (evt) => {
      evt.preventDefault();
      login(loginCredentials.username, loginCredentials.password);
    }
    const onChange = (evt) => {
      setLoginCredentials({ ...loginCredentials,[evt.target.id]: evt.target.value })
    }
    return (    
      <div>
        <h2>Login</h2>
        <form onChange={onChange} >
          <input placeholder="User Name" id="username" />
          <input placeholder="Password" id="password" type="password" />
          <button onClick={performLogin}>Login</button>
        </form>
      </div>
      
    )
   
  }
  
  export function LoggedIn({ roles }) {
    const [dataFromServer, setDataFromServer] = useState("")
  
      useEffect(() => { facade.fetchRoles(roles).then(data=> setDataFromServer(data.msg));
      }, [])
    
  
      return (
        <div>
          <h2>Data Received from server</h2>
          <h3>{dataFromServer}</h3>
        </div>
      )
  
    }

  