import { HOST } from '../hosts';
import RestApiClient from "./rest-client";
import { endpoint } from "../hosts";

function getDevices(callback) {
  let request = new Request(HOST.device_api + endpoint.device, {
    method: 'GET',
  });
  console.log(request.url);
  RestApiClient.performRequest(request, callback);
}

function getUserDevices(userId, callback) {
  let request = new Request(HOST.device_api + endpoint.device + `/user-devices/${userId}`, {
    method: 'GET',
  });
  console.log(request.url);
  RestApiClient.performRequest(request, callback);
}

function getDeviceById(params, callback) {
  let request = new Request(HOST.device_api + endpoint.device + params.id, {
    method: 'GET',
  });

  console.log(request.url);
  RestApiClient.performRequest(request, callback);
}

function postDevice(device, callback) {
  let request = new Request(HOST.device_api + endpoint.device, {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(device)
  });

  console.log("URL: " + request.url);

  RestApiClient.performRequest(request, callback);
}

function updateDevice(device, callback) {
  let request = new Request(HOST.device_api + endpoint.device + `/${device.id}`, {
    method: 'PUT',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(device)
  });

  console.log("URL: " + request.url);

  RestApiClient.performRequest(request, callback);
}

function deleteDevice(deviceId, callback) {
  let request = new Request(HOST.device_api + endpoint.device + `/${deviceId}`, {
    method: 'DELETE',
  });

  console.log("URL: " + request.url);

  RestApiClient.performRequest(request, callback);
}

export {
  getDevices,
  getDeviceById,
  postDevice,
  deleteDevice,
  updateDevice,
  getUserDevices
};