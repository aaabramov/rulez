import React from "react";
import PropTypes from "prop-types";
import {Spinner} from "reactstrap";
import Error from "../../bl/Error";

const Loading = ({loading, content, error}) => {
    return (
        <>
            {
                loading ? <Spinner/> : error ? <Error error={error}/> : content()
            }
        </>

    );
};

Loading.propTypes = {
    loading: PropTypes.bool.isRequired,
    error: PropTypes.object,
    content: PropTypes.func.isRequired
};

Loading.defaultProps = {
    error: null
};

export default Loading;