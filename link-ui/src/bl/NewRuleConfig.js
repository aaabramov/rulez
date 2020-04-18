import React from 'react';
import {useHistory} from "react-router-dom";
import axios from 'axios';
import RuleConfigForm from "./RuleConfigForm";
import Loading from "../components/util/Loading";

const NewRuleConfig = (props) => {

    console.log("New Rule");

    let history = useHistory();

    const handleSubmit = (ruleSet) => {
        axios
            .post('http://localhost:9000/api/v1/rules', ruleSet)
            .then(function (response) {
                history.push("/rules")
            })
            .catch(function (error) {
                console.log(error)
            })
    };

    const content = () => (
        <RuleConfigForm history={history}
                        onSubmit={handleSubmit}
                        title="New Rule config"/>
    );

    return (
        <Loading loading={false} content={content}/>
    );
};

export default NewRuleConfig;