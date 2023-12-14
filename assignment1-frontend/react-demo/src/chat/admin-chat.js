import React, { useState, useContext, useEffect } from 'react';
import "react-chat-elements/dist/main.css";
import { MessageBox, Input, Button } from 'react-chat-elements';

import { Card, CardHeader, Col, Row} from 'reactstrap';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { authenticate } from "../commons/auth/auth";
import { WebSocketContext } from '../commons/websocket-context';
import { Alert } from 'react-bootstrap';

const AdminChat = () => {
    const [authResult, ] = authenticate();
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [inputKey, setInputKey] = useState(Date.now());

    const [selectedClientId, setSelectedClientId] = useState(null);
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const toggleDropdown = () => setDropdownOpen(prevState => !prevState);

    if(!authResult){
        return (<div>Redirecting...</div>);
    }

    const {sendMessage, totalMessages, setTotalMessages,
        sendReadNotificationStomp,isTyping, userIdTyping} = useContext(WebSocketContext);

    const handleSendMessage = () => {
        if(message.trim() !== '' && selectedClientId != null) {
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
                destinationUserId:  selectedClientId,  // admin // '3407384a-e4a3-40ed-a424-5e40cd04560a'
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
                destinationUserId: selectedClientId,
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
        let totalMessagesCopy = { ...totalMessages};
        for (let clientId in totalMessagesCopy) {
            totalMessagesCopy[clientId] = [];
        }
        sessionStorage.setItem('totalMessages', JSON.stringify(totalMessagesCopy));
        setTotalMessages(totalMessagesCopy);
    };


    if(totalMessages && Object.keys(totalMessages).length === 0){
        return (<div>Awaiting messages from clients...</div>);
    }

    return (
        <div>
            <CardHeader>
                <strong> Support Chat Admin View</strong>
                <br />
                {selectedClientId &&  <Button
                                    color="white"
                                    backgroundColor="red"
                                    text="Clear messages"
                                    onClick={clearMessages}
                                />}
            </CardHeader>
            <Card>
            {selectedClientId && selectedClientId === userIdTyping && isTyping && (
                    <div style={{ textAlign: 'center', color: 'gray', marginBottom: '10px' }}>
                        <em>
                            {sessionStorage.getItem('userNames') 
                            && JSON.parse(sessionStorage.getItem('userNames'))[selectedClientId]
                            || 'The client'} is typing...</em>
                    </div>
                )}
                <br />
                <Row>
                    <Col sm={{ size: '8', offset: 1 }}>
                        <Dropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
                            <DropdownToggle caret>
                                {sessionStorage.getItem('userNames') && JSON.parse(sessionStorage.getItem('userNames'))[selectedClientId]
                                             || 'Select a client'}
                            </DropdownToggle>
                            <DropdownMenu>
                                {totalMessages && Object.keys(totalMessages).map((clientId, index) => (
                                    <DropdownItem
                                        key={index}
                                        onClick={() =>{
                                            sessionStorage.setItem('selectedClientId', clientId);
                                            setSelectedClientId(clientId);
                                            sendReadNotificationStomp();
                                           }       
                                        }
                                        active={selectedClientId === clientId}
                                    >
                                        {
                                            sessionStorage.getItem('userNames') && JSON.parse(sessionStorage.getItem('userNames'))[clientId]
                                            || clientId
                                        }
                                    </DropdownItem>
                                ))}
                            </DropdownMenu>
                        </Dropdown>
                    </Col>
                </Row>
                <br />
                <Row>
                    <Col sm={{ size: '8', offset: 1 }}>
                    {selectedClientId && <h2>Send a message to the client </h2>}
                       
                        {totalMessages[selectedClientId] &&
                        totalMessages[selectedClientId].map((msg, index) => (
                            <MessageBox key={index} {...msg} />
                        ))}
                    </Col>
                </Row>
                <br />
                <Row>
                    <Col sm={{ size: '8', offset: 1 }}>
                    {selectedClientId && selectedClientId === userIdTyping &&  isTyping &&
                    <Alert show={isTyping} key="light" variant="light">
                      {sessionStorage.getItem('userNames') 
                            && JSON.parse(sessionStorage.getItem('userNames'))[selectedClientId]
                            || 'The client'} is typing...
                      </Alert>}
                    {selectedClientId &&
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
                        /> }
                    </Col>
                </Row>
            </Card>
        </div>
    );
};

export default AdminChat;
