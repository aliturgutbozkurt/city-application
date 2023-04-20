import {Axios, API_URL} from "../common/Axios.jsx";

const register = (username, fullName, password) => {
    return Axios.post(  "/auth/register", {
        username,
        fullName,
        password,
    });
};

const login = (username, password) => {
    return Axios
        .post( "/auth/login", {
            username,
            password,
        })
        .then((response) => {
            if (response.data.accessToken) {
                localStorage.setItem("accessToken", response.data.accessToken);
            }

            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem("accessToken");
};

const getCurrentToken = () => {
    return localStorage.getItem("accessToken");
};

const AuthService = {
    register,
    login,
    logout,
    getCurrentToken,
}

export default AuthService;
