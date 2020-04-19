import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Loading from "../util/Loading";
import {fetchRoleWithPermissions} from "../../requests/roles";
import ContentHeader from "../../bl/ContentHeader";
import {Col, Input, Row} from "reactstrap";
import Select from 'react-select';
const EditRole = ({history}) => {
    const {roleId} = useParams();

    const [role, setRole] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const f = async () => {
            const response = await fetchRoleWithPermissions(roleId, setError);
            setRole(response);
            setLoading(false)
        };
        f()
    }, []);

    const content = () => {

        const options = [
            { value: 'chocolate', label: 'Chocolate' },
            { value: 'strawberry', label: 'Strawberry' },
            { value: 'vanilla', label: 'Vanilla' },
        ];

        return (
            <>
                <ContentHeader history={history}
                               title={`Edit role: ${role.name}`}/>
                <Row>
                    <Col md={3}>
                        <b>Name:</b>
                    </Col>
                    <Col md={9}>
                        {role.name}
                    </Col>
                </Row>
                <Row>
                    <Col md={3}>
                        <b>Description:</b>
                    </Col>
                    <Col md={9}>
                        {role.description}
                    </Col>
                </Row>
                <Row>
                    <Col md={3}>
                        <b>Permissions:</b>
                    </Col>
                    <Col md={9}>
                        <Select options={options}
                                isMulti
                                defaultValue={options}/>
                    </Col>
                </Row>
            </>
        );
    };

    return (
        <Loading loading={loading} content={content} error={error}/>
    );
};
export default EditRole;

