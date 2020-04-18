import {NavItem, NavLink} from "reactstrap";
import {NavLink as RouterNavLink} from "react-router-dom";
import PropTypes from "prop-types";
import React from "react";

const Item = ({href, name, disabled}) => {
    return (
        <NavItem>
            <NavLink tag={RouterNavLink}
                     to={href}
                     exact
                     activeClassName="active"
                     className={disabled ? "disabled" : ""}>
                {name}
            </NavLink>
        </NavItem>
    );
};

Item.propTypes = {
    href: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    disabled: PropTypes.bool
};

Item.defaultProps = {
    disabled: false
};

export default Item;