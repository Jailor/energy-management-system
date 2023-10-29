import React, { useState, useEffect } from 'react';
import validate from "../../commons/validators/validators";
import Button from "react-bootstrap/Button";
import * as API_DEVICES from "../../commons/api/device-api";
import * as API_USERS from "../../commons/api/user-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import { Col, Row } from "reactstrap";
import { FormGroup, Input, Label } from 'reactstrap';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import '../../commons/styles/buttons-style.css';

const DeviceForm = ({ reloadHandler, isUpdateMode, initialData }) => {
    const [formControls, setFormControls] = useState({
        description: {
          value: '',
          placeholder: 'Enter description...',
          valid: false,
          touched: false,
          validationRules: {
            minLength: 3,
            isRequired: true
          }
        },
        address: {
          value: '',
          placeholder: 'Enter address...',
          valid: false,
          touched: false,
          validationRules: {
            minLength: 3,
            isRequired: true
          }
        },
        maxEnergyConsumption: {
          value: '',
          placeholder: 'Enter max energy consumption...',
          valid: false,
          touched: false,
          validationRules: {
            isRequired: true,
            isNumber: true, 
            numberRange: { min: 1, max: 1000000000 }
          }
        },
        selectedUser: {
          value: null, 
          placeholder: 'Select User',
          valid: true, 
          touched: false,
        },
        users: [], // Array to store the list of users for the dropdown
      });

  const [formIsValid, setFormIsValid] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (isUpdateMode && initialData) {
      // If it's an update, set the form controls with initial data
      setFormControls({
        description: {
          value: initialData.description,
          placeholder: 'Enter description...',
          valid: true,
          touched: false,
          validationRules: {
            minLength: 3,
            isRequired: true
          }
        },
        address: {
          value: initialData.address,
          placeholder: 'Enter address...',
          valid: true,
          touched: false,
          validationRules: {
            minLength: 3,
            isRequired: true
          }
        },
        maxEnergyConsumption: {
          value: initialData.maxEnergyConsumption.toString(), // Convert to string for input value
          placeholder: 'Enter max energy consumption...',
          valid: true,
          touched: false,
          validationRules: {
            isRequired: true,
            isNumber: true, 
            numberRange: { min: 1, max: 1000000000 } 
          }
        },
        selectedUser: {
          value: initialData.userId, // Set the selected user ID
          placeholder: 'Select User',
          valid: true,
          touched: false,
        },
        users: [], // We'll fetch and set the users separately
      });
    }

    // Fetch and set the users for the dropdown
    API_USERS.getUsers((result, status, error) => {
    if (result !== null && status === 200) {
        console.log(result);
        
        setFormControls(prevFormControls => ({
        ...prevFormControls,
        users: result,
        }));

        setLoading(false);
    } else {
        console.log(error);
        setErrorStatus(status);
        setError(error);
    }
    });
  }, [isUpdateMode, initialData]);

  useEffect(() => {
    let isValid = true;
    for (let formElementName in formControls) {
        if (formElementName !== 'users') {
            isValid = formControls[formElementName].valid && isValid;
        }
    }
    setFormIsValid(isValid);
  }, [formControls]);

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;

    setFormControls(prevFormControls => {
      const updatedControls = { ...prevFormControls };
      const updatedFormElement = updatedControls[name];

      updatedFormElement.value = value;
      updatedFormElement.touched = true;
      updatedFormElement.valid = validate(value, updatedFormElement.validationRules);

      return updatedControls;
    });
  };

  const handleUserChange = (selectedUserId) => {
    setFormControls(prevFormControls => ({
      ...prevFormControls,
      selectedUser: {
        value: selectedUserId,
        valid: true,
        touched: true,
      },
    }));
  };

  const registerDevice = (device) => {
    API_DEVICES.postDevice(device, (result, status, error) => {
      if (result !== null && (status === 200 || status === 201)) {
        console.log("Successfully inserted device with id: " + result);
        reloadHandler();
      } else {
        console.log(error);
        setErrorStatus(status);
        setError(error);
      }
    });
  };

  const updateDevice = (device) => {
    API_DEVICES.updateDevice(device, (result, status, error) => {
      if (result !== null && (status === 200 || status === 201)) {
        console.log("Successfully updated device with id: " + result);
        reloadHandler();
      }
      else {
        console.log(error);
        setErrorStatus(status);
        setError(error);
      }
    });
  };

  const handleSubmit = () => {
    let device = {
      description: formControls.description.value,
      address: formControls.address.value,
      maxEnergyConsumption: parseInt(formControls.maxEnergyConsumption.value, 10),
      userId: formControls.selectedUser.value,
    };
    if (isUpdateMode) {
      device.id = initialData.id;
      updateDevice(device);
    }
    else {
      registerDevice(device);
    }
    console.log(device);
  };

  if(loading) {
    return (
      <div>
        Loading...
      </div>
    );
  }

  return (
    <div>
      <FormGroup id='description'>
        <Label for='descriptionField'> Description: </Label>
        <Input
          name='description'
          id='descriptionField'
          placeholder={formControls.description.placeholder}
          onChange={handleChange}
          defaultValue={formControls.description.value}
          touched={formControls.description.touched ? 1 : 0}
          valid={formControls.description.valid}
          required
        />
        {formControls.description.touched && !formControls.description.valid &&
          <div className={"error-message row"}> * Description must have at least 3 characters </div>}
      </FormGroup>

      <FormGroup id='address'>
        <Label for='addressField'> Address: </Label>
        <Input
          name='address'
          id='addressField'
          placeholder={formControls.address.placeholder}
          onChange={handleChange}
          defaultValue={formControls.address.value}
          touched={formControls.address.touched ? 1 : 0}
          valid={formControls.address.valid}
          required
        />
        {formControls.address.touched && !formControls.address.valid &&
          <div className={"error-message row"}> * Address must have at least 3 characters </div>}
      </FormGroup>

      <FormGroup id='maxEnergyConsumption'>
        <Label for='maxEnergyConsumptionField'> Max Energy Consumption: </Label>
        <Input
          name='maxEnergyConsumption'
          id='maxEnergyConsumptionField'
          type='number'
          placeholder={formControls.maxEnergyConsumption.placeholder}
          onChange={handleChange}
          defaultValue={formControls.maxEnergyConsumption.value}
          touched={formControls.maxEnergyConsumption.touched ? 1 : 0}
          valid={formControls.maxEnergyConsumption.valid}
          required
        />
        {formControls.maxEnergyConsumption.touched && !formControls.maxEnergyConsumption.valid &&
          <div className={"error-message row"}> * Please enter a valid number within the specified range </div>}
      </FormGroup>

      <FormGroup id='selectedUser'>
        <Label for='userDropdown'> Select User: </Label>
        <Dropdown
            name='selectedUser'
            id='userDropdown'
            isOpen={formControls.selectedUser.touched}
            toggle={() => setFormControls(prevFormControls => ({
            ...prevFormControls,
            selectedUser: {
                ...prevFormControls.selectedUser,
                touched: !prevFormControls.selectedUser.touched,
            },
            }))}
            >   
          <DropdownToggle caret>
            {formControls.selectedUser.value
              ? formControls.users.find(user => user.id === formControls.selectedUser.value).username
              : formControls.selectedUser.placeholder
            }
          </DropdownToggle>
          
          <DropdownMenu>
            <DropdownItem
              onClick={() => handleUserChange(null)}
              active={!formControls.selectedUser.value}
            >
              No user selected
            </DropdownItem>
            {formControls.users.map(user => (
              <DropdownItem
                key={user.id}
                onClick={() => handleUserChange(user.id)}
                active={formControls.selectedUser.value === user.id}
              >
                {`${user.username} - ${user.name}`}
              </DropdownItem>
            ))}
          </DropdownMenu>
        </Dropdown>
      </FormGroup>

      <Row>
        <Col sm={{ size: '4', offset: 8 }}>
          <Button type={"submit"} disabled={!formIsValid} onClick={handleSubmit}> Submit </Button>
        </Col>
      </Row>

      {errorStatus > 0 &&
        <APIResponseErrorMessage errorStatus={errorStatus} error={error} />
      }
    </div>
  );
};

export default DeviceForm;