import React from 'react';

import BackgroundImg from '../commons/images/background.jpg';

import {Container, Jumbotron} from 'reactstrap';
import {authenticate} from "../commons/auth/auth";
const backgroundStyle = {
    backgroundPosition: 'center',
    backgroundSize: 'cover',
    backgroundRepeat: 'no-repeat',
    width: "100%",
    height: "1920px",
    backgroundImage: `url(${BackgroundImg})`
  };
  
  const textStyle = { color: 'white' };
  
  const Home = () => {
    const authResult = authenticate();
    
    if(!authResult){
      return (<div>Redirecting...</div>)
    }

    return (
      <div>
        <Jumbotron fluid style={backgroundStyle}>
          <Container fluid>
            <h1 className="display-3" style={textStyle}>Energy platform</h1>
            <p className="lead" style={textStyle}><b>Welcome user to the energy platform!</b></p>
            <hr className="my-2" />
            <p style={textStyle}><b>Use the drop down to navigate to the available menus!</b></p>
            {/* <p className="lead">
              <Button color="primary" onClick={() => window.open('http://coned.utcluj.ro/~salomie/DS_Lic/')}>
                Learn More
              </Button>
            </p> */}
          </Container>
        </Jumbotron>
      </div>
    );
  };
  
  export default Home;
