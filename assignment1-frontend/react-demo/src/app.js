import React from 'react';
import {BrowserRouter as Router, Route, Switch, Redirect} from 'react-router-dom'
import NavigationBar from './navigation-bar'
import Home from './home/home';
import Chat from './chat/chat';

import ErrorPage from './commons/errorhandling/error-page';
import styles from './commons/styles/project-style.css';
import UserContainer from './user/user-container';
import DeviceContainer from './device/device-container';
import Login from './commons/auth/login';
import Logout from './commons/auth/logout';
import { checkAuthenticationStatus } from './commons/auth/auth';
import UserDevicesPage from './device/view-own-devices';
import WebSocketComponent from './commons/websocket';
import DeviceConsumptionPage from './device/view-consumption';
import { WebSocketProvider } from './commons/websocket-context';
import AdminChat from './chat/admin-chat';

const App = () => {
  const [isLoggedIn, isAdmin] = checkAuthenticationStatus();

    return (
        <div className={styles.back}>
        <Router>
          <div>
            {isLoggedIn && <NavigationBar />}
            {isLoggedIn && <WebSocketComponent />}
            <Switch>
              <Redirect exact from="/" to="/login" />
              <Route exact path='/login' component={Login} />
              <Route exact path='/logout' component={Logout} />
              <WebSocketProvider>
              <Route exact path='/home' component={Home} />
              {!isAdmin && <Route exact path='/chat' component={Chat} />}
              {isAdmin && <Route exact path='/admin-chat' component={AdminChat} />}
              <Route exact path='/user' component={UserContainer} />
              <Route exact path='/device' component={DeviceContainer} />
              <Route exact path='/user-devices' component={UserDevicesPage} />
              <Route exact path='/device-consumption' component={DeviceConsumptionPage} />
              </WebSocketProvider>
              <Route exact path='/error' component={ErrorPage} />
              <Route component={ErrorPage} />
            </Switch>
          </div>
        </Router>
      </div>
    );
  };
  
  export default App;
