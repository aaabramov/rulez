import React from "react";
import PropTypes from "prop-types";
import {Col, Row} from "reactstrap";
import DateTime from "../datetime/DateTime";

const RoleDetails = ({role}) => {

    const {name, description, active, createdAt, updatedAt} = role;

    return (
        <>
            <Row>
                <Col md={3}>
                    <b>Name:</b>
                </Col>
                <Col md={9}>
                    <code>{name}</code>
                </Col>
            </Row>
            <Row>
                <Col md={3}>
                    <b>Description:</b>
                </Col>
                <Col md={9}>
                    {description}
                </Col>
            </Row>
            <Row>
                <Col md={3}>
                    <b>Is active:</b>
                </Col>
                <Col md={9}>
                    {String(active)}
                </Col>
            </Row>
            <Row>
                <Col md={3}>
                    <b>Created:</b>
                </Col>
                <Col md={9}>
                    <DateTime date={createdAt}/>
                </Col>
            </Row>
            <Row>
                <Col md={3}>
                    <b>Last updated:</b>
                </Col>
                <Col md={9}>
                    <DateTime date={updatedAt}/>
                </Col>
            </Row>
        </>
    )
};

RoleDetails.propTypes = {
    role: PropTypes.object.isRequired
};

export default RoleDetails;