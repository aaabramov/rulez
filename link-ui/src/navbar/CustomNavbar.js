import React, {useState} from 'react';
import {
    Button,
    Collapse,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
    Nav,
    Navbar,
    NavbarBrand,
    NavbarText,
    NavbarToggler,
    UncontrolledDropdown
} from 'reactstrap';
import {Auth, Permissions} from "../auth/index";
import Item from "./Item";
import PropTypes from "prop-types";

const CustomNavbar = ({onLogout, register}) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    const handleLogout = () => {
        Auth.logout();
        onLogout();
    };

    return (
        <div>
            <Navbar color="light" light expand="md">
                <NavbarBrand href="/">Rulez App</NavbarBrand>
                <NavbarToggler onClick={toggle}/>
                <Collapse isOpen={isOpen} navbar>
                    <Nav className="mr-auto" navbar>
                        <Item href="/secrets" name="Secrets" disabled={!Auth.hasPermission(Permissions.VIEW)}/>
                        <Item href="/rules" name="Rules" disabled={!Auth.hasPermission(Permissions.VIEW)}/>
                        <Item href="/rules/new" name="New Rule" disabled={!Auth.hasPermission(Permissions.CREATE)}/>
                        <UncontrolledDropdown nav inNavbar>
                            <DropdownToggle nav caret>
                                Options
                            </DropdownToggle>
                            <DropdownMenu right>
                                <DropdownItem>
                                    Option 1
                                </DropdownItem>
                                <DropdownItem>
                                    Option 2
                                </DropdownItem>
                                <DropdownItem divider/>
                                <DropdownItem>
                                    Reset
                                </DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                    </Nav>
                    <NavbarText>
                        {
                            Auth.isSignedIn() ? <><span className="text-success">{Auth.email()}</span> <Button
                                    onClick={() => handleLogout()}>Log out</Button></> :
                                <Button onClick={() => register()}>Register</Button>
                        }
                    </NavbarText>
                </Collapse>
            </Navbar>
        </div>
    );
};

CustomNavbar.propTypes = {
    onLogout: PropTypes.func.isRequired,
    register: PropTypes.func.isRequired
};

export default CustomNavbar;