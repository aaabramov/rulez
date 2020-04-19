import React, {useEffect, useState} from 'react';
import {Button, Table} from 'reactstrap';
import axios from 'axios';
import {FaPlusSquare} from "react-icons/all";
import {Link} from "react-router-dom";
import Loading from "../components/util/Loading";
import RemoveRuleSetButton from "./RemoveRuleSetButton";
import {Auth, Permissions} from "../auth";
import DateTime from "../components/datetime/DateTime";

const RuleSets = props => {
    const {history} = props;
    const [ruleSets, setRuleSets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true);

        async function fetchData() {
            const data = await axios('http://localhost:9000/api/v1/rules')
                .then(r => r.data)
                .catch(e => setError(e));
            setRuleSets(data);
            setLoading(false);
        }

        fetchData();
    }, []);

    const content = () => (
        <Table className="mt-3" bordered hover responsive>
            <thead>
            <tr>
                <th>Name</th>
                <th>Author</th>
                <th>Rules count</th>
                <th>Conditions count</th>
                <th>Updated time</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td colSpan={5}/>
                <td>
                    <Button color="primary"
                            onClick={() => history.push("/rules/new")}
                            block
                            disabled={!Auth.hasPermission(Permissions.CREATE)}>
                        <FaPlusSquare/>
                    </Button>
                </td>
            </tr>
            {
                ruleSets.map((rs, i) =>
                    <tr key={rs.ruleSet.id}>
                        <td>
                            <Link to={`/rules/${rs.ruleSet.id}`}>
                                {rs.ruleSet.name}
                            </Link>
                        </td>
                        <td>{rs.ruleSet.author}</td>
                        <td>{rs.rules.length}</td>
                        <td>{rs.conditions.length}</td>
                        <td>
                            <DateTime date={new Date(rs.ruleSet.updatedAt)}/>
                        </td>
                        <td>
                            <RemoveRuleSetButton idx={i}
                                                 ruleId={rs.ruleSet.id} elems={ruleSets}
                                                 setElems={setRuleSets}
                                                 disabled={!Auth.hasPermission(Permissions.DELETE)}/>
                        </td>
                    </tr>
                )
            }
            </tbody>
        </Table>
    );

    return (
        <Loading loading={loading} content={content} error={error}/>
    );
};

// RuleSets.propTypes = {
//     history: PropTypes.object.
// };

export default RuleSets;