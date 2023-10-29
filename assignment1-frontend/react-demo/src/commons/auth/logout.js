import React from 'react';
import { logout } from './auth';
const Logout = () => {
  logout();
  
  return (
    <div>Redirecting...</div>
  );
};

export default Logout;