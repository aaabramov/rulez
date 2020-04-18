import React from 'react';
import './App.css';
import CustomNavbar from "./navbar/CustomNavbar";
import {Container} from "reactstrap";
import {Auth} from "./auth/index";
import {Redirect, Route, Router, Switch, useHistory} from "react-router-dom";

const NewRuleConfig = React.lazy(() => import('./bl/NewRuleConfig'));
const EditRuleConfig = React.lazy(() => import('./bl/EditRuleConfig'));
const RuleConfig = React.lazy(() => import('./bl/RuleConfig'));
const RuleSets = React.lazy(() => import('./bl/RuleSets'));
const NewSecret = React.lazy(() => import('./links/NewSecret'));
const Links = React.lazy(() => import('./links/Links'));
const Login = React.lazy(() => import('./login/Login'));

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
