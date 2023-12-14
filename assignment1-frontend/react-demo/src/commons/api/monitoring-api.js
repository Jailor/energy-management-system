import { HOST } from '../hosts';
import RestApiClient from "./rest-client";
import { endpoint } from "../hosts";

function getUserDevices(userId, callback) {
  let request = new Request(HOST.monitoring_api + endpoint.monitoring + `/user-devices/${userId}`, {
    method: 'GET',
  });
  console.log(request.url);
  RestApiClient.performRequest(request, callback);
}

function getDeviceConsumption(deviceId, date, callback) {
  let request = new Request(HOST.monitoring_api + endpoint.monitoring + `/hourly-consumption/${deviceId}/${date}`, {
    method: 'GET',
  });
  console.log(request.url);
  RestApiClient.performRequest(request, callback);
}

export {
  getUserDevices,
  getDeviceConsumption
};