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
import ExcludedDate from "./excludedDate";
import TimeRange from "./timeRange";
import {getExcludedDates, getTimeRanges, setExcludedDates, setTimeRanges} from "../../api";
import {WEEKDAYS} from "./constants";
import only from "only";
import {SavingState} from "../common/savingState";
import ExcludedDateContainer from "./excludedDate/index";
import TimeRangeContainer from "./timeRange/index";


export default class App extends React.Component {
  constructor() {
    super();
    this.state = {
    };
  }





  render() {
    return (<div>
        <TimeRangeContainer/>
        <ExcludedDateContainer/>
      </div>
    );
  }
}
