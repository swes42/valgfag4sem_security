import 'bootstrap/dist/css/bootstrap.min.css';
// import { localURL as URL } from "./settings.js";
import { onlineURL as URL } from "./settings.js";
import { userURL } from "./settings.js";
import { fetchURL } from "./settings.js";

function handleHttpErrors(res) {
    if (!res.ok) {
        return Promise.reject({ status: res.status, fullError: res.json() })
    }
    return res.json();
}
/* ------------------ Token ------------------ */

function apiFacade() {
    const setToken = (token) => {
        localStorage.setItem('jwtToken', token) //This sets token in the local storage
    }
    const getToken = () => {
            return localStorage.getItem('jwtToken')
        }
        /* ------------------ Login/Logout ------------------ */

    const loggedIn = () => {
        const loggedIn = getToken() != null;
        return loggedIn;
    }
    const logout = () => {
        localStorage.removeItem("jwtToken"); //This removes token from the local storage
    }

    const login = (user, password, setRoles) => {
        const options = makeOptions("POST", true, {
            username: user,
            password: password,
        });

        return fetch(URL + "/api/login", options)
            .then(handleHttpErrors)
            .then((res) => {
                setToken(res.token); //This sets token in the local storage
                setRoles(res.roles);

            });
    };
    /* ------------------ Roles ------------------ */

    //The Roles parameter is passed in as parameter from the LoggedIn component in the App.js
    const fetchRoles = roles => {
        let userType = "none"; //Default role is none
        if (roles.includes("user") && roles.includes("admin")) userType = "admin"; //If it's both user and Admin we use the admin login endpoint
        else if (roles.includes("user")) userType = "user";
        else if (roles.includes("admin")) userType = "admin";
        const options = makeOptions("GET", true); //True adds the token 
        return fetch(URL + "/api/info/" + userType, options).then(handleHttpErrors); //Uses the right endpoint depending on what role the user has
    };

    function getAllUsers() {
        const options = makeOptions("GET", true); //True adds the token 

        return fetch(userURL + "allusers", options) //Returns promise
            .then(handleHttpErrors);
    }
    /* ------------------ CRUD ------------------ */
    function addUser(user) {
        let options = makeOptions("POST", false, user, )
        return fetch(userURL, options) //Returns promise
            .then(handleHttpErrors);
    }

    function editUser(username) {
        let options = makeOptions("PUT", true, username);
        // console.log(options);
        return fetch(userURL + username, options)
            .then(handleHttpErrors);
    }

    function deleteUser(username) {
        let options = makeOptions("DELETE", true, username);
        return fetch(userURL + /admin/ + username, options)
            .then(handleHttpErrors);
    }
    /* ------------------ Options ------------------ */

    const makeOptions = (method, addToken, body) => {
            var opts = {
                method: method,
                headers: {
                    "Content-type": "application/json",
                    'Accept': 'application/json',
                }
            }
            if (addToken && loggedIn()) {
                opts.headers["x-access-token"] = getToken();
            }
            if (body) {
                opts.body = JSON.stringify(body);
            }
            return opts;
        }


    return {
        makeOptions,
        setToken,
        getToken,
        loggedIn,
        login,
        logout,
        fetchRoles,
        getAllUsers,
        addUser,
        editUser,
        deleteUser,
    }
}
const facade = apiFacade();
export default facade;