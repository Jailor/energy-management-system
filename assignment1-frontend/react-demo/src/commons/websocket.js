import React, { useState, useEffect } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { HOST, endpoint } from './hosts';
import { Alert } from 'react-bootstrap';
import { withRouter } from 'react-router-dom';

const WebSocketComponent = (props) => {
    // device consumption
    const [stompClient, setStompClient] = useState(null);
    const [showNotification, setShowNotification] = useState(false);
    const [message, setMessage] = useState(null);

    const hideNotification = () => {
      setShowNotification(false);
    };

  
    // device consumption
  useEffect(() => {
    const socket = new SockJS(HOST.monitoring_api + endpoint.ws);
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, frame => {
      console.log('Connected: ' + frame);

      stompClient.subscribe('/topic', (notification) => {
          const allowedPaths = ['/device-consumption', '/home', '/user-devices'];
          if (allowedPaths.includes(props.location.pathname)) {
            const message = JSON.parse(notification.body);
            const sessionId = sessionStorage.getItem("id");
            if(message.userId === sessionId) {
              console.log('Received WebSocket Notification: ', message);
              setMessage(message.message);
              setShowNotification(true);
            }       
          }  
      });
      setStompClient(stompClient);
    });

    return () => {
        if(stompClient){
            stompClient.disconnect();
            console.log('Disconnected');
        }
    };
  }, [props.location]);

    // chat
   /* const [chatStompClient, setChatStompClient] = useState(null);
  const [chatMessage, setChatMessage] = useState(null);
  const [inputMessage, setInputMessage] = useState('');


  useEffect(() => {
    const socket = new SockJS(HOST.chat_api + endpoint.ws);
    const chatStompClient = Stomp.over(socket);

    chatStompClient.connect({}, frame => {
      console.log('Connected: ' + frame);
      const userId = sessionStorage.getItem("id");
      chatStompClient.subscribe('/topic/' + userId, (notification) => {
          const chatMessage = JSON.parse(notification.body);
          console.log('Received WebSocket Notification from chat api: ', chatMessage);
          setChatMessage(chatMessage.message);
      });
      setChatStompClient(chatStompClient);
    });

    return () => {
        if(chatStompClient){
           chatStompClient.disconnect();
            console.log('Disconnected from chat api');
        }
    };
  }, [props.location]);
*/

/*
  const sendInputMessage = () => {
    const toSend = { message: inputMessage,
                     sourceUserId: sessionStorage.getItem("id"),
                     destinationUserId: '3407384a-e4a3-40ed-a424-5e40cd04560a' };  // client2
    console.log('Sending WebSocket Notification: ', toSend);
    chatStompClient.send("/app/greeting",  {}, JSON.stringify(toSend));
  }

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setInputMessage(value);
  }
  */
  

  
  return (<div>
            {/* <div>
          <h6> WebSocket Component</h6>
          <Input
          name='nameInput'
          id='nameInput'
          type='text'
          placeholder= 'Enter name'
          onChange={handleChange}
        />
          <button onClick={sendInputMessage}>Send</button>
          <br/>
          <Label>Message received: {chatMessage}</Label>
          </div> */}
      {showNotification && (
          <Alert variant="danger" onClose={() => setShowNotification(false)} dismissible>
            {message || "The hourly energy data for the device is above the maximum!"}
         </Alert>
        )}
  </div>);
};

export default withRouter(WebSocketComponent);