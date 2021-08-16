import React from "react";
import { UserSession, defaultUserSession } from "../common/types/user";

type UserSessionContextType = {
    activeUserSession : UserSession,
    updateUserSession : (newUserSession : UserSession) => void
  }
  
/// Create UserSession context with default values
export const UserSessionContext = React.createContext<UserSessionContextType>({
    activeUserSession: defaultUserSession,
    updateUserSession : () => {},
});

  