import React, { useState, useEffect } from "react";
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import Field from "./fields/Field";
import { Col, Row } from "react-bootstrap";

const Table = (props) => {
  const [data, setData] = useState(props.data || []);
  const [filters, setFilters] = useState([]);
  const [selectedRow, setSelectedRow] = useState(null);
  const [pageSize] = useState(props.pageSize || 10);
  const handleRowClick = props.handleRowClick;

  useEffect(() => {
    setData(props.data || []);
  }, [props.data]);

  const filter = (rowData) => {
    return filters.every(val => {
      if (String(val.value) === "") {
        return true;
      }
      return String(rowData[val.accessor]).includes(String(val.value)) || String(val.value).includes(String(rowData[val.accessor]));
    });
  };

  const handleChange = (value, index, header) => {
    const updatedFilters = [...filters];
    updatedFilters[index] = {
      value: value.target.value,
      accessor: header
    };
    setFilters(updatedFilters);
  };

  const getTRPropsType = (state, rowInfo) => {
    if (rowInfo) {
      return {
        onClick: () => {
          setSelectedRow(rowInfo.original);
          if(handleRowClick){
            handleRowClick(rowInfo.original);
          }
          
        },
        style: {
          textAlign: "center",
          background: rowInfo.original === selectedRow ? 'lightgray' : 'inherit',
        }
      };
    } else {
      return {};
    }
  };

  const renderSearchFields = () => {
    return props.search.map((header, index) => (
      <Col key={index}>
        <div>
          <Field id={header.accessor} label={header.accessor} onChange={(e) => handleChange(e, index, header.accessor)} />
        </div>
      </Col>
    ));
  };

  return (
    <div>
      <Row>{renderSearchFields()}</Row>
      <Row>
        <Col>
          <ReactTable
            data={data.filter(rowData => filter(rowData))}
            resolveData={data => data.map(row => row)}
            columns={props.columns}
            defaultPageSize={pageSize}
            getTrProps={getTRPropsType}
            showPagination={true}
            style={{
              height: '300px'
            }}
          />
        </Col>
      </Row>
    </div>
  );
};

export default Table;
