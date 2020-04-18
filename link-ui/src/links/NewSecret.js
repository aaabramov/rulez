import React, {useState} from 'react';
import {Col, FormGroup, Input, Label, Row} from "reactstrap";
import InputMask from 'react-input-mask';

const NewSecret = props => {
    const {history} = props;

    const [startDate, setStartDate] = useState(new Date());

    return (
        <>
            <Row>
                <Col>
                    <FormGroup>
                        <Label for="data">Secret data:</Label>
                        <Input type="textarea" name="data" id="data"/>
                    </FormGroup>
                </Col>
            </Row>
            <Row>
                <Col>
                    <FormGroup>
                        <InputMask mask="99/99/99" />
                    </FormGroup>
                    <Input type="date" name="data" id="exp"/>
                </Col>
            </Row>
        </>
    );
};

export default NewSecret;