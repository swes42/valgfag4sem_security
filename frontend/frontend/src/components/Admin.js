import React, { useState,useEffect } from "react"
import facade from "../apiFacade";

export default function Admin() {
    const [render, setRender] = useState(false);
    const [dataFromServer, setDataFromServer] = useState(null);
    const [message, setMessage] = useState(null);
    const [tableRows, setTableRows] = useState("");

    // function editUser(evt){
    //     evt.preventDefault();
    //     facade.editUser(evt.target.id)
    //     userToEdit = {username: evt.target.id, password}
    // }
    function deleteUser(evt){
        evt.preventDefault();
        facade.deleteUser(evt.target.id)
        setRender(!render)
        setMessage("User deleted");
        getAllUsers()
        
    }
    function getAllUsers(){
        facade.getAllUsers()
        .then(data => {
            setTableRows(data.map((user) =>
            (<tr key={user.username}>
            <td>{user.username}</td>    
            <td><button id={user.username} onClick={deleteUser} >Delete</button></td>    
            </tr>)
        )                 
              )})
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
    }
    useEffect(() =>{ //Takes care of the stuff that ComponentDidMount used to
        setMessage("")
        getAllUsers()
        // setDataFromServer(null)
        
    },[render]) //What useEffect is listening for changes on


 const displayUsers = setTableRows? (
    <table className="table" style={{width: "90%"}}>
    <thead>
      <tr>
        <th>Username</th>
        <th></th>
      </tr>
    </thead>
    <tbody id="tbody">{tableRows}</tbody>
  </table>
 ) : "Loading..."

 return (    
    <div>
      <h2>Fetched Data</h2>
      {!message && displayUsers}
      <p>{message}</p>

    </div>
    
  );
 }