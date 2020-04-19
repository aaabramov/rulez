import React from "react";
import "./ViewButton.css"
import {FaRegEye} from "react-icons/all";
import {Button} from "reactstrap";
import {IconContext} from "react-icons";

const ViewButton = (props) => {

    const btnProps = {
        ...props,
        className: props.className + ' btn-view'
    };

    return (
        <Button {...btnProps}>
            <IconContext.Provider value={{}}>
                <FaRegEye/>
            </IconContext.Provider>
        </Button>
    );
};

ViewButton.propTypes = Button.propTypes;
ViewButton.defaultProps = Button.defaultProps;

export default ViewButton;