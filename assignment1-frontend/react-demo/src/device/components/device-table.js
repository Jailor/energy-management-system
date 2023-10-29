import React from "react";
import Table from "../../commons/tables/table";

const columns = [
  {
    Header: 'Description',
    accessor: 'description',
  },
  {
    Header: 'Address',
    accessor: 'address',
  },
  {
    Header: 'Max Energy Consumption',
    accessor: 'maxEnergyConsumption',
  },
  {
    Header: 'Username',
    accessor: 'user.username',
  },
  {
    Header: 'Name',
    accessor: 'user.name',
  }
];

const filters = [
  {
    accessor: 'address',
  }
];

const DeviceTable = (props) => {
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

export default DeviceTable;