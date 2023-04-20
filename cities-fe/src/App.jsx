import {useEffect, useState} from 'react'

import './App.css'
import {Link, Route, Routes} from "react-router-dom";
import AuthService from "./services/auth.service.js";
import EventBus from "./common/EventBus";
import Home from "./components/Home.jsx";
import Login from "./components/auth/Login.jsx";
import Register from "./components/auth/Register.jsx";
import Cities from "./components/city/Cities.jsx";

function App() {
    const [currentToken, setCurrentToken] = useState(undefined);

    useEffect(() => {
        const token = AuthService.getCurrentToken();

        if (token) {
            setCurrentToken(token);
        }
        EventBus.on("logout", () => {
            logOut();
        });

        return () => {
            EventBus.remove("logout");
        };
    }, []);

    const logOut = () => {
        AuthService.logout();
        setCurrentToken(undefined);
    };


    return (
        <div>
            <nav className="navbar navbar-expand navbar-dark bg-dark">
                <div className="navbar-nav mr-auto">
                    <li className="nav-item">
                        <Link to={"/home"} className="nav-link">
                            Home
                        </Link>
                    </li>
                </div>

                {currentToken ? (
                    <div>
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <a href="/login" className="nav-link" onClick={logOut}>
                                    LogOut
                                </a>
                            </li>
                        </div>
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <a href="/cities" className="nav-link">
                                    Cities
                                </a>
                            </li>
                        </div>
                    </div>
                ) : (
                    <div className="navbar-nav ml-auto">
                        <li className="nav-item">
                            <Link to={"/login"} className="nav-link">
                                Login
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link to={"/register"} className="nav-link">
                                Sign Up
                            </Link>
                        </li>
                    </div>
                )}
            </nav>

            <div className="container mt-3">
                <Routes>
                    <Route exact path={"/"} element={<Home/>}/>
                    <Route exact path={"/home"} element={<Home/>}/>
                    <Route exact path="/login" element={<Login/>}/>
                    <Route exact path="/register" element={<Register/>}/>
                    <Route exact path="/cities" element={<Cities/>}/>

                </Routes>
            </div>

            {/* <AuthVerify logOut={logOut}/> */}
        </div>
    );
}

export default App
