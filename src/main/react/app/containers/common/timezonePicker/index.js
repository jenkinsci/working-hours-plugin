import React from "react";
import PropTypes from "prop-types";

import timezones from "./timezones";


class TimezonePicker extends React.Component {
  static propTypes = {
    value: PropTypes.string,
    offset: PropTypes.oneOf(["GMT", "UTC"]),
    onChange: PropTypes.func.isRequired,
    className: PropTypes.string,
    style: PropTypes.shape({}),
    inputProps: PropTypes.shape({
      onBlur: PropTypes.func,
      onFocus: PropTypes.func,
      onChange: PropTypes.func,
      onKeyDown: PropTypes.func
    })
  };

  static defaultProps = {
    value: "",
    offset: "GMT",
    className: "",
    style: {},
    inputProps: {}
  };

  state = {
    focus: null,
    query: "",
    currentZone: this.props.value ? timezones.find(zone => zone.name === this.props.value) : null
  };

  static getDerivedStateFromProps(props, state) {
    if (props.value !== (state.currentZone ? state.currentZone.name : "")) {
      return { currentZone: timezones.find(zone => zone.name === props.value) };
    }
    return null;
  }

  stringifyZone(zone, offset) {
    const ensure2Digits = num => (num > 9 ? `${num}` : `0${num}`);

    return `(${offset}${zone.offset < 0 ? "-" : "+"}${ensure2Digits(
      Math.floor(Math.abs(zone.offset))
    )}:${ensure2Digits(Math.abs((zone.offset % 1) * 60))}) ${zone.label}`;
  }

  timezones() {
    if (!this.state.query.trim()) return timezones;

    return timezones.filter(zone =>
      zone.label
        .toLowerCase()
        .replace(/\s+/g, "")
        .includes(this.state.query.toLowerCase().replace(/\s+/g, ""))
    );
  }

  handleFocus = e => {
    this.setState({ focus: 0 });

    if (this.props.inputProps.onFocus) {
      this.props.inputProps.onFocus(e);
    }
  };

  handleBlur = e => {
    this.setState({ focus: null, query: "" });

    if (this.props.inputProps.onBlur) {
      this.props.inputProps.onBlur(e);
    }
  };

  handleChange = (e) => {
    this.setState({ query: e.currentTarget.value, focus: 0 });

    if (this.props.inputProps.onChange) {
      this.props.inputProps.onChange(e);
    }
  };

  handleKeyDown = e => {
    if (e.key === "ArrowDown") {
      e.stopPropagation();
      e.preventDefault();

      const ulElement = e.currentTarget.parentElement.querySelector("ul");
      const zones = this.timezones();
      this.setState(state => {
        const focus = state.focus === zones.length - 1 ? 0 : state.focus + 1;

        this.scrollToElement(ulElement.children[focus]);

        return { focus };
      });
    } else if (e.key === "ArrowUp") {
      e.stopPropagation();
      e.preventDefault();

      const ulElement = e.currentTarget.parentElement.querySelector("ul");
      const zones = this.timezones();
      this.setState(state => {
        const focus = state.focus === 0 ? zones.length - 1 : state.focus - 1;

        this.scrollToElement(ulElement.children[focus]);

        return { focus };
      });
    } else if (e.key === "Escape" && this.input) {
      e.stopPropagation();
      e.preventDefault();

      this.input.blur();
    } else if (e.key === "Enter") {
      e.stopPropagation();
      e.preventDefault();
      e.currentTarget.blur();

      const zones = this.timezones();
      if (zones[this.state.focus]) {
        this.handleChangeZone(zones[this.state.focus]);
      }
    }

    if (this.props.inputProps.onKeyDown) {
      this.props.inputProps.onKeyDown(e);
    }
  };

  handleHoverItem = index => {
    if (index === this.state.focus) return;

    this.setState({ focus: index });
  };

  handleChangeZone = zone => {
    this.props.onChange(zone);

    this.input.blur();
  };

  scrollToElement = element => {
    const ulElement = element.parentElement;

    const topDifference = element.offsetTop - ulElement.scrollTop;
    const bottomDifference =
      ulElement.clientHeight + ulElement.scrollTop - (element.offsetTop + element.offsetHeight);

    if (topDifference < 0) {
      // Scroll top
      ulElement.scrollTop = element.offsetTop;
    }

    if (bottomDifference < 0) {
      // Scroll bottom
      ulElement.scrollTop -= bottomDifference;
    }
  };

  render() {
    const { offset, inputProps } = this.props;
    const { currentZone, focus, query } = this.state;

    const open = focus !== null;

    return (
      <div className={"timezone-div"} style={this.props.style}>
        <input className={"timezone-input input-select"}
               type="text"
               autoComplete="off"
               {...inputProps}
               onFocus={this.handleFocus}
               onBlur={this.handleBlur}
               onChange={this.handleChange}
               onKeyDown={this.handleKeyDown}
               value={currentZone && !open ? this.stringifyZone(currentZone, offset) : query}
               ref={input => {
                 this.input = input;
               }}
        />

        <ul className={(open ? "timezone-open" : "") + " timezone-ul"}>
          {this.timezones().map((zone, index) => (
            <li className={"timezone-li"} key={zone.name}>
              <button
                title={zone.label}
                onMouseDown={() => this.handleChangeZone(zone)}
                onMouseOver={() => this.handleHoverItem(index)}
                onFocus={() => this.handleHoverItem(index)}
                className={(focus === index ? "timezone-button-focus" : "") + " timezone-button"}
              >
                {this.stringifyZone(zone, offset)}
              </button>
            </li>
          ))}
        </ul>

        <style jsx>
          {`
            .timezone-div {
              width:320px;
              display: inline-block;
              font: 13px sans-serif;
              position: relative;
            }
            .timezone-input {
              width: 100%;
              padding: 9px 12px;
              font: inherit;
              box-sizing: border-box;
              outline: 0;
              background: #fff;
              border: 1px solid #e6ebec;
              border-radius: 2px;
              color: #474747;
            }
            .timezone-ul {
              background:white;
              position: absolute;
              top: 100%;
              left: 0;
              right: 0;
              max-height: 200px;
              overflow-y: auto;
              margin: 0;
              padding: 0;
              border: 1px solid #e6ebec;
              margin-top: -1px;
              border-radius: 0 0 3px 3px;
              display: none;
            }
            .timezone-li {
              display: block;
            }
            .timezone-button {
              color: #444;
              padding: 5px 12px;
              cursor: pointer;
              outline: none;
              display: block;
              border: 0;
              width: 100%;
              text-align: left;
              border-radius: 0;
              font: inherit;
              background: transparent;
            }
            .timezone-button-focus {
              background: #f0f0f0;
            }
            .timezone-open {
              display: block;
            }
            .timezone-input::-ms-clear {
              display: none;
            }
          `}
        </style>
      </div>
    );
  }
}

export { TimezonePicker as default, timezones };
