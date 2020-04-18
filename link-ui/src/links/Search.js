import React, {useState} from 'react';
import {Button, Input, InputGroup, InputGroupAddon} from "reactstrap";

const Search = props => {
    const {onClick} = props;

    const [search, setSearch] = useState('');

    const fire = () => {
        console.log("here>" + search + "<");
        onClick(search)
    };

    let latestId = null;

    const handleInput = () => {
        if (latestId) {
            clearTimeout(latestId);
        } else {
            latestId = setTimeout(fire, 1000);
        }
    };


    return (
        <>
            <InputGroup>
                <Input type="text" placeholder="..." style={{textAlign: "right"}}
                       onChange={(e) => {
                           setSearch(e.target.value);
                           handleInput();
                       }}
                       onPaste={e => {
                           console.log(e.clipboardData);
                           e.clipboardData.getData('Text');
                           handleInput();
                       }}/>
                <InputGroupAddon addonType="append">
                    <Button onClick={() => onClick(search)}>Search</Button>
                </InputGroupAddon>
            </InputGroup>
        </>
    );
};

export default Search;