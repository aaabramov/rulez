import React, {useEffect, useState} from "react";
import {Link, useHistory, useParams} from "react-router-dom";
import Loading from "../util/Loading";
import ContentHeader from "../../bl/ContentHeader";
import {Button, Table} from "reactstrap";
import DateTime from "../datetime/DateTime";
import {rolesWithUsers} from "../../requests/roles";
import RoleDetails from "./RoleDetails";
import {FaPencilAlt} from "react-icons/all";

const ViewRole = (props) => {
    const history = useHistory();
    const {roleId} = useParams();
    const [role, setRole] = useState(null);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const f = async () => {
            const [role, users] = await rolesWithUsers(roleId, setError);
            setRole(role);
            setUsers(users);
            setLoading(false)
        };
        f()
    }, [roleId]);

    const content = () => {
        return (
            <>
                <ContentHeader history={history}
                               title={`Role: ${role.name}`}
                               buttons={[
                                   <Link to={`/roles/${roleId}/edit`}>
                                       <Button>
                                           <FaPencilAlt/>
                                       </Button>
                                   </Link>]}/>
                <RoleDetails role={role}/>
                <hr/>
                <Table hover striped bordered>
                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>Created</th>
                        <th>Role</th>
                        <th>Active</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        users.map(({id, email, createdAt, active}) => (
                            <tr key={id}>
                                <td>{email}</td>
                                <td><DateTime date={createdAt}/></td>
                                <td>{role.name}</td>
                                <td>{String(active)}</td>
                            </tr>
                        ))
                    }
                    </tbody>
                </Table>
            </>
        )
    };

    return (
        <Loading loading={loading} content={content} error={error}/>
    )
};

export default ViewRole;