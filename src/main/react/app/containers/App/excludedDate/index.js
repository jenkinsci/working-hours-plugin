import React from "react";

import ExcludedDate from './excludedDate'
import {getExcludedDates, setExcludedDates} from "../../../api";
import only from "only";
import {LOADING_STATE, LoadingState} from "../../common/savingState";
import {debounce} from "lodash";

export default class ExcludeDateContainer extends React.Component {
  constructor() {
    super();
    this.state = {
      excludedDates: [],
      openIndex: -1,
      loadingState: LOADING_STATE.WAITING,
    };
  }

  /*A debounced function used to clear loading state.
* */
  debouncedClearLoading = debounce(() => {
    this.setState({
      loadingState: LOADING_STATE.WAITING
    })
  }, 1000)

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
    this.setState({
      loadingState: LOADING_STATE.LOADING
    })
    setExcludedDates({
      data: param
        .map(item => only(item, [
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
          "repeatPeriod"].join(" ")))
    }).then(res => {
      this.setState({
        loadingState: LOADING_STATE.SUCCESS
      })
      this.debouncedClearLoading()
    }).catch(err => {
      this.setState({
        loadingState: LOADING_STATE.FAIL
      })
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
    this.setState({
      loadingState: LOADING_STATE.LOADING
    })
    getExcludedDates().then(res => {
      this.setState({
        excludedDates: res.data.data,
        loadingState: LOADING_STATE.SUCCESS
      });
    }).catch(err => {
      console.log(err)
      this.setState({
        loadingState: LOADING_STATE.FAIL
      })
    })
  }


  /*Apply the selected preset.*/

  render() {
    return (
      <div>
        <div className={"config-header"}>
          <div className={'config-title'}>Excluded Dates</div>
          <div className={'config-loading-status'}>
            <LoadingState loadingState={this.state.loadingState}/>
          </div>
          <div className={'config-count'}>Total:{this.state.excludedDates.length}</div>
        </div>
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
