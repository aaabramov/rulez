import axios from "axios";

export const fetchPermissions = (onError) => {
    return axios('http://localhost:9000/api/v1/permissions')
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const createPermission = (permission, onError) => {
    return axios.post(
        'http://localhost:9000/api/v1/permissions',
        {...permission, author: ''}
    )
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const switchPermission = (id, on, onError) => {
    return axios(`http://localhost:9000/api/v1/permissions/${id}/${on ? 'enable' : 'disable'}`)
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const permissionWithUsers = (id, onError) => {
    return axios.all([
        axios(`http://localhost:9000/api/v1/permissions/${id}`),
        axios(`http://localhost:9000/api/v1/permissions/${id}/users`)
    ])
        .then(axios.spread((permission, users) => {
            return [permission.data, users.data];
        }))
        .catch(error => onError && onError(error));
};
