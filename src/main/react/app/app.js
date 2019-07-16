/**
 * app.js
 *
 * This is the entry file for the application.
 */

// Import all the third party stuff
import React from "react";
import ReactDOM from "react-dom";
import "sanitize.css/sanitize.css";
// Import root app
import App from "containers/App";
import moment from 'moment-timezone';

window.Holidays = require('date-holidays')

var now = new Date();
var momentFromDate = moment(now);
var utcDateTime = momentFromDate.utc();
var dateTimeString = utcDateTime.format();
var momentFromString = moment(dateTimeString);


// console.log('UTC TIME: ' + dateTimeString);
//
// console.log('LA TIME (from moment object): ' + utcDateTime.tz('America/Los_Angeles').format());
// console.log('LA TIME (from ' + dateTimeString + '): ' + momentFromString.tz('America/Los_Angeles').format());


const MOUNT_NODE = document.getElementById("container-excluded-dates");
const render = () => {
  ReactDOM.render(
    <App/>,
    MOUNT_NODE
  );
};


if (module.hot) {
  // Hot reloadable React components and translation json files
  // modules.hot.accept does not accept dynamic dependencies,
  // have to be constants at compile-time
  module.hot.accept(["containers/App"], () => {
    ReactDOM.unmountComponentAtNode(MOUNT_NODE);
    render();
  });
}

render();
