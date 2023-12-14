import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import {
    CardHeader,
    Col,
    Row,
    Card
} from 'reactstrap';

import { DatePicker } from 'antd';
import "react-datepicker/dist/react-datepicker.css";
import { authenticate } from '../commons/auth/auth';
import { getDeviceConsumption } from '../commons/api/monitoring-api';
import APIResponseErrorMessage from "../commons/errorhandling/api-response-error-message";

const DeviceConsumptionPage = (props) => {
   const authResult = authenticate();

  const device = props.location.state.device;
  const [selectedDate, setSelectedDate] = useState(null);
  //const [, setIsLoaded] = useState(false);
  const [errorStatus, setErrorStatus] = useState(0);
  const [error, setError] = useState(null);
  const [chartData, setChartData] = useState({});

  useEffect(() => {
    if(!authResult) return;

    if (selectedDate) {
      fetchDeviceConsumption(selectedDate);
    }
  }, [selectedDate]);

  const processAndSetChartData = (hourlyConsumptions) => {
    const hours = Array.from({ length: 24 }, (_, i) => i); // 0-23 hours
    const consumptionData = hours.map(hour => {
      const consumptionEntry = hourlyConsumptions.find(
        hc => new Date(hc.timestamp).getHours() === hour
      );
      return consumptionEntry ? consumptionEntry.hourlyConsumption : 0;
    });

    setChartData({
      labels: hours.map(hour => `${hour}:00`),
      datasets: [
        {
          label: `Consumption for Device`,
          data: consumptionData,
          fill: false,
          backgroundColor: 'rgb(75, 192, 192)',
          borderColor: 'rgba(75, 192, 192, 0.2)',
        },
      ],
    });
  };

  // TODO: install day js if all is fine
  const handleDateChange = (date) => {
    setSelectedDate(date);
    const year = date.$y;
    const month = date.$M + 1; // Month is 0-indexed, so add 1
    const day = date.$D;

    // Ensuring two digits for month and day
    const formattedMonth = month < 10 ? `0${month}` : month;
    const formattedDay = day < 10 ? `0${day}` : day;

    const formattedDate = `${year}-${formattedMonth}-${formattedDay}`;
    console.log(formattedDate);
    //const formattedDate = format(date, 'yyyy-MM-dd'); 
    //console.log(formattedDate);
  };

  const fetchDeviceConsumption = (date) => {
    if (!date || !date.$y) return;

    const year = date.$y;
    const month = date.$M + 1; // Month is 0-indexed, so add 1
    const day = date.$D;
  
    const formattedMonth = month < 10 ? `0${month}` : month;
    const formattedDay = day < 10 ? `0${day}` : day;
  
    const formattedDate = `${year}-${formattedMonth}-${formattedDay}`;
    //const formattedDate = format(date, 'yyyy-MM-dd'); 

    getDeviceConsumption(device.id, formattedDate, (result, status, error) => {
      if (result !== null && status === 200) {
        console.log(result);
        processAndSetChartData(result);
      } else {
        setErrorStatus(status);
        setError(error);
      }
    });
  };

  // Sample data for the line chart
  // const data = {
  //   labels: ['Sample 1', 'Sample 2', 'Sample 3', 'Sample 4', 'Sample 5'],
  //   datasets: [
  //     {
  //       label: `Consumption for Device`,
  //       data: [10, 20, 30, 40, 50], // Sample constant values
  //       fill: false,
  //       backgroundColor: 'rgb(75, 192, 192)',
  //       borderColor: 'rgba(75, 192, 192, 0.2)',
  //     },
  //   ],
  // };

  return (
    <div>
        <CardHeader>
          <strong> Device Consumption Chart </strong>
          <h6> Description: {device.description} </h6>
          <h6> Address: {device.address} </h6>
          <h6> Maximum energy consumption: {device.maxEnergyConsumption} </h6>
        </CardHeader>
        <Row>
          <Col sm={{ size: '8', offset: 1 }}>
            <h5>Select a Date:</h5>
            <DatePicker
              value={selectedDate}
              onChange={handleDateChange}
            />
          </Col>
        </Row>
        <Card>
          <br />
            <Row>
                <Col sm={{ size: '8', offset: 1 }}>
                    {selectedDate && ( <Line data={chartData} />)}
                    {errorStatus > 0 && <APIResponseErrorMessage
                    errorStatus={errorStatus}
                    error={error}
                    />}
                </Col>
            </Row>
          </Card>
    </div>
  );
};

export default DeviceConsumptionPage;