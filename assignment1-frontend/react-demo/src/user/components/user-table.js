import React from "react";
import Table from "../../commons/tables/table";


const columns = [
    {
        Header: 'Username',
        accessor: 'username',
    },
    {
        Header: 'Role',
        accessor: 'role',
    },
    {
        Header: 'Full Name',
        accessor: 'name',
    },
];

const filters = [
    {
        accessor: 'username',
    }
];

const UserTable = (props) => {
  const { tableData, handleRowClick } = props;

  return (
    <Table
      data={tableData}
      columns={columns}
      search={filters}
      pageSize={5}
      handleRowClick={handleRowClick}
    />
  );
};

export default UserTable;