import React from 'react'
import logo from './commons/images/icon.png';

import {
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    Navbar,
    NavbarBrand,
    NavLink,
    UncontrolledDropdown
} from 'reactstrap';

import { authenticate } from './commons/auth/auth';

const textStyle = {
    color: 'white',
    textDecoration: 'none'
};

const NavigationBar = () => {
 const [authResult, isAdmin] = authenticate();

 if(!authResult){
        return <div>Redirecting...</div>
}

 return (
    <div>
        <Navbar color="dark" light expand="md">
            <NavbarBrand href="/home">
                <img src={logo} width={"50"}
                     height={"35"} alt="energy-logo" />
            </NavbarBrand>
            <Nav className="mr-auto" navbar>

                <UncontrolledDropdown nav inNavbar>
                    <DropdownToggle style={textStyle} nav caret>
                       Menu
                    </DropdownToggle>
                    <DropdownMenu right >

                    {isAdmin && (
                        <DropdownItem>
                            <NavLink href="/user">Users</NavLink>
                        </DropdownItem>)}

                    {isAdmin && (
                        <DropdownItem>
                            <NavLink href="/device">Devices</NavLink>
                        </DropdownItem>)}

                        <DropdownItem>
                            <NavLink href="/user-devices">My Devices</NavLink>
                        </DropdownItem>


                       {!isAdmin && ( 
                        <DropdownItem>
                            <NavLink href="/chat">Support chat</NavLink>
                        </DropdownItem>)}

                        {isAdmin && ( 
                        <DropdownItem>
                            <NavLink href="/admin-chat">Support chat</NavLink>
                        </DropdownItem>)}

                        <DropdownItem>
                            <NavLink href="/logout"> Logout</NavLink>
                        </DropdownItem>


                    </DropdownMenu>
                </UncontrolledDropdown>

            </Nav>
        </Navbar>
    </div>
 );
}

export default NavigationBar
