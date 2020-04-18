import React from 'react';
import {Button, Input, Table} from "reactstrap";
import {useFieldArray} from "react-hook-form";
import {FaPlusSquare, FaTrashAlt} from "react-icons/all";
import PropTypes from "prop-types";

const KeyValueTable = ({name, register, control}) => {

    const {fields, append, remove} = useFieldArray({
        control,
        name
    });

    return (
        <Table hover striped bordered>
            <tbody>
            {
                fields.map(({key, value}, i) => (
                    <tr key={i}>
                        <td><Input name={`${name}[${i}].key`} innerRef={register()} placeholder="Key"/></td>
                        <td><Input name={`${name}[${i}].value`} innerRef={register()} placeholder="Value"/></td>
                        <td><Button color="danger" block onClick={_ => remove(i)}><FaTrashAlt/></Button></td>
                    </tr>
                ))
            }
            <tr>
                <td colSpan={3}>
                    <Button color="secondary"
                            onClick={_ => append({key: "", value: ""})}
                            block>
                        <FaPlusSquare/>
                    </Button>
                </td>
            </tr>
            </tbody>
        </Table>
    );
};

KeyValueTable.propTypes = {
    name: PropTypes.string.isRequired,
    register: PropTypes.any.isRequired,
    control: PropTypes.any.isRequired
};

export default KeyValueTable;