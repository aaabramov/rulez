import React from "react";
import Moment from "react-moment";
import PropTypes from "prop-types";

const DateTime = ({date}) => {
    return <Moment format="HH:mm DD.MM.YYYY" withTitle>{date}</Moment>
};

DateTime.propTypes = {
    date: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
};

export default DateTime;