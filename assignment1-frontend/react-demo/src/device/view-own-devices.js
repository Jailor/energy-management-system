import React, { useState, useEffect } from 'react';
import { getUserDevices } from '../commons/api/monitoring-api';
import DeviceTable from './components/device-table'; // Assuming you have a device table component
import {
    CardHeader,
    Col,
    Row,
    Card,
    Button
} from 'reactstrap';
import { authenticate } from '../commons/auth/auth';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import { withRouter } from 'react-router-dom';

const UserDevicesPage = (props) => {
  const authResult = authenticate();

  const [userDevices, setUserDevices] = useState([]);
  const userId = sessionStorage.getItem('id');
  const [isLoaded, setIsLoaded] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);
  const [selectedDevice, setSelectedDevice] = useState(null);

  useEffect(() => {
    if(!authResult) return;

    if (userId) {
      fetchUserDevices(userId);
      setIsLoaded(true);
    }
  }, [userId]);

  const fetchUserDevices = (userId) => {
    getUserDevices(userId, (result, status, error) => {
      if (result !== null && status === 200) {
        const updatedData = result.map(device => {
          if (device.user === null) {
            device.user = {
              username: sessionStorage.getItem("username"),
              name: sessionStorage.getItem("name")
            };
          }
          return device;
        });

        setUserDevices(updatedData);
      } else {
        setErrorStatus(status);
        setError(error);
      }
    });
  };

  const handleRowClick = (device) => {
    setSelectedDevice(device);
  };

 
  const handleClick= () => {
    if (selectedDevice) {
      props.history.push({
        pathname: '/device-consumption',
        state: { device: selectedDevice }
      });
    }
  };



  if(!authResult){
    return <div>Redirecting...</div>
  }


  return (
    <div>
        <CardHeader>
          <strong> {sessionStorage.getItem("name")} Device Management </strong>
        </CardHeader>
        <Card>
          <br />
          <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              <Button color="info" disabled={!selectedDevice} onClick={handleClick} 
              className="formButton"> View Device Consumption </Button>
            </Col>
          </Row>
          <br />
        <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              {isLoaded && <DeviceTable tableData={userDevices} handleRowClick={handleRowClick}/>}
              {errorStatus > 0 && <APIResponseErrorMessage
                errorStatus={errorStatus}
                error={error}
              />}
            </Col>
          </Row>
          </Card>
    </div>
  );
};

export default withRouter(UserDevicesPage);