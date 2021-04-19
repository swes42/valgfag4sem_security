import {
    BrowserRouter as Router,
    Switch,
    Route,
    NavLink,
  } from "react-router-dom";

export default function Header(){
return(
    <ul className="header">
    <li><NavLink exact activeClassName="selected" to="/">Home</NavLink></li>
    <li><NavLink activeClassName="selected" to="/login">Login</NavLink></li>
    <li><NavLink activeClassName="selected" to="/register">Register</NavLink></li>
    <li><NavLink activeClassName="selected" to="/edit">Edit</NavLink></li>
    <li><NavLink activeClassName="selected" to="/admin">Admin</NavLink></li>
    </ul>
);
}
