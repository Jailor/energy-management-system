// Assuming you have a function to check authentication status
const checkAuthenticationStatus = () => {
    const jsonWebToken = sessionStorage.getItem("jsonWebToken");
    if (jsonWebToken) {
      return true;
    } else {
      return false;
    }
};


const authenticate = () => {
  if (!checkAuthenticationStatus()) {
    window.location.href = "/";
    return [false, false];
  }
  const userRole = sessionStorage.getItem('userRole');
  const isAdmin = userRole === 'ADMINISTRATOR';
  
  return [true, isAdmin];

  // async function fetchSecureData() {
  //   try {
  //     const securityResponse = await axios.get('/secure');
  //     return true;
  //   } catch (error) {
  //     console.error("Error fetching secure data:", error);
  //     sessionStorage.clear();
  //     window.location.href = LOGIN_URL;
  //     return false;
  //   }
  // }
  // return fetchSecureData();
};

function logout() {
    sessionStorage.clear();
    console.log("Logout");
    window.location.href = "/login";
  }

export {
    checkAuthenticationStatus,
    logout,
    authenticate
};