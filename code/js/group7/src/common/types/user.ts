import { ActionsSiren } from "./actions";
import { LinksSiren } from "./links";

/// Type for passing a user around inside the application
export type UserSession = {
  isLoggedIn: boolean;
  userID: number;
  authCredentials: string;
  username: string;
};

export const defaultUserSession = {
  isLoggedIn: false,
  username: "",
  userID: 0,
  authCredentials: "",
};

export type UserInputModel = {
  username: string;
  password: string;
};

export type UserProperty = {
  user_id: number;
  username: string;
};

export type User = {
  class: Array<string>;
  properties: UserProperty;
  actions: Array<ActionsSiren>;
  links: Array<LinksSiren>;
};
