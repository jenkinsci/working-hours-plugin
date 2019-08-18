/**
 *
 * App
 *
 * This component is the skeleton around the actual pages, and should only
 * contain code that should be seen on all pages. (e.g. navigation bar)
 */

import React from "react";

import "react-datepicker/dist/react-datepicker.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./style/index.css";

import ExcludedDateContainer from "./excludedDate/index";
import TimeRangeContainer from "./timeRange/index";

import {PluginContext} from "../context/context";


export default class App extends React.Component {
  constructor() {
    super();
    this.state = {};
  }

  render() {
    const initialPluginContext = {
      datesLoaded: false,
      rangesLoaded: false,
    }
    return (
      <div>
        <h3>
          Configure Working Hours
        </h3>
        <TimeRangeContainer/>
        <ExcludedDateContainer/>
      </div>
    );
  }
}
