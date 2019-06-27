import React from "react";
import {DATE_TYPE, getDatePresets, PERIODS} from "../constants";

import ExcludedDate from './excludedDate'
import {getExcludedDates, setExcludedDates} from "../../../api";
import only from "only";

export default class ExcludeDateContainer extends React.Component {
  constructor() {
    super();
    this.state = {
      excludedDates: [],
      openIndex: -1,
    };
  }

  /**
   * Handler for changing a excluded date
   * @param index The index of the child
   * @param show Status that indicates whether the child want to be unfold or fold
   * @param state The new state.
   */
  handleExcludedDateChange = (index, show, state) => {
    let list = this.state.excludedDates;
    this.setState({
      openIndex: show ? index : -1
    })

    list[index] = state;

    /*Set both data for child and data for submit.*/
    this.setState({
      excludedDates: list
    });

    this.uploadDates(list);
  };

  /**
   * Handler for deleting a excluded date
   * @param index
   */
  handleExcludedDateDelete = (index) => {
    if (this.state.openIndex === index) {
      this.setState({
        openIndex: -1
      })
    }
    let list = this.state.excludedDates;
    list.splice(index, index + 1);
    this.setState({
      excludedDates: list
    });

    this.uploadDates(list);
  };

  uploadDates(param) {
    setExcludedDates({
      data: param
        .map(item => JSON.stringify(only(item, [
          "name",
          "type",
          "timezone",
          "utcOffset",
          "startDate",
          "endDate",
          "noEnd",
          "repeat",
          "repeatCount",
          "repeatInterval",
          "repeatPeriod"].join(" "))))
    }).then(res => {
      console.log("excluded dates updated");
    });
  }

  /**
   * Handler for adding a new excluded date
   */
  addExcludedDate = () => {
    let list = this.state.excludedDates;
    list.push({});
    this.setState({
      excludedDates: list
    });
    this.uploadDates(list);
  };

  componentDidMount() {
    getExcludedDates().then(res => {
      this.setState({
        excludedDates: res.data.data.map(item => JSON.parse(item))
      });
    });
  }


  /*Apply the selected preset.*/

  render() {
    return (
      <div>
        Excluded Dates
        <div>
          {this.state.excludedDates.length <= 0 ?
            <div className={"config-item"}>There's no excluded
              dates</div> : this.state.excludedDates.map((item, index) => (
              <ExcludedDate key={index}
                            index={index}
                            opened={index === this.state.openIndex}
                            date={item}
                            onEdit={this.handleExcludedDateChange}
                            onDelete={this.handleExcludedDateDelete}
              />
            ))}
          <button type={"button"} className='btn btn-gray' onClick={this.addExcludedDate}>Add</button>
        </div>
      </div>
    );
  }
}
