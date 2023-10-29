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
import UserForm from "./components/user-form";

import * as API_USERS from "../commons/api/user-api"
import UserTable from "./components/user-table";
import '../commons/styles/buttons-style.css'
import DeleteConfirmationModal from '../commons/modals/delete-modal';
import { authenticate } from '../commons/auth/auth';
import { Redirect } from 'react-router-dom';

const UserContainer = (props) => {
    const [authResult, isAdmin] = authenticate();

    const [selected, setSelected] = useState(false);
    const [tableData, setTableData] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const [errorStatus, setErrorStatus] = useState(0);
    const [error, setError] = useState(null);
    const [selectedUser, setSelectedUser] = useState(null);
    const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
    const [isUpdateMode, setUpdateMode] = useState(false);
  
    const fetchUsers = () => {
      return API_USERS.getUsers((result, status, err) => {
        if (result !== null && status === 200) {
          setTableData(result);
          setIsLoaded(true);
        } else {
          setErrorStatus(status);
          setError(err);
        }
      });
    };

    useEffect(() => {
      if(!authResult) return;

      fetchUsers();
    }, []);
  
    const toggleForm = () => {
      setSelected(!selected);
      if(selected === true && isUpdateMode === true){
        setUpdateMode(false);
      }
    };
  
    const reload = () => {
      setIsLoaded(false);
      toggleForm();
      fetchUsers();
    };

    // update modal handlers

    const handleRowClick = (user) => {
      setSelectedUser(user);
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
      
      API_USERS.deleteUser(selectedUser.id, (result, status, error) => {
        if (result !== null && (status === 200)) {
          console.log("Successfully deleted user with id: " + selectedUser.id);
          setIsLoaded(false);
          toggleDeleteModal();
          fetchUsers();
        } else {
          setErrorStatus(status);
          setError(error);
        }
      });
    };

    if(!authResult){
      return (<div>Redirecting...</div>)
    }

    if(!isAdmin){
      return <Redirect to="/home" />;
    }
  
    return (
      <div>
        <CardHeader>
          <strong> User Management </strong>
        </CardHeader>
        <Card>
          <br />
          <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              <Button color="primary" onClick={toggleForm} className="formButton">Add User </Button>
              <Button color="info" disabled={!selectedUser || selectedUser.username === 'admin'}
               onClick={handleUpdate} className="formButton"> Update User </Button>
              <Button color="danger" disabled={!selectedUser || selectedUser.username === 'admin'} 
              onClick={handleDelete} className="formButton"> Delete User </Button>
            </Col>
          </Row>
          <br />
          <Row>
            <Col sm={{ size: '8', offset: 1 }}>
              {isLoaded && <UserTable tableData={tableData}  selectedUser={selectedUser}  handleRowClick={handleRowClick} />}
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
        entityName="user"
      />
        <Modal isOpen={selected} toggle={toggleForm} className={props.className} size="lg">
        <ModalHeader toggle={toggleForm}>
          {isUpdateMode ? 'Update User' : 'Add User'}
        </ModalHeader>
          <ModalBody>
            <UserForm reloadHandler={reload} isUpdateMode={isUpdateMode} initialData={selectedUser} cancelHandler = {cancelHandler}/>
          </ModalBody>
        </Modal>
      </div>
    );
  };
  
  export default UserContainer;