import {HOST} from '../hosts';
import RestApiClient from "./rest-client";
import {endpoint} from "../hosts";

function getUsers(callback) {
    let request = new Request(HOST.user_api + endpoint.user, {
        method: 'GET',
    });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function getUserById(params, callback){
    let request = new Request(HOST.user_api + endpoint.user + params.id, {
       method: 'GET'
    });

    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function postUser(user, callback){
    let request = new Request(HOST.user_api + endpoint.user , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function updateUser(user, callback){
    let request = new Request(HOST.user_api + endpoint.user  + `/${user.id}`, {
        method: 'PUT',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

function deleteUser(userId, callback) {
    let request = new Request(HOST.user_api + endpoint.user + `/${userId}`, {
      method: 'DELETE',
    });
  
    console.log("URL: " + request.url);
  
    RestApiClient.performRequest(request, callback);
  }
function loginUser(userDTO, callback){
    let request = new Request(HOST.user_api + endpoint.login , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userDTO)
    });

    console.log("URL: " + request.url);

    RestApiClient.performRequest(request, callback);
}

export {
    getUsers,
    getUserById,
    postUser,
    deleteUser,
    updateUser,
    loginUser
};
