import React from 'react';
import PropTypes from 'prop-types';
import {Button} from "reactstrap";
import {FaArrowLeft} from "react-icons/all";
import './ContentHeader.css'

const ContentHeader = ({history, title, buttons}) => {

    return (
        <>
            <h3>
                <div className="clearfix">
                    <div className="float-left">
                        <Button size="sm"
                                color="transparent"
                                className="btn-move-back"
                                onClick={_ => history.goBack()}
                        >
                            <FaArrowLeft/>
                        </Button>
                        {title}
                    </div>
                    <div className="float-right">
                        {
                            buttons.map((b, i) =>
                                <React.Fragment key={i}>
                                    {b}
                                </React.Fragment>
                            )
                        }
                    </div>
                </div>
            </h3>
        </>
    );
};

ContentHeader.propTypes = {
    title: PropTypes.oneOfType([PropTypes.string.isRequired, PropTypes.element]),
    history: PropTypes.object.isRequired,
    buttons: PropTypes.array
};

ContentHeader.defaultProps = {
    buttons: []
};

export default ContentHeader;