import {Form, InputNumber, Popconfirm, Table, Typography, Input, Tooltip} from 'antd';
import {useEffect, useState} from 'react';
import {Axios} from "../../common/Axios.jsx";

const EditableCell = ({
                          editing,
                          dataIndex,
                          title,
                          inputType,
                          record,
                          index,
                          children,
                          ...restProps
                      }) => {
    const inputNode = inputType === 'number' ? <InputNumber/> : <Input/>;
    return (
        <td {...restProps}>
            {editing ? (
                <Form.Item
                    name={dataIndex}
                    style={{
                        margin: 0,
                    }}
                    rules={[
                        {
                            required: true,
                            message: `Please Input ${title}!`,
                        },
                    ]}
                >
                    {inputNode}
                </Form.Item>
            ) : (
                children
            )}
        </td>
    );
};
const CityList = (props) => {
    const [form] = Form.useForm();
    const [editingKey, setEditingKey] = useState('');

    useEffect(() => {
        cancel();
    }, [props.searchName]);
    const isEditing = (record) => {
        return record.id === editingKey;
    }
    const edit = (record) => {
        console.log(record);
        form.setFieldsValue({
            name: '',
            photo: '',
            ...record,
        });
        setEditingKey(record.id);
    };
    const onPageChange = (page) => {
        setEditingKey('');
        props.fetchData(page);
    };

    const cancel = () => {
        setEditingKey('');
    };
    const save = async (key) => {
        try {
            const row = await form.validateFields();
            const newData = [...data];
            const index = newData.findIndex((item) => key === item.id);
            if (index > -1) {
                const item = newData[index];
                row.id=item.id;
                await Axios.put("/cities", row);
                setEditingKey('');
                props.fetchData(currentPage-1);
            }
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
        }
    };
    const columns = [
        {
            title: 'id',
            dataIndex: 'id',
            width: '5%',
            editable: false,
        },
        {
            title: 'name',
            dataIndex: 'name',
            width: '10%',
            editable: true,
        },
        {
            title: 'photo url',
            dataIndex: 'photo',
            ellipsis: {
                showTitle: false,
            },
            render: (photoUrl) => (
                <Tooltip placement="topLeft" title={photoUrl}>
                    {photoUrl}
                </Tooltip>
            ),
            editable: true,
        },
        {
            title: 'photo',
            render: (text) => <img src={text.photo} alt={text.name}/>,
            width: '40%',
            fixed: 'right',
            editable: false,
        },
        {
            title: 'operation',
            dataIndex: 'operation',
            fixed: 'right',
            width: '15%',
            render: (_, record) => {
                const editable = isEditing(record);
                return editable ? (
                    <span>
            <Typography.Link
                onClick={() => save(record.id)}
                style={{
                    marginRight: 8,
                }}
            >
              Save
            </Typography.Link>
            <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
              <a>Cancel</a>
            </Popconfirm>
          </span>
                ) : (
                    <Typography.Link disabled={editingKey !== ''} onClick={() => edit(record)}>
                        Edit
                    </Typography.Link>
                );
            },
        },
    ];
    const mergedColumns = columns.map((col) => {
        if (!col.editable) {
            return col;
        }
        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: col.dataIndex === 'id' ? 'number' : 'text',
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });
    let {data,pageNo, currentPage, totalElements} = props.tableData;
    return (
        <Form form={form} component={false}>
            <Table
                components={{
                    body: {
                        cell: EditableCell,
                    },
                }}
                bordered
                dataSource={data}
                columns={mergedColumns}
                rowClassName="editable-row"
                rowKey="id"
                pagination={{
                    onChange: (page) => onPageChange(page-1),
                    defaultPageSize:10,
                    current: currentPage,
                    showSizeChanger: false,
                    defaultCurrent: pageNo,
                    total: totalElements,
                }}
            />
        </Form>
    );
};

export default CityList;