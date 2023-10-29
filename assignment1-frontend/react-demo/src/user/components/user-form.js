import React,{ useState, useEffect } from 'react';
import validate from "../../commons/validators/validators";
import Button from "react-bootstrap/Button";
import * as API_USERS from "../../commons/api/user-api";
import APIResponseErrorMessage from "../../commons/errorhandling/api-response-error-message";
import {Col, Row} from "reactstrap";
import { FormGroup, Input, Label, Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import '../../commons/styles/buttons-style.css'


const UserForm = ({ reloadHandler, isUpdateMode, initialData}) => {
    const [formControls, setFormControls] = useState({
      username: {
        value: '',
        placeholder: 'Enter username...',
        valid: false,
        touched: false,
        validationRules: {
          minLength: 3,
          isRequired: true
        }
      },
      name: {
        value: '',
        placeholder: 'Enter name...',
        valid: false,
        touched: false,
        validationRules: {
          minLength: 3,
          isRequired: true
        }
      },
      password: {
        value: '',
        placeholder: 'Enter password...',
        valid: false,
        touched: false,
        validationRules: {
          minLength: 5, 
          isRequired: true
        }
      },
      role: {
        value: '',
        placeholder: 'Select Role',
        valid: false,
        touched: false,
        options: ['CLIENT', 'ADMINISTRATOR'],
        validationRules: {
          isRequired: true
        }
      },
    });
  
    const [formIsValid, setFormIsValid] = useState(false);
    const [errorStatus, setErrorStatus] = useState(0);
    const [error, setError] = useState(null);

    useEffect(() => {
      if (isUpdateMode && initialData) {
        // If it's an update, set the form controls with initial data
        setFormControls({
          username: {
            value: initialData.username,
            placeholder: 'Enter username...',
            valid: true,
            touched: false,
            validationRules: {
              minLength: 3,
              isRequired: true
            }
          },
          name: {
            value: initialData.name,
            placeholder: 'Enter name...',
            valid: true,
            touched: false,
            validationRules: {
              minLength: 3,
              isRequired: true
            }
          },
          password: {
            value: '', // Password is not editable in update mode
            placeholder: 'Enter password...',
            valid: true,
            touched: false,
            validationRules: {
              minLength: 6, // Adjust as needed
              isRequired: true
            }
          },
          role: {
            value: initialData.role,
            placeholder: 'Select Role',
            valid: true,
            touched: false,
            options: ['CLIENT', 'ADMINISTRATOR'], // Add your enum values here
            validationRules: {
              isRequired: true
            }
          },
        });
      }
    }, [isUpdateMode, initialData]);
  
    useEffect(() => {
      let isValid = true;
      for (let formElementName in formControls) {
        isValid = formControls[formElementName].valid && isValid;
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
  
    const registerUser = (user) => {
      API_USERS.postUser(user, (result, status, error) => {
        if (result !== null && (status === 200 || status === 201)) {
          console.log("Successfully inserted user with id: " + result);
          reloadHandler();
        } else if (status === 409) {
          setErrorStatus(status);
          setError(error);
          console.log("User already exists!");
          console.log(error);
        } else {
          console.log(error);
          setErrorStatus(status);
          setError(error);
        }
      });
    };

    const updateUser = (user) => {
      API_USERS.updateUser(user, (result, status, error) => {
        if (result !== null && (status === 200 || status === 201)) {
          console.log("Successfully updated user with id: " + result);
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
      let user = {
        username: formControls.username.value,
        name: formControls.name.value,
        password: formControls.password.value,
        role: formControls.role.value,
      };
      if(isUpdateMode) {
        user.id = initialData.id;
        user.password = null;
        updateUser(user);
      }
      else{
        registerUser(user);
      }
      console.log(user);
      
    };
  
    const handleRoleChange = (selectedRole) => {
      setFormControls(prevFormControls => {
        const updatedControls = { ...prevFormControls };
        const updatedFormElement = updatedControls['role'];
  
        updatedFormElement.value = selectedRole;
        updatedFormElement.touched = true;
        updatedFormElement.valid = validate(selectedRole, updatedFormElement.validationRules);
  
        return updatedControls;
      });
    };
  
    return (
      <div>
        <FormGroup id='username'>
          <Label for='usernameField'> Username: </Label>
          <Input
            name='username'
            id='usernameField'
            placeholder={formControls.username.placeholder}
            onChange={handleChange}
            defaultValue={formControls.username.value}
            touched={formControls.username.touched ? 1 : 0}
            valid={formControls.username.valid}
            disabled={isUpdateMode}
            className={isUpdateMode ? 'disabled-input' : ''}
            required
          />
          {formControls.username.touched && !formControls.username.valid &&
            <div className={"error-message row"}> * Username must have at least 3 characters </div>}
        </FormGroup>
  
        <FormGroup id='name'>
          <Label for='nameField'> Name: </Label>
          <Input
            name='name'
            id='nameField'
            placeholder={formControls.name.placeholder}
            onChange={handleChange}
            defaultValue={formControls.name.value}
            touched={formControls.name.touched ? 1 : 0}
            valid={formControls.name.valid}
            required
          />
          {formControls.name.touched && !formControls.name.valid &&
            <div className={"error-message row"}> * Name must have at least 3 characters </div>}
        </FormGroup>
        
        {!isUpdateMode &&
        <FormGroup id='password'>
          <Label for='passwordField'> Password: </Label>
          <Input
            name='password'
            id='passwordField'
            type='password'
            placeholder={formControls.password.placeholder}
            onChange={handleChange}
            defaultValue={formControls.password.value}
            touched={formControls.password.touched ? 1 : 0}
            valid={formControls.password.valid}
            required
          />
          {formControls.password.touched && !formControls.password.valid &&
            <div className={"error-message row"}> * Password must have at least 6 characters </div>}
        </FormGroup>}
  
        <FormGroup id='role'>
          <Label for='roleField'> Role: </Label>
          <Dropdown
            isOpen={formControls.role.dropdownOpen}
            toggle={() => setFormControls(prevFormControls => ({ ...prevFormControls, role: { ...prevFormControls.role, dropdownOpen: !prevFormControls.role.dropdownOpen } }))}
          >
            <DropdownToggle caret>
              {formControls.role.value ? formControls.role.value : formControls.role.placeholder}
            </DropdownToggle>
            <DropdownMenu>
              {formControls.role.options.map((role, index) => (
                <DropdownItem key={index} onClick={() => handleRoleChange(role)}>{role}</DropdownItem>
              ))}
            </DropdownMenu>
          </Dropdown>
          {formControls.role.touched && !formControls.role.valid &&
            <div className={"error-message"}> * Role is required</div>}
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
  
  export default UserForm;