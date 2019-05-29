/**
 *
 * App
 *
 * This component is the skeleton around the actual pages, and should only
 * contain code that should be seen on all pages. (e.g. navigation bar)
 */

import React from "react";
import { nextOccurrenceByYear, nextOccurrenceByMonth, format, formatDate } from "utils/date";

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "index.css";
import ExcludedDate from './excludedDate'


export default class App extends React.Component {
  constructor() {
    super();
    this.state = {
      excludedDate: [],
    };
  }

  handleChildEdit = (index, show) => {
    let list = this.state.excludedDate;
    if (show) {
      list.forEach(item => {
        item.edit = false;
      });
      list[index].edit = true;
    } else {
      list[index].edit = false;
    }
    this.setState({
      excludedDate: list
    });
  };

  handleChildDelete = (index) => {
    let list = this.state.excludedDate;
    list.splice(index, index + 1);
    list.forEach((item, index) => {
      item.index = index;
    });
    this.setState({
      excludedDate: list
    });
  };

  addChild = () => {

    let list = this.state.excludedDate;
    let newItem = {};
    list.push({});
    list.forEach((item, index) => {
      item.index = index;
    });
    this.setState({
      excludedDate: list
    });
  };

  render() {
    return (
      <div>
        {this.state.excludedDate.length <= 0 ?
          <div>There's no excluded dates</div> : this.state.excludedDate.map((item, index) => (
            <ExcludedDate key={index} date={item} onEdit={this.handleChildEdit} onDelete={this.handleChildDelete}
                         />
          ))}
        <button type={"button"} className='btn btn-outline-primary form-row' onClick={this.addChild}>Add</button>
      </div>
    );
  }
}
