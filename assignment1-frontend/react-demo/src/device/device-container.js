import React, { useState, useEffect } from 'react';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";
import {
    Button,
    Card,
    CardHeader,
    Col,
    Modal,
    ModalBody,
    ModalHeader,
    Row
} from 'reactstrap';
import DeviceForm from "./components/device-form";

import * as API_DEVICES from "../commons/api/device-api"
import DeviceTable from "./components/device-table";
import '../commons/styles/buttons-style.css'
import DeleteConfirmationModal from '../commons/modals/delete-modal';
import { authenticate } from '../commons/auth/auth';

const DeviceContainer = (props) => {
    const [authResult, isAdmin] = authenticate();

    const [selected, setSelected] = useState(false);
    const [tableData, setTableData] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const [errorStatus, setErrorStatus] = useState(0);
    const [error, setError] = useState(null);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
    const [isUpdateMode, setUpdateMode] = useState(false);
  
    const fetchDevices = () => {
      return API_DEVICES.getDevices((result, status, err) => {
        if (result !== null && status === 200) {
          const updatedData = result.map(device => {
            if (device.user === null) {
              device.user = {
                username: "No user mapped",
                name: "No user mapped"
              };
            }
            return device;
          });

          setTableData(updatedData);
          setIsLoaded(true);
        } else {
          setErrorStatus(status);
          setError(err);
        }
      });
    };

    useEffect(() => {
      if(!authResult) return;

      fetchDevices();
    }, []);
  
    const toggleForm = () => {
      setSelected(!selected);
      if (selected === true && isUpdateMode === true) {
        setUpdateMode(false);
      }
    };
  
    const reload = () => {
      setIsLoaded(false);
      toggleForm();
      fetchDevices();
    };

    // update modal handlers

    const handleRowClick = (device) => {
      setSelectedDevice(device);
    };

    const handleUpdate = () => {
      setUpdateMode(true);
      toggleForm();
    };

    const cancelHandler = () => {
      setUpdateMode(false);
    }
  

    // delete modal handlers
    const toggleDeleteModal = () => {
      setDeleteModalOpen(!isDeleteModalOpen);
    };
  
    const handleDelete = () => {
      toggleDeleteModal();
    };
  
    const handleConfirmDelete = () => {
      API_DEVICES.deleteDevice(selectedDevice.id, (result, status, error) => {
        if (result !== null && (status === 200)) {
          console.log("Successfully deleted device with id: " + selectedDevice.id);
          setIsLoaded(false);
          toggleDeleteModal();
          fetchDevices();
        } else {
          setErrorStatus(status);
          setError(error);
        }
      });
    };

    if(!authResult) return (<div>Redirecting...</div>)

    if(!isAdmin){
      window.location.href = "/home";
      return (<div>Redirecting...</div>);
    }
  
    return (
      <div>
        <CardHeader>
          <strong> Device Management </strong>
        </CardHeader>
        <Card>
          <br />
          <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              <Button color="primary" onClick={toggleForm} className="formButton">Add Device </Button>
              <Button color="info" disabled={!selectedDevice} onClick={handleUpdate} className="formButton"> Update Device </Button>
              <Button color="danger" disabled={!selectedDevice} onClick={handleDelete} className="formButton"> Delete Device </Button>
            </Col>
          </Row>
          <br />
          <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              {isLoaded && <DeviceTable tableData={tableData} handleRowClick={handleRowClick} />}
              {errorStatus > 0 && <APIResponseErrorMessage
                errorStatus={errorStatus}
                error={error}
              />}
            </Col>
          </Row>
        </Card>
        <DeleteConfirmationModal
          isOpen={isDeleteModalOpen}
          toggle={toggleDeleteModal}
          handleConfirm={handleConfirmDelete}
          entityName="device"
        />
        <Modal isOpen={selected} toggle={toggleForm} className={props.className} size="lg">
          <ModalHeader toggle={toggleForm}>
            {isUpdateMode ? 'Update Device' : 'Add Device'}
          </ModalHeader>
          <ModalBody>
            <DeviceForm reloadHandler={reload} isUpdateMode={isUpdateMode} initialData={selectedDevice} cancelHandler={cancelHandler} />
          </ModalBody>
        </Modal>
      </div>
    );
  };
  
  export default DeviceContainer;