import React, {useState, useRef} from "react";
import {Button, Form, Input} from "antd";

function CitySearch(props) {
    const [form] = Form.useForm();
    const [formLayout, setFormLayout] = useState('horizontal');
    const onInputChange = (e) => {
        props.onChange(e.target.value);
    };

    const buttonItemLayout =
        formLayout === 'horizontal'
            ? {
                wrapperCol: {
                    span: 14,
                    offset: 4,
                },
            }
            : null;
    return (
        <div>
            <Input placeholder="Search By Name" onChange={(e) => onInputChange(e)}/>
        </div>

    );
}

export default CitySearch;