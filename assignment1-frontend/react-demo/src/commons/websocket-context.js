import React, { createContext, useState, useEffect, useRef } from 'react';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { HOST, endpoint } from './hosts'; // Adjust the path as necessary
import {checkAuthenticationStatus } from './auth/auth';
import { withRouter } from 'react-router-dom';

export const WebSocketContext = createContext(null);


const WebSocketProviderBase = ({children, location}) => {
    const [,isAdmin] = checkAuthenticationStatus();
    const [stompClient, setStompClient] = useState(null);
    const [totalMessages, setTotalMessages] = useState(isAdmin ? {} : []);
    const [sendReadNotification, setSendReadNotification] = useState(false);
    const [isTyping, setIsTyping] = useState(false);
    const [userIdTyping, setUserIdTyping] = useState(null);
    const typingTimeoutRef = useRef(null);
    const TYPING_NOTIFICATION_TIMEOUT = 5000;

    useEffect(() => {
        const userId = sessionStorage.getItem('id');
        if (!userId) return;

        // Example of using the location prop
        console.log('Current location is:', location.pathname);

        if(isAdmin){
            if(location.pathname === '/admin-chat'){
                sendReadNotificationStomp();
            }
            else sessionStorage.setItem('selectedClientId', null);
            
        }
        else {
            if(location.pathname === '/chat'){
                sendReadNotificationStomp();
            }
        }
    }, [location, stompClient]);

    useEffect(() => {
        const userId = sessionStorage.getItem('id');
        if (!userId) return;

        if(sendReadNotification === true){
            if(isAdmin){
                if(location.pathname === '/admin-chat'){
                    sendReadNotificationStomp();
                    setSendReadNotification(false);
                }
                
            }
            else {
                if(location.pathname === '/chat'){
                    sendReadNotificationStomp();
                    setSendReadNotification(false);
                }
            }
        }
    }, [sendReadNotification]);


    // main websocket connection and message processing
    useEffect(() => {
        const userId = sessionStorage.getItem('id');
        if (!userId) return;

        // grab the received messages from the session storage if any exist
        let storedMessages = sessionStorage.getItem('totalMessages');
        if (storedMessages) {
            setTotalMessages(JSON.parse(storedMessages));
        }
        else {
            console.log('No messages in session storage');
        }

        const socket = new SockJS(HOST.chat_api + endpoint.ws);
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            const userId = sessionStorage.getItem("id");
            stompClient.subscribe('/topic/' + userId, (notification) => {
                const message = JSON.parse(notification.body);
                if(message.messageType === 'text'){
                   processMessageText(message);
                }
                else if (message.messageType === 'read-notification'){
                    processMessageReadNotification(message);
                } else if (message.messageType === 'typing-notification'){
                    processMessageTypingNotification(message);
                }
               
            });
            setStompClient(stompClient);
          });
        return () => {
            if (stompClient) {
                stompClient.disconnect();
            }
        };
    }, []);

    // process the reception of a text message
    const processMessageText = (message) => {
        if(isAdmin){
            // handle message reception for admin
            const sourceUserId = message.sourceUserId;
            let chatMessage = message.message;
            setTotalMessages(prevMessages => {
                const updatedMessages = { ...prevMessages };
                updatedMessages[sourceUserId] = updatedMessages[sourceUserId] ? [...updatedMessages[sourceUserId], chatMessage] : [chatMessage];
                sessionStorage.setItem('totalMessages', JSON.stringify(updatedMessages));
                return updatedMessages;
            });
            addUsername(sourceUserId, chatMessage);

            if(location.pathname === '/admin-chat'){
                //console.log("Received message in admin and sending automatic read notification")
                setSendReadNotification(true);
                //sendReadNotificationAdmin();
            }
        }
        else {
            // handle message reception for clients
            let chatMessage = message.message;
            setTotalMessages(prevMessages => [...prevMessages, chatMessage]);
            
            //set the messages in the session storage
            let storedMessages = sessionStorage.getItem('totalMessages');
            if (storedMessages) {
                sessionStorage.setItem('totalMessages', JSON.stringify([...JSON.parse(storedMessages), chatMessage]));
            }
            else {
                sessionStorage.setItem('totalMessages', JSON.stringify([chatMessage]));
            }

            if(location.pathname === '/chat'){
                //console.log("Received message in client and sending automatic read notification")
                setSendReadNotification(true);
                //sendReadNotificationClient()
            }
        }
    }

    // process a message of type read notification, updating the current messages as being read
    const processMessageReadNotification = (message) => {
        // console.log('Received read notification from server');
        // console.log(message);
        let storedMessages = sessionStorage.getItem('totalMessages');
        if(isAdmin){
            // mark the messages as read for the client that sent the notification
            if (storedMessages) {
                let messagesCopy = { ...JSON.parse(storedMessages) };
                if( messagesCopy[message.sourceUserId] !== undefined  && messagesCopy[message.sourceUserId] !== null){
                    messagesCopy[message.sourceUserId] = messagesCopy[message.sourceUserId].map(m => {
                        if(m.status){
                            m.status = 'read';
                        }
                        return m;
                    });
                    sessionStorage.setItem('totalMessages', JSON.stringify(messagesCopy));
                    setTotalMessages(messagesCopy);
                }
            }
        }
        else {
            // simple case, grab the messages and those that have the admin id
            // will be marked as read with their state
            if (storedMessages) {
                let messagesCopy = [...JSON.parse(storedMessages)];
                let updatedMessages = messagesCopy.map(m => {
                    if(m.userId === message.destinationUserId){
                        m.status = 'read';
                    }
                    return m;
                });
                sessionStorage.setItem('totalMessages', JSON.stringify(updatedMessages));
                setTotalMessages(updatedMessages);
            }
        }
    }

    const processMessageTypingNotification = (message) => {
        console.log('The other user is typing...');
        setIsTyping(true);
        setUserIdTyping(message.sourceUserId);
        if (typingTimeoutRef.current) {
            clearTimeout(typingTimeoutRef.current);
        }
        typingTimeoutRef.current = setTimeout(() => {
            setIsTyping(false);
            setUserIdTyping(null);
            console.log('The other user stopped typing...');
        }, TYPING_NOTIFICATION_TIMEOUT);
    }

    const sendMessage = (destination, message) => {
        if(message.messageType === 'text'){
            // add the message to the array before sending it
            if(isAdmin){
                setTotalMessages(prevMessages => {
                    const updatedMessages = { ...prevMessages };
                    updatedMessages[message.destinationUserId] = updatedMessages[message.destinationUserId] ? [...updatedMessages[message.destinationUserId], message.message] : [message.message];
                    sessionStorage.setItem('totalMessages', JSON.stringify(updatedMessages));
                    return updatedMessages;
                });
            }
            else { 
                setTotalMessages(prevMessages => [...prevMessages, message.message]);
                let storedMessages = sessionStorage.getItem('totalMessages');
                if (storedMessages) {
                    sessionStorage.setItem('totalMessages', JSON.stringify([...JSON.parse(storedMessages), message.message]));
                }
                else {
                    sessionStorage.setItem('totalMessages', JSON.stringify([message.message]));
                }
            }
            
        } // valid:  'read-notification' and 'typing-notification'

        if (stompClient && stompClient.connected) {
            stompClient.send(destination, {}, JSON.stringify(message));
        }
        else  console.log('Stomp client not connected');
       
    };

    const sendReadNotificationStomp = () => {
        let destinationUserId;
    
        if (isAdmin) {
            // For admin, send to the selected client
            destinationUserId = sessionStorage.getItem('selectedClientId');
            if (!destinationUserId) return; // If no client is selected, exit the function
        } else {
            // For a client, send to the admin's user ID
            destinationUserId = 'a0856447-425c-4407-8a4c-66fc57afb587'; // Admin's user ID
        }
    
        const newMessage = {
            id : Date.now(),
            title : sessionStorage.getItem('name'),
            userId: sessionStorage.getItem('id'),
            type: 'text',
            text: 'this is a notification, it should not be displayed',
        };

        const toSend = 
        { message: newMessage,
          sourceUserId: sessionStorage.getItem("id"),
          destinationUserId:  destinationUserId,
          messageType: 'read-notification'};
    
        sendMessage("/app/greeting", toSend);
    };

   // add the received username to session storage
    const addUsername = (sourceUserId, chatMessage) => {
        // store the mapping between the sourceUserId and the name of the user
        const storedUserNames = sessionStorage.getItem('userNames');
        if (storedUserNames) {
            sessionStorage.setItem('userNames', JSON.stringify({ ...JSON.parse(storedUserNames), [sourceUserId]: chatMessage.title }));
        }
        else {
            sessionStorage.setItem('userNames', JSON.stringify({ [sourceUserId]: chatMessage.title }));
        }
    }

    return (
        <WebSocketContext.Provider value={{ 
            stompClient, 
            sendMessage, 
            totalMessages,
            setTotalMessages, 
            sendReadNotificationStomp,
            isTyping,
            userIdTyping}}>
            {children}
        </WebSocketContext.Provider>
    );
};

export const WebSocketProvider = withRouter(WebSocketProviderBase);

