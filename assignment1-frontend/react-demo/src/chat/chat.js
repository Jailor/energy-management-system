import React, { useState, useContext } from 'react';
import "react-chat-elements/dist/main.css";
import { MessageBox, Input, Button } from 'react-chat-elements';

import { Card, CardHeader, Col, Row} from 'reactstrap';
import { Alert } from 'react-bootstrap';
import { authenticate } from "../commons/auth/auth";
import { WebSocketContext } from '../commons/websocket-context';
const Chat = () => {
    const [authResult,] = authenticate();
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [inputKey, setInputKey] = useState(Date.now());

    if(!authResult){
        return (<div>Redirecting...</div>);
    }

    const {sendMessage, totalMessages, setTotalMessages, isTyping} = useContext(WebSocketContext);

    const handleSendMessage = () => {
        if(message.trim() !== '') {
            const newMessage = {
                id : Date.now(),
                title : sessionStorage.getItem('name'),
                userId: sessionStorage.getItem('id'),
                position: 'right',
                type: 'text',
                text: message,
                status : 'received',
                dateString: new Date().toLocaleTimeString()
            };
            setMessages([...messages, newMessage]);
            setMessage('');
            setInputKey(Date.now());

            const toSend = 
              { message: newMessage,
                sourceUserId: sessionStorage.getItem("id"),
                destinationUserId:  'a0856447-425c-4407-8a4c-66fc57afb587',  // admin // '3407384a-e4a3-40ed-a424-5e40cd04560a'
                messageType: 'text'};
            sendMessage("/app/greeting", toSend);
        }
    };

    const handleSendTypingNotification = () => {
        if(message.trim() !== '') {
            const newMessage = {
                id : Date.now(),
                title : sessionStorage.getItem('name'),
                userId: sessionStorage.getItem('id'),
                position: 'right',
                type: 'text',
                text: 'this is a typing notification, it should not be displayed',
            };
            const toSend = 
              { message: newMessage,
                sourceUserId: sessionStorage.getItem("id"),
                destinationUserId:  'a0856447-425c-4407-8a4c-66fc57afb587',
                messageType: 'typing-notification'};
            sendMessage("/app/greeting", toSend);
        }
    };



    const handleMessageChange = (e) => {
        setMessage(e.target.value);
    };

    const handleKeyPress = (e) => {
        handleSendTypingNotification();
        if (e.key === 'Enter') {
            handleSendMessage();
        }
    };

    const clearMessages = () => {    
        sessionStorage.setItem('totalMessages', JSON.stringify([]));
        setTotalMessages([]);
    };
    

    return (
        <div>
            <CardHeader>
                <strong> Support Chat </strong>
                <Button
                                    color="white"
                                    backgroundColor="red"
                                    text="Clear messages"
                                    onClick={clearMessages}
                                />
            </CardHeader>
            <Card>
               {isTyping && (
                    <div style={{ textAlign: 'center', color: 'gray', marginBottom: '10px' }}>
                        <em>The administrator is typing...</em>
                    </div>
                )}
                <br />
                <Row>
                    <Col sm={{ size: '8', offset: 1 }}>
                    <h2>Send a message to the administrator</h2>
                       
                        {totalMessages.map((msg, index) => (
                            <MessageBox key={index} {...msg} />
                        ))}
                    </Col>
                </Row>
                <br />
                <Row>
                    <Col sm={{ size: '8', offset: 1 }}>
                      <Alert show={isTyping} key="light" variant="light">
                      The administrator is typing...
                      </Alert>
                        <Input
                            key={inputKey}
                            placeholder="Type your message here..."
                            value={message}
                            onKeyPress={handleKeyPress} 
                            onChange={handleMessageChange}
                            autofocus = {true}
                            rightButtons={
                                <Button
                                    color="white"
                                    backgroundColor="green"
                                    text="Send"
                                    onClick={handleSendMessage}
                                />
                            }
                        />
                    </Col>
                </Row>
            </Card>
        </div>
    );
};

export default Chat;
