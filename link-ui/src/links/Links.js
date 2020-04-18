import React from 'react';
import {Badge, Button, Table} from 'reactstrap';
import PropTypes from 'prop-types';
import {FaCheck, FaPencilAlt, FaRegCopy, FaTrashAlt, FaWindowClose, FaEye, FaPlusSquare} from 'react-icons/all'
import {CopyToClipboard} from 'react-copy-to-clipboard';
import Search from "./Search";

const LinkRow = (props) => {
    const {
        uuid, valid_till, viewed
    } = props.link;
    return (
        <tr>
            <td>
                {uuid}
                <CopyToClipboard text={uuid}>
                    <Badge color="success" className="ml-1"><FaRegCopy/></Badge>
                </CopyToClipboard>
            </td>
            <td>{valid_till}</td>
            <td>{viewed ? <FaCheck/> : < FaWindowClose/>}</td>
            <td>
                <Button color="warning" className="mr-1"><FaPencilAlt/></Button>
                <Button color="danger" className="mr-1"><FaTrashAlt/></Button>
                <a href={`http://localhost:9000/api/v1/view/${uuid}`} className="btn btn-primary" target="_blank" rel="noopener noreferrer"><FaEye/></a>
            </td>
        </tr>
    );
};

LinkRow.propTypes = {
    link: PropTypes.object.isRequired
};

class Links extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            links: [],
            search: ''
        }
    }

    componentDidMount() {
        this.load();
    }

    load = (filter) => {
        const url = 'http://localhost:9000/api/v1/secrets' + (filter ? `?uuid=${filter}` : '');
        fetch(url)
            .then(r => r.json())
            .then(links => this.setState({links}))
    };

    render() {
        return (
            <Table className="mt-3" bordered hover responsive>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Valid till</th>
                    <th>viewed</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <Search onClick={this.load}/>
                    </td>
                    <td></td>
                    <td></td>
                    <td><Button color="primary" onClick={() => this.props.history.push("/secrets/new")}><FaPlusSquare /></Button></td>
                </tr>
                {this.state.links.map(l => <LinkRow key={l.uuid} link={l}/>)}
                </tbody>
            </Table>
        );
    }
}

LinkRow.propTypes = {
    className: PropTypes.string
};

export default Links;