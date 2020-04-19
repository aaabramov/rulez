import React, {useEffect, useState} from "react";
import {Button, ButtonGroup, Table} from "reactstrap";
import Loading from "../util/Loading";
import {FaEllipsisH} from "react-icons/all";
import CheckSuccess from "../icons/CheckSuccess";
import TimesDanger from "../icons/TimesDanger";
import ViewButton from "../icons/ViewButton";
import {Link} from "react-router-dom";
import PermissionsDetails from "./PermissionsDetails";
import {fetchPermissions} from "../../requests/permissions";
import DateTime from "../datetime/DateTime";
import NewPermissionButton from "./NewPermissionButton";
import ContentHeader from "../../bl/ContentHeader";

const Permissions = ({history}) => {

    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selected, setSelected] = useState(null);

    useEffect(() => {
        const res = async () => {
            const data = await fetchPermissions(setError);
            setPermissions(data);
            setLoading(false)
        };
        res();
    }, []);

    const view = (id) => setSelected(selected === id ? null : id);

    const content = () => (
        <>
            <ContentHeader history={history}
                           title={`Permissions`}
                           buttons={[<NewPermissionButton onNew={p => setPermissions([p, ...permissions])}/>]}/>
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
                {
                    permissions.map((permission) => {
                        const {id, name, description, updatedAt, active} = permission;
                        return (
                            <React.Fragment key={id}>
                                <tr>
                                    <td><Link to={`/permissions/${id}`}>{name}</Link></td>
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
                                        <PermissionsDetails permission={permission}/>
                                    </td>
                                </tr>
                            </React.Fragment>
                        )
                    })
                }
                </tbody>
            </Table>
        </>
    );

    return (
        <Loading loading={loading} error={error} content={content}/>
    )
};

export default Permissions;