import React, { useState, useEffect, useContext } from "react";
import { Login } from "./login";
import { Signup } from "./signup";
import { Redirect } from "react-router-dom";
import { NavbarContext } from "../navbar/navbarContext";
import { UserSessionContext} from "../users/userSessionContext";

/// LoginRegister component props
type LoginRegisterProps = {
  getUserURI: string;
  createUserURI: string;
}

export function LoginRegister({
  getUserURI,
  createUserURI,
}: LoginRegisterProps ) {

  /// useContext
  const { activeUserSession } = useContext(UserSessionContext);
  if (activeUserSession.isLoggedIn) return <Redirect to="/" />;

  const [login, setLoginState] = useState(true);

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => { /// Avoid component side-effects
    if(activeItem !== "Login or Register") updateActiveItem("Login or Register");
  }, [])

  function handleLogInForm(login: boolean) {
    setLoginState(login);
  }

  return login ?
  
    <div>
      <Login getUserURI={getUserURI} handleLogInForm={handleLogInForm} />;
    </div>
      
    :
    
    <div>
      <Signup createUserURI={createUserURI} handleLogInForm={handleLogInForm} />;
    </div>
  
}
