import React from "react";
import PropTypes from "prop-types";
import {Spinner} from "reactstrap";
import Error from "../../bl/Error";

const Loading = ({loading, content, error}) => {
    return (
        <div>
            {
                loading ? (
                    <div className="text-center mt-3">
                        <Spinner/>
                    </div>
                ) : (error ?
                        <Error error={error}/> :
                        content()
                )
            }
        </div>

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