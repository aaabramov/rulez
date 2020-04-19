import React, {useEffect, useState} from "react";
import {Button, ButtonGroup, Table} from "reactstrap";
import Loading from "../util/Loading";
import {Auth, Permissions as P} from "../../auth";
import {FaEllipsisH, FaPlusSquare} from "react-icons/all";
import CheckSuccess from "../icons/CheckSuccess";
import TimesDanger from "../icons/TimesDanger";
import ViewButton from "../icons/ViewButton";
import {Link} from "react-router-dom";
import PermissionsDetails from "../permissions/PermissionsDetails";
import {fetchPermissions} from "../../requests/permissions";
import {fetchRoles} from "../../requests/roles";
import RoleDetails from "./RoleDetails";
import DateTime from "../datetime/DateTime";

const Roles = ({history}) => {

    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selected, setSelected] = useState(null);

    useEffect(() => {
        const res = async () => {
            const data = await fetchRoles(setError);
            setRoles(data);
            setLoading(false)
        };
        res();
    }, []);

    const view = (id) => setSelected(selected === id ? null : id);

    const content = () => (
        <Table hover striped bordered responsive>
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Updated At</th>
                <th>Active</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td colSpan={4}/>
                <td>
                    <Button color="primary"
                            onClick={() => history.push("/roles/new")}
                            block
                            disabled={!Auth.hasPermission(P.CREATE)}>
                        <FaPlusSquare/>
                    </Button></td>
            </tr>
            {
                roles.map((role) => {
                    const {id, name, description, updatedAt, active} = role;
                    return (
                        <React.Fragment key={id}>
                            <tr>
                                <td><Link to={`/roles/${id}`}>{name}</Link></td>
                                <td>{description}</td>
                                <td><DateTime date={updatedAt}/></td>
                                <td>
                                    <div className="text-center">
                                        {active ? <CheckSuccess/> : <TimesDanger/>}
                                    </div>
                                </td>
                                <td>
                                    <ButtonGroup className="d-flex" role="group">
                                        <ViewButton className={`w-100 ${selected === id ? 'active' : ''}`}
                                                    onClick={() => view(id)}/>
                                        <Button className="w-100">
                                            <FaEllipsisH/>
                                        </Button>
                                    </ButtonGroup>
                                </td>
                            </tr>
                            <tr hidden={selected !== id}>
                                <td colSpan={5}>
                                    <RoleDetails role={role}/>
                                </td>
                            </tr>
                        </React.Fragment>
                    )
                })
            }
            </tbody>
        </Table>
    );

    return (
        <Loading loading={loading} error={error} content={content}/>
    )
};

export default Roles;