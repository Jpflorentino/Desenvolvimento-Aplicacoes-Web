import React, { useEffect, useState, useContext } from "react";
import { useHistory, Redirect } from "react-router-dom";
import { NavbarContext } from "../navbar/navbarContext";
import base64 from "base-64";
import { UserSessionContext } from "../users/userSessionContext";

/// EditUser component props
type EditUserProps = {
  uri: string;
};

export function EditUser({ uri }: EditUserProps) {
  /// If user is not logged in, redirect to Login / Register page
  const { activeUserSession, updateUserSession } =
    useContext(UserSessionContext);
  if (!activeUserSession.isLoggedIn) return <Redirect to="/loginRegister" />;

  const [newPassword, setNewPassword] = useState("");
  const [submit, setSubmit] = useState(false);

  const history = useHistory();

  /// Update navbar active item
  const { activeItem, updateActiveItem } = useContext(NavbarContext);
  useEffect(() => {
    /// Avoid component side-effects
    if (activeItem !== "user") updateActiveItem("user");
  }, []);

  /// Request user edition
  useEffect(() => {
    if (!submit) return;

    setSubmit(false);

    const body = {
      newPassword: newPassword,
    };

    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Basic " + activeUserSession.authCredentials,
      },
      body: JSON.stringify(body),
    };

    fetch(uri.replace("{userid}", activeUserSession.userID.toString()), options)
      .then((resp) => {
        if (resp.status > 300) return resp.json(); //to handle the errors
      })
      .then((json) => {
        if (json) {
          alert(json.detail);
        } else {
          const creds = base64.encode(
            activeUserSession.username + ":" + newPassword
          );

            /// Avoid component side-effects
            updateUserSession({
              isLoggedIn: activeUserSession.isLoggedIn,
              username: activeUserSession.username,
              userID: activeUserSession.userID,
              authCredentials: creds,
            }); //+userid turns a string into a number*/

          history.push("/user");
        }
      })
      .catch((err) => alert(err));
  }, [submit]);

  function handleUserEditSubmit() {
    setSubmit(true);
  }

  return (
    <div className="ui form">
      <div className="fields">
        <div className="required field">
          <label>New Password</label>
          <input
            type="password"
            name="password"
            placeholder="New Password"
            onChange={(event) => {
              if (event.target.value != "") setNewPassword(event.target.value);
              else alert("You need to input a password");
            }}
          />
        </div>
      </div>

      <button
        className="ui button"
        type="submit"
        onClick={handleUserEditSubmit}
      >
        Change It!
      </button>
    </div>
  );
}
