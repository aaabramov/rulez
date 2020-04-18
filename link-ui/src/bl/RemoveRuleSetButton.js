import React, {useEffect, useState} from "react";
import axios from "axios";
import {Button, Spinner} from "reactstrap";
import {FaTrashAlt} from "react-icons/all";
import PropTypes from "prop-types";

const RemoveRuleSetButton = ({ruleId, idx, elems, setElems, disabled}) => {

    const [clicked, setClicked] = useState(false);

    useEffect(() => {
        if (clicked) {
            const res = async () => {
                const _ = await axios.delete(`http://localhost:9000/api/v1/rules/${ruleId}`);
                setClicked(false);
                const newElems = [...elems];
                newElems.splice(idx, 1);
                setElems(newElems);
            };
            res();
        }
    }, [clicked]);

    return (
        <Button
            color="danger"
            disabled={disabled || clicked}
            block
            onClick={_ => setClicked(true)}>
            {clicked ? <Spinner size="sm"/> : <FaTrashAlt/>}
        </Button>
    )
};

RemoveRuleSetButton.propTypes = {
    ruleId: PropTypes.number.isRequired,
    idx: PropTypes.number.isRequired,
    elems: PropTypes.array.isRequired,
    setElems: PropTypes.func.isRequired,
    disabled: PropTypes.bool.isRequired
};

export default RemoveRuleSetButton;