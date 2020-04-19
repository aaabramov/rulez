import axios from "axios";

export const fetchRoles = (onError) => {
    return axios('http://localhost:9000/api/v1/roles')
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const fetchRoleWithPermissions = (roleId, onError) => {
    return axios(`http://localhost:9000/api/v1/roles/${roleId}?withPermissions=true`)
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const switchRole = (id, on, onError) => {
    return axios(`http://localhost:9000/api/v1/roles/${id}/${on ? 'enable' : 'disable'}`)
        .then(response => response.data)
        .catch(error => onError && onError(error));
};

export const rolesWithUsers = (id, onError) => {
    return axios(`http://localhost:9000/api/v1/roles/${id}/users`)
        .then(response => {
            const {role, users} = response.data;
            return [role, users];
        })
        .catch(error => onError && onError(error));
};
