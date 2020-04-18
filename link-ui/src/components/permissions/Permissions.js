import React, {useEffect, useState} from "react";
import axios from "axios";
import {Button, Table} from "reactstrap";
import Loading from "../util/Loading";
import {Auth, Permissions as P} from "../../auth";
import {FaPlusSquare} from "react-icons/all";

const Permissions = ({history}) => {

    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const res = async () => {
            const data = await axios('http://localhost:9000/api/v1/permissions')
                .then(response => response.data)
                .catch(error => setError(error));
            setPermissions(data);
            setLoading(false)
        };
        res();
    }, []);

    const content = () => (
        <Table hover striped bordered responsive>
            <thead>
            <tr>
                <th>name</th>
                <th>Created At</th>
                <th>Updated At</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td colSpan={3}/>
                <td>
                    <Button color="primary"
                            onClick={() => history.push("/permissions/new")}
                            block
                            disabled={!Auth.hasPermission(P.CREATE)}>
                        <FaPlusSquare/>
                    </Button></td>
            </tr>
            {
                permissions.map(({name, createdAt, updatedAt}, i) =>
                    <tr key={i}>
                        <td><code>{name}</code></td>
                        <td>{createdAt}</td>
                        <td>{updatedAt}</td>
                        <td></td>
                    </tr>
                )
            }
            </tbody>
        </Table>
    );

    return (
        <Loading loading={loading} error={error} content={content}/>
    )
};

export default Permissions;