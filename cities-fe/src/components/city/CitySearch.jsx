import React, {useState, useRef} from "react";
import {Button, Form, Input} from "antd";

function CitySearch(props) {
    const onInputChange = (e) => {
        props.onChange(e.target.value);
    };
    
    return (
        <div>
            <Input placeholder="Search By Name" onChange={(e) => onInputChange(e)}/>
        </div>

    );
}

export default CitySearch;