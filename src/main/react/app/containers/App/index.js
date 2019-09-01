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
import TimezoneSelect from "./timezoneSelect/index";
import {getDefaultTimezone} from "../../utils";
import moment from "moment";

export default class App extends React.Component {
  constructor() {
    super();
    this.state = {
      timezone:getDefaultTimezone(),
      offset:moment().utcOffset()
    };
  }

  render() {
    return (
      <div>
        <div className={"app-header"}>
          <h3>
            Configure Working Hours
          </h3>
          <TimezoneSelect/>
        </div>
        <TimeRangeContainer/>
        <ExcludedDateContainer/>
      </div>
    );
  }
}
