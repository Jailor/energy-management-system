import React from 'react';
import {BrowserRouter as Router, Route, Switch, Redirect} from 'react-router-dom'
import NavigationBar from './navigation-bar'
import Home from './home/home';

import ErrorPage from './commons/errorhandling/error-page';
import styles from './commons/styles/project-style.css';
import UserContainer from './user/user-container';
import DeviceContainer from './device/device-container';
import Login from './commons/auth/login';
import Logout from './commons/auth/logout';
import { checkAuthenticationStatus } from './commons/auth/auth';
import UserDevicesPage from './device/view-own-devices';

const App = () => {
  const isLoggedIn = checkAuthenticationStatus();

    return (
      <div className={styles.back}>
        <Router>
          <div>
            {isLoggedIn && <NavigationBar />}
            <Switch>
              <Redirect exact from="/" to="/login" />
              <Route exact path='/login' component={Login} />
              <Route exact path='/logout' component={Logout} />
              <Route exact path='/home' component={Home} />
              <Route exact path='/user' component={UserContainer} />
              <Route exact path='/device' component={DeviceContainer} />
              <Route exact path='/user-devices' component={UserDevicesPage} />
              <Route exact path='/error' component={ErrorPage} />
              <Route component={ErrorPage} />
            </Switch>
          </div>
        </Router>
      </div>
    );
  };
  
  export default App;
