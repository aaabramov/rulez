import jwt_decode from "jwt-decode";

export const Permissions = {
    CREATE: 'create',
    EDIT: 'edit',
    DELETE: 'delete',
    VIEW: 'view'
};

/*
"auth": {
  "email": "1",
  "role": "default",
  "permissions": [
    "view",
    "edit",
    "delete",
    "create"
  ],
  "jwt": "..."
}
 */
export const Auth = {
    isSignedIn: () => !!localStorage.getItem("auth"),

    jwt: () => {
        if (Auth.isSignedIn())
            return JSON.parse(localStorage.getItem("auth"));
        else
            return null;
    },
    logout: () => localStorage.removeItem("auth"),

    login: (jwt) => {
        const decoded = jwt_decode(jwt);
        localStorage.setItem("auth", JSON.stringify({
            ...decoded,
            jwt
        }))
    },

    permissions: () => {
        if (Auth.isSignedIn())
            return new Set(Auth.jwt().permissions);
        else
            return null;
    },

    hasPermission: (name) => {
        if (Auth.isSignedIn())
            return Auth.permissions().has(name);
        else
            return null;
    },

    email: () => {
        if (Auth.isSignedIn())
            return Auth.jwt().email;
        else
            return null;
    }

};