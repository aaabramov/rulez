import React from 'react';
import {useForm} from "react-hook-form";
import {Col, Form, FormFeedback, FormGroup, FormText, Input, Label, Row} from "reactstrap";
import ContentHeader from "./ContentHeader";
import KeyValueTable from "./KeyValueTable";
import PropTypes from "prop-types";

const RuleConfigForm = ({title, history, onSubmit, ruleSet}) => {

    const {register, control, handleSubmit} = useForm({
        defaultValues: {...ruleSet.ruleSet, conditions: ruleSet.conditions, rules: ruleSet.rules}
    });


    const handleSubmitInternal = data => {
        const rules = data.rules || [];
        const conditions = data.conditions || [];
        const result = {...data, rules, conditions};
        console.log("Submitting", result);
        onSubmit(result);
    };

    return (
        <>
            <ContentHeader history={history} title={title}/>
            <Form onSubmit={handleSubmit(handleSubmitInternal)}>
                <FormGroup row>
                    <Label for="name" sm={2}>Description</Label>
                    <Col sm={10}>
                        <Input name="name"
                               placeholder="Block USA cards"
                               innerRef={register({required: true})}/>
                        <FormFeedback>Please provide a valid name</FormFeedback>
                        <FormText>Description of Rule set.</FormText>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Label for="author" sm={2}>Author</Label>
                    <Col sm={10}>
                        <Input name="author"
                               placeholder="user@example.com"
                               innerRef={register({required: true})}/>
                    </Col>
                </FormGroup>
                <Row>
                    <Col sm={2}>
                        Conditions
                    </Col>
                    <Col sm={10}>
                        <KeyValueTable control={control} register={register} name="conditions"/>
                    </Col>
                </Row>
                <Row>
                    <Col sm={2}>
                        Rules
                    </Col>
                    <Col sm={10}>
                        <KeyValueTable control={control} register={register} name="rules"/>
                    </Col>
                </Row>
                <Row>
                    <Col sm={12} md={{size: 10, offset: 2}}>
                        <Input type="submit" className="btn btn-primary btn-block" value="Save"/>
                    </Col>
                </Row>
            </Form>
        </>


    );
};

RuleConfigForm.propTypes = {
    title: PropTypes.string.isRequired,
    ruleSet: PropTypes.object.isRequired,
    onSubmit: PropTypes.func.isRequired,
    history: PropTypes.object.isRequired
};

RuleConfigForm.defaultProps = {
    ruleSet: {ruleSet: {name: "", author: ""}, conditions: [], rules: []}
};

export default RuleConfigForm;