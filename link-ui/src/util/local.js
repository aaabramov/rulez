export const checkLoggedIn = () => localStorage.getItem("is_logged_in") === "true";

export const setLoggedIn = (value) => localStorage.setItem("is_logged_in", String(value));