import React, {useEffect, useState} from "react";
import {Link, useHistory, useParams} from "react-router-dom";
import Loading from "../util/Loading";
import ContentHeader from "../../bl/ContentHeader";
import PermissionsDetails from "./PermissionsDetails";
import {Table} from "reactstrap";
import {permissionWithUsers} from "../../requests/permissions";
import DateTime from "../datetime/DateTime";

const ViewPermission = (props) => {
    const history = useHistory();
    const {permissionId} = useParams();
    const [permission, setPermission] = useState(null);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const f = async () => {
            const [p, u] = await permissionWithUsers(permissionId, setError);
            setPermission(p);
            setUsers(u);
            setLoading(false)
        };
        f()
    }, [permissionId]);

    const content = () => {
        return (
            <>
                <ContentHeader history={history}
                               title={`Permission: ${permission.name}`}/>
                <PermissionsDetails permission={permission}/>
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
                        users.map(({role, users}, i) =>
                            users.map(({email, createdAt, active}, j) => (
                                <tr key={`${i}.${j}`}>
                                    <td>{email}</td>
                                    <td><DateTime date={createdAt}/></td>
                                    <td><Link to={`/roles/${role.id}`}>{role.name}</Link></td>
                                    <td>{String(active)}</td>
                                </tr>
                            ))
                        )
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

export default ViewPermission;