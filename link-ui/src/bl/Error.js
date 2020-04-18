import React from 'react';

const Error = props => {
    return (
        <>
            <h1>An error occurred</h1>
            <pre>
            <code>
                {JSON.stringify(props.error, null ,2)}
            </code>
            </pre>
        </>
    );
};

export default Error;