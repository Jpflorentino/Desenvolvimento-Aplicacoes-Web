import { defaultUserSession } from "../common/types/user";
import { Redirect } from "react-router-dom";
import React, { useEffect, useContext } from "react";
import { UserSessionContext} from "../users/userSessionContext";

export function Logout() {

    /// Update user session
    const { activeUserSession, updateUserSession } = useContext(UserSessionContext);
    
    useEffect(() => { /// Avoid component side-effects
      if(activeUserSession !== defaultUserSession) updateUserSession(defaultUserSession);
    }, [])

    return (
      <Redirect to="/"/>  
    )
    
}