import React from 'react';
import {Button, Form, FormGroup, Input, Label} from "reactstrap";
import {BarLoader} from "react-spinners";
import {Auth} from "../auth/index";
import PropTypes from 'prop-types';
import "./Login.css";

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            password: '',
            loading: false,
            error: null
        };
    }

    signIn = () => {
        const {register} = this.props;
        fetch(`http://localhost:9000/api/v1/${register ? "signup" : "signin"}`, {
            method: 'POST',
            body: JSON.stringify({
                email: this.state.email,
                password: this.state.password,
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(r => r.json())
            .then(result => {
                if (result.ok) {
                    Auth.login(result.jwt);
                    const to = this.props.history.location.state || {from: {pathname: "/"}};
                    this.props.history.replace(to.from.pathname)
                } else {
                    this.setState({error: result.error})
                }
            });
    };

    render() {
        const {loading, error} = this.state;
        const {register} = this.props;

        return (
            <>
                <Form>
                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="email" name="email" id="exampleEmail" placeholder="Email"
                               autoComplete="username"
                               onChange={e => this.setState({email: e.target.value})}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="password" name="password" id="examplePassword" placeholder="Password"
                               autoComplete={register ? "new-password" : "current-password"}
                               onChange={e => this.setState({password: e.target.value})}/>
                    </FormGroup>
                    <BarLoader className="login-progress" color="#FF00FF" loading={loading} size="auto"/>
                    {error && <p style={{"color": "red"}}>{error}</p>}
                    <Button className="mt-1" onClick={() => this.signIn()}>{register ? "Register" : "Login"}</Button>
                </Form>
            </>
        )
    }

}

Login.propTypes = {
    register: PropTypes.bool
};

Login.defaultProps = {
    register: false
};

export default Login;