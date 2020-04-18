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
    NavItem,
    NavLink,
    UncontrolledDropdown
} from 'reactstrap';
import PropTypes from 'prop-types';
import {Auth} from "../auth/index";
import {NavLink as RouterNavLink} from "react-router-dom";


const Item = (props) => {
    return (
        <NavItem>
            <NavLink tag={RouterNavLink} to={props.href} exact activeClassName="active">{props.name}</NavLink>
        </NavItem>
    );
};

Item.propTypes = {
    href: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
};

const CustomNavbar = (props) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggle = () => setIsOpen(!isOpen);

    const handleLogout = () => {
        Auth.logout();
        props.onLogout();
    };

    return (
        <div>
            <Navbar color="light" light expand="md">
                <NavbarBrand href="/">Rulez App</NavbarBrand>
                <NavbarToggler onClick={toggle}/>
                <Collapse isOpen={isOpen} navbar>
                    <Nav className="mr-auto" navbar>
                        <Item href="/secrets" name="Secrets"/>
                        <Item href="/rules" name="Rules"/>
                        <Item href="/rules/new" name="New Rule"/>
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
                                <Button onClick={() => props.register()}>Register</Button>
                        }
                    </NavbarText>
                </Collapse>
            </Navbar>
        </div>
    );
};

export default CustomNavbar;