import React, {useState, useRef, useEffect} from "react";
import CityList from "./CityList.jsx";
import CitySearch from "./CitySearch.jsx";
import {Axios} from "../../common/Axios.jsx";

function Cities() {
    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [loading, setLoading] = useState(false);
    const [searchName, setSearchName] = useState(false);
    const [tableData, setTableData] = useState({})


    const fetchData = async (page) => {

        setLoading(true);
        try {
            let response = {};
            if (searchName) {
                response = await Axios.get("/cities/search?name=" + searchName, {params: {pageNo: page}});
            } else {
                response = await Axios.get("/cities", {params: {pageNo: page}});
            }

            setTableData({
                data: response.data.content,
                currentPage: page + 1,
                totalElements: response.data.totalElements
            })
        } catch (error) {
            console.error(error.message);
        }
        setLoading(false);
    }

    useEffect(() => {
        fetchData(0);
    }, []);

    useEffect(() => {
        fetchData(0);
    }, [searchName]);

    const onSearchBoxChange = async (name) => {
        console.log(name);
        setSearchName(name);
    }


    return (
        <div>
            <div><CitySearch onChange={ onSearchBoxChange}/></div>
            <div><CityList fetchData={fetchData} searchName={searchName} tableData={tableData}/></div>
        </div>
    )
}

export default Cities;