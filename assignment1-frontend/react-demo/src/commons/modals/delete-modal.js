import React from 'react';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from 'reactstrap';

const DeleteConfirmationModal = ({ isOpen, toggle, handleConfirm, entityName }) => {
  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>Confirmation</ModalHeader>
      <ModalBody>
        Are you sure you want to delete this {entityName}?
      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={handleConfirm}>
          Confirm
        </Button>{' '}
        <Button color="secondary" onClick={toggle}>
          Cancel
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DeleteConfirmationModal;