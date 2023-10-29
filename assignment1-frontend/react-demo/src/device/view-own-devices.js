import React, { useState, useEffect } from 'react';
import { getUserDevices } from '../commons/api/device-api';
import DeviceTable from './components/device-table'; // Assuming you have a device table component
import {
    CardHeader,
    Col,
    Row
} from 'reactstrap';
import { authenticate } from '../commons/auth/auth';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";

const UserDevicesPage = () => {
 const authResult = authenticate();

  const [userDevices, setUserDevices] = useState([]);
  const userId = sessionStorage.getItem('id');
  const [isLoaded, setIsLoaded] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);

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

  if(!authResult){
    return <div>Redirecting...</div>
  }


  return (
    <div>
        <CardHeader>
          <strong> {sessionStorage.getItem("name")} Device Management </strong>
        </CardHeader>
        <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              {isLoaded && <DeviceTable tableData={userDevices}/>}
              {errorStatus > 0 && <APIResponseErrorMessage
                errorStatus={errorStatus}
                error={error}
              />}
            </Col>
          </Row>
    </div>
  );
};

export default UserDevicesPage;