import React from 'react';
import './App.css';
import CustomNavbar from "./navbar/CustomNavbar";
import {Container} from "reactstrap";
import {Auth} from "./auth/index";
import {Redirect, Route, Switch, useHistory} from "react-router-dom";

const NewRuleConfig = React.lazy(() => import('./bl/NewRuleConfig'));
const EditRuleConfig = React.lazy(() => import('./bl/EditRuleConfig'));
const RuleConfig = React.lazy(() => import('./bl/RuleConfig'));
const RuleSets = React.lazy(() => import('./bl/RuleSets'));
const NewSecret = React.lazy(() => import('./links/NewSecret'));
const Links = React.lazy(() => import('./links/Links'));
const Login = React.lazy(() => import('./login/Login'));
const Permissions = React.lazy(() => import('./components/permissions/Permissions'));
const ViewPermission = React.lazy(() => import('./components/permissions/ViewPermission'));
const Roles = React.lazy(() => import('./components/roles/Roles'));
const ViewRole = React.lazy(() => import('./components/roles/ViewRole'));
const EditRole = React.lazy(() => import('./components/roles/EditRole'));

const App = (props) => {

    const history = useHistory();

    const onLogout = () => {
        history.replace("/login");
    };

    const register = () => {
        history.push("/register");
    };

    return (
        <div>
            <CustomNavbar onLogout={onLogout} register={() => register()}/>
            <Container>
                <Switch>
                    <Route path="/rules/:ruleId([0-9]+)/edit" component={EditRuleConfig}/>
                    <Route path="/rules/:ruleId([0-9]+)" component={RuleConfig}/>
                    <Route exact path="/rules/new" component={NewRuleConfig}/>
                    <Route exact path="/rules" component={RuleSets}/>
                    <Route exact path="/login" component={Login}/>
                    <Route exact path="/register"
                           render={({history}) => <Login history={history} register/>}>
                    </Route>
                    <Route exact path="/secrets/new">
                        <NewSecret/>
                    </Route>
                    <Route path="/permissions/:permissionId([0-9]+)" component={ViewPermission}/>
                    <Route exact path="/permissions" component={Permissions}/>
                    <Route path="/roles/:roleId([0-9]+)/edit" component={EditRole}/>
                    <Route exact path="/roles/:roleId([0-9]+)" component={ViewRole}/>
                    <Route exact path="/roles" component={Roles}/>
                    <PrivateRoute path="/secrets"
                                  doRender={({history}) => <Links history={history}/>}/>
                    <Route path="/">
                        <Redirect to="/rules"/>
                    </Route>
                </Switch>
            </Container>
        </div>
    );
};

// A wrapper for <Route> that redirects to the login
// screen if you're not yet authenticated.
function PrivateRoute({children, doRender, ...rest}) {
    return (
        <Route
            {...rest}
            render={(props) =>
                Auth.isSignedIn() ? (
                    doRender(props)
                ) : (
                    <Redirect
                        to={{
                            pathname: "/login",
                            state: {from: props.location}
                        }}
                    />
                )
            }
        />
    );
}

export default App;
