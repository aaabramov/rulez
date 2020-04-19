import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router-dom";
import axios from 'axios';
import RuleConfigForm from "./RuleConfigForm";
import Loading from "../components/util/Loading";

const EditRuleConfig = (props) => {

    let {ruleId} = useParams();
    let history = useHistory();

    const [loading, setLoading] = useState(true);
    const [ruleSet, setRuleSet] = useState(null);
    const [update, setUpdate] = useState(false);

    const handleSubmit = (rs) => {
        setRuleSet(rs);
        setUpdate(true);
    };

    useEffect(() => {

        const fetchData = async () => {
            const ruleSet = await axios(`http://localhost:9000/api/v1/rules/${ruleId}`)
                .then((response) => response.data)
                .catch(function (error) {
                    console.log(error)
                });

            setRuleSet(ruleSet);
            setLoading(false);
        };

        fetchData();
    }, [ruleId]);

    useEffect(() => {
        if (update) {
            const action = async () => {
                const response = await axios.put(`http://localhost:9000/api/v1/rules/${ruleId}`, ruleSet)
                    .then(function (response) {
                        history.push("/rules")
                    })
                    .catch(function (error) {
                        console.log(error)
                    });
                history.push("/rules");
            };
            action();
        }
    }, [update]);

    const content = () =>
        <RuleConfigForm history={history}
                        onSubmit={handleSubmit}
                        title={`Edit Rule config #${ruleId}`}
                        ruleSet={ruleSet}/>
    ;

    return (
        <Loading loading={loading} content={content}/>
    );
};

export default EditRuleConfig;