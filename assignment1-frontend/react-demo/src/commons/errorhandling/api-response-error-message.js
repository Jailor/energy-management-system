import React, { useEffect } from 'react'
import { useState } from 'react';
import {Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row, Alert} from 'reactstrap';
import styles from '../styles/project-style.css';

const APIResponseErrorMessage = (props) => {
    const [collapseForm, setCollapseForm] = useState(false);
    const [show, setShow] = useState(false);

    const toggleForm = () => {
      setCollapseForm(!collapseForm);
    };

  useEffect(() => {
    setShow(true);
  }, [props.error]);

  const error = props.error;
  const errorStatus = props.errorStatus;
  
  function onDismiss() {
    setShow(false);
  }

   if(error === null || error === undefined) {
    return(<div>Bad error!</div>);
   }
    // TODO: modify to controlled alert
    return (
      <div>
         <Alert color="danger" isOpen={show} toggle={onDismiss}>
          An unexpected error occurred on the server side!
          {errorStatus > 1 && <Button color="link" onClick={toggleForm}>Details...</Button>}
          </Alert>
  
        {errorStatus > 1 && (
          <Modal isOpen={collapseForm} toggle={toggleForm} className={props.className}>
            <ModalHeader toggle={toggleForm} className={styles.errorTitle}>
              Server side error information:
            </ModalHeader>
            <ModalBody>
              <Row>
                <Col xs="3"> Time: </Col> <Col xs="auto" className={styles.errorText}>{error.timestamp} </Col>
              </Row>
              <Row>
                <Col xs="3"> Resource : </Col> <Col xs="auto" className={styles.errorText}>{error.resource} </Col>
              </Row>
              <Row>
                <Col xs="3"> Error : </Col> <Col xs="auto" className={styles.errorText}>
                  {error.status} - {error.error}
                </Col>
              </Row>
              <Row>
                <Col xs="3"> Message : </Col> <Col xs="auto" className={styles.errorText}>{error.message} </Col>
              </Row>
              <Row>
                <Col xs="3"> Path : </Col> <Col xs="auto" className={styles.errorText}>{error.path} </Col>
              </Row>
              <Row>
                <Col xs="3"> Details : </Col> <Col xs="auto" className={styles.errorText}>
                  {error.details}
                </Col>
              </Row>
            </ModalBody>
            <ModalFooter>
              <Button color="danger" onClick={toggleForm}>
                Cancel
              </Button>
            </ModalFooter>
          </Modal>
        )}
      </div>
    );
  };
  
  export default APIResponseErrorMessage;

