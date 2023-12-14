// Assuming you have a function to check authentication status
const checkAuthenticationStatus = () => {
    const jsonWebToken = sessionStorage.getItem("jsonWebToken");
    if (jsonWebToken) {
      const isAdmin = sessionStorage.getItem('userRole') === 'ADMINISTRATOR';
      return [true, isAdmin];
    } else {
      return [false, false];
    }
};


const authenticate = () => {
  const [isLoggedIn, isAdmin] = checkAuthenticationStatus();
  if (!isLoggedIn) {
    window.location.href = "/";
    return [false, false];
  }
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