import React, {useEffect, useState} from "react";
import {Auth, Permissions as P} from "../../auth";
import {FaPlusSquare} from "react-icons/all";
import {Button, Form, FormGroup, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import {useForm} from "react-hook-form";
import {createPermission} from "../../requests/permissions";
import PropTypes from "prop-types";

const NewPermissionButton = ({onNew}) => {

    const [open, setOpen] = useState(false);
    const toggle = () => setOpen(!open);

    const {register, handleSubmit} = useForm();
    const [submitted, setSubmitted] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (submitted) {
            const f = async () => {
                const permission = await createPermission(submitted, setError);
                onNew(permission);
                toggle();
            };
            f();
        }
    }, [submitted]);

    return (
        <>
            <Button color="primary"
                    onClick={_ => setOpen(true)}
                    block
                    disabled={!Auth.hasPermission(P.CREATE)}>
                <FaPlusSquare/>
            </Button>
            <Modal isOpen={open} toggle={toggle}>
                <ModalHeader toggle={toggle}>Create new permissions</ModalHeader>
                <ModalBody>
                    <Form>
                        <FormGroup>
                            <Label for="name">Name</Label>
                            <Input type="text" name="name" placeholder="create"
                                   innerRef={register({required: true, minLength: 3})}/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="description">Description</Label>
                            <Input type="text" name="description" placeholder="Create new entities"
                                   innerRef={register({required: true, minLength: 5})}/>
                        </FormGroup>
                        {error && <span className="text-danger">{JSON.stringify(error)}</span>}
                    </Form>
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={handleSubmit(setSubmitted)}>Create</Button>{' '}
                    <Button color="secondary" onClick={toggle}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </>
    );
};

NewPermissionButton.propTypes = {
    onNew: PropTypes.func.isRequired
};

export default NewPermissionButton;