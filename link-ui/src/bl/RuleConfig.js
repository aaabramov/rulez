import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {Link, useHistory, useParams} from "react-router-dom";
import {Button, Col, ListGroup, Row, Table} from "reactstrap";
import ContentHeader from "./ContentHeader";
import {FaPencilAlt} from "react-icons/all";
import Loading from "../components/util/Loading";
import {Auth, Permissions} from "../auth";

const RuleConfig = props => {
    const history = useHistory();
    const {ruleId} = useParams();
    const [ruleSet, setRuleSet] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    console.log("RuleConfig!", ruleId);

    useEffect(() => {
        async function fetchData() {

            try {
                const result = await axios(
                    `http://localhost:9000/api/v1/rules/${ruleId}`,
                );
                setRuleSet(result.data);
            } catch (e) {
                setError(e)
            }

            setLoading(false);
        }

        fetchData();
    }, [ruleId]);

    const editButton = () => {

        const btn = <Button disabled={!Auth.hasPermission(Permissions.EDIT)}><FaPencilAlt/></Button>;

        if (Auth.hasPermission(Permissions.EDIT)) {
            return (<Link
                to={`/rules/${ruleId}/edit`}>
                {btn}
            </Link>);
        } else {
            return btn;
        }
    };

    const content = () => (<>
        <ContentHeader history={history}
                       title={`Rule: ${ruleSet.ruleSet.name}`}
                       buttons={[editButton()]}/>
        <small className="text-muted">Author: <u>{ruleSet.ruleSet.author}</u></small>
        <ListGroup className="mt-1">
            <Row className="mb-1">
                <Col sm={2}>
                    Description
                </Col>
                <Col sm={10}>
                    {ruleSet.ruleSet.name}
                </Col>
            </Row>
            <Row className="mb-1">
                <Col sm={2}>
                    Conditions
                </Col>
                <Col sm={10}>
                    <Table className="table" hover striped bordered>
                        <tbody>
                        {
                            ruleSet.conditions.map(r =>
                                <tr key={r.id}>
                                    <td>{r.key}</td>
                                    <td>{r.value}</td>
                                </tr>
                            )
                        }
                        </tbody>
                    </Table>
                </Col>
            </Row>
            <Row className="mb-1">
                <Col sm={2}>
                    Rules
                </Col>
                <Col sm={10}>
                    <Table className="table" hover striped bordered>
                        <tbody>
                        {
                            ruleSet.rules.map(r =>
                                <tr key={r.id}>
                                    <td className="w-50">{r.key}</td>
                                    <td className="w-50">{r.value}</td>
                                </tr>
                            )
                        }
                        </tbody>
                    </Table>
                </Col>
            </Row>

        </ListGroup>
    </>);

    return (
        <Loading loading={loading} content={content} error={error}/>
    );
};

export default RuleConfig;